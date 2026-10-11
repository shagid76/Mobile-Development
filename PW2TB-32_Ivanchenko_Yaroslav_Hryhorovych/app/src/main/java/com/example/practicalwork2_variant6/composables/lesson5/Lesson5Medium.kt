package com.example.practicalwork2_variant6.composables.lesson5

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.practicalwork2_variant6.composables.common.MenuButton
import com.example.practicalwork2_variant6.composables.common.ScreenTitle

@Composable
fun MediumNavApp() {
    val navController = rememberNavController()
    MediumNavHostController(navController)
}

@Composable
private fun MediumNavHostController(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.MediumScreen.Menu.route) {
        composable(Screen.MediumScreen.Menu.route + "/{operatorName}",
            arguments = listOf(navArgument("operatorName") { type = NavType.StringType; defaultValue = "Оператор" })
        ) { backStackEntry ->
            val operatorName = backStackEntry.arguments?.getString("operatorName") ?: "Оператор"
            MediumMenuScreen(navController, operatorName)
        }
        composable(Screen.MediumScreen.Menu.route) { MediumMenuScreen(navController, "Оператор") }
        composable(Screen.MediumScreen.About.route) { MediumAboutScreen(navController) }
        composable(Screen.MediumScreen.OperatorInfo.route) { MediumOperatorInfoScreen(navController) }
        composable(Screen.MediumScreen.Status.route) { MediumStatusScreen(navController) }
    }
}

@Composable
private fun MediumMenuScreen(navController: NavHostController, operatorName: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center
    ) {
        ScreenTitle("Головне меню")
        Text("Поточний оператор: $operatorName")
        MenuButton("Про систему") { navController.navigate(Screen.MediumScreen.About.route) }
        MenuButton("Інформація про оператора") { navController.navigate(Screen.MediumScreen.OperatorInfo.route) }
        MenuButton("Стан реактора") { navController.navigate(Screen.MediumScreen.Status.route) }
    }
}

@Composable
private fun MediumAboutScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        ScreenTitle("Про систему")
        Text("Біогазова установка переробляє органічну сировину у біогаз для подальшого використання як палива.")
        MenuButton("Повернутися в меню") { navController.navigate(Screen.MediumScreen.Menu.route) }
    }
}

@Composable
private fun MediumOperatorInfoScreen(navController: NavHostController) {
    var name by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center
    ) {
        ScreenTitle("Інформація про оператора")
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Ім'я оператора") },
            modifier = Modifier.fillMaxWidth()
        )
        MenuButton("Зберегти та повернутись у меню") {
            val safeName = name.ifBlank { "Оператор" }
            navController.navigate(Screen.MediumScreen.Menu.route + "/$safeName") {
                popUpTo(Screen.MediumScreen.Menu.route) { inclusive = true }
            }
        }
    }
}

@Composable
private fun MediumStatusScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        ScreenTitle("Стан реактора")
        Text("Наповнення: 60%")
        Text("Концентрація метану: 55%")
        Text("Температура: 36 °C")
        MenuButton("Повернутися в меню") { navController.navigate(Screen.MediumScreen.Menu.route) }
    }
}
