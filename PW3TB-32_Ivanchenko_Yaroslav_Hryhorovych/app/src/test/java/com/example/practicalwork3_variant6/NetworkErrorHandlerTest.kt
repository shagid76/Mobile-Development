package com.example.practicalwork3_variant6

import com.example.practicalwork3_variant6.data.remote.NetworkErrorHandler
import com.example.practicalwork3_variant6.data.remote.NetworkErrorType
import com.example.practicalwork3_variant6.data.remote.NetworkResult
import com.example.practicalwork3_variant6.data.remote.safeApiCall
import com.example.practicalwork3_variant6.data.remote.safeApiCallWithRetry
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class NetworkErrorHandlerTest {


    @Test
    fun `classify UnknownHostException as NO_INTERNET`() {
        assertEquals(NetworkErrorType.NO_INTERNET, NetworkErrorHandler.classify(UnknownHostException()))
    }

    @Test
    fun `classify SocketTimeoutException as TIMEOUT`() {
        assertEquals(NetworkErrorType.TIMEOUT, NetworkErrorHandler.classify(SocketTimeoutException()))
    }

    @Test
    fun `classify generic IOException as NO_INTERNET`() {
        assertEquals(NetworkErrorType.NO_INTERNET, NetworkErrorHandler.classify(IOException("broken pipe")))
    }

    @Test
    fun `classify unknown throwable as UNKNOWN`() {
        assertEquals(NetworkErrorType.UNKNOWN, NetworkErrorHandler.classify(RuntimeException("boom")))
    }

    @Test
    fun `classifyHttpCode 500-599 as SERVER_ERROR`() {
        assertEquals(NetworkErrorType.SERVER_ERROR, NetworkErrorHandler.classifyHttpCode(500))
        assertEquals(NetworkErrorType.SERVER_ERROR, NetworkErrorHandler.classifyHttpCode(503))
    }

    @Test
    fun `classifyHttpCode 400-499 as CLIENT_ERROR`() {
        assertEquals(NetworkErrorType.CLIENT_ERROR, NetworkErrorHandler.classifyHttpCode(401))
        assertEquals(NetworkErrorType.CLIENT_ERROR, NetworkErrorHandler.classifyHttpCode(404))
    }

    @Test
    fun `classifyHttpCode other codes as UNKNOWN`() {
        assertEquals(NetworkErrorType.UNKNOWN, NetworkErrorHandler.classifyHttpCode(200))
    }


    @Test
    fun `userMessage for CLIENT_ERROR 401 mentions authorization`() {
        val message = NetworkErrorType.CLIENT_ERROR.userMessage(401)
        assertTrue(message.contains("авториз", ignoreCase = true))
    }

    @Test
    fun `userMessage for SERVER_ERROR includes http code`() {
        val message = NetworkErrorType.SERVER_ERROR.userMessage(503)
        assertTrue(message.contains("503"))
    }


    @Test
    fun `safeApiCall returns Success for successful response with body`() = runBlocking {
        val response = Response.success("дані")
        val result = safeApiCall { response }
        assertTrue(result is NetworkResult.Success)
        assertEquals("дані", (result as NetworkResult.Success).data)
    }

    @Test
    fun `safeApiCall returns Error for non-2xx http response`() = runBlocking {
        val errorBody = "{\"message\":\"not found\"}".toResponseBody("application/json".toMediaType())
        val response = Response.error<String>(404, errorBody)
        val result = safeApiCall { response }
        assertTrue(result is NetworkResult.Error)
        assertEquals(404, (result as NetworkResult.Error).code)
    }

    @Test
    fun `safeApiCall returns Error without throwing when call throws IOException`() = runBlocking {
        val result = safeApiCall<String> { throw IOException("мережа недоступна") }
        assertTrue(result is NetworkResult.Error)
        assertEquals(null, (result as NetworkResult.Error).code)
    }


    @Test
    fun `safeApiCallWithRetry does not retry on 4xx client error`() = runBlocking {
        var attempts = 0
        val errorBody = "{}".toResponseBody("application/json".toMediaType())
        val result = safeApiCallWithRetry<String>(maxAttempts = 3, delayMillis = 1) {
            attempts++
            Response.error(404, errorBody)
        }
        assertEquals(1, attempts) // 4xx не повторюється
        assertTrue(result is NetworkResult.Error)
    }

    @Test
    fun `safeApiCallWithRetry retries on 5xx server error up to maxAttempts`() = runBlocking {
        var attempts = 0
        val errorBody = "{}".toResponseBody("application/json".toMediaType())
        val result = safeApiCallWithRetry<String>(maxAttempts = 3, delayMillis = 1) {
            attempts++
            Response.error(500, errorBody)
        }
        assertEquals(3, attempts) // усі 3 спроби використані, бо завжди 5xx
        assertTrue(result is NetworkResult.Error)
    }

    @Test
    fun `safeApiCallWithRetry stops retrying once a success arrives`() = runBlocking {
        var attempts = 0
        val errorBody = "{}".toResponseBody("application/json".toMediaType())
        val result = safeApiCallWithRetry<String>(maxAttempts = 3, delayMillis = 1) {
            attempts++
            if (attempts < 2) Response.error(500, errorBody) else Response.success("ok")
        }
        assertEquals(2, attempts)
        assertTrue(result is NetworkResult.Success)
        assertEquals("ok", (result as NetworkResult.Success).data)
    }
}
