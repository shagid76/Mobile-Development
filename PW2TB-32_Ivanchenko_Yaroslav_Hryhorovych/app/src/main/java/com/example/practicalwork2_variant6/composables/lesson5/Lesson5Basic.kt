package com.example.practicalwork2_variant6.composables.lesson5

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.practicalwork2_variant6.composables.common.MenuButton
import com.example.practicalwork2_variant6.composables.common.ScreenTitle

@Composable
fun BasicNavApp() {
    val navController = rememberNavController()
    BasicNavHostController(navController)
}

@Composable
private fun BasicNavHostController(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.BasicScreen.Menu.route) {
        composable(Screen.BasicScreen.Menu.route) { BasicMenuScreen(navController) }
        composable(Screen.BasicScreen.About.route) { BasicAboutScreen(navController) }
        composable(Screen.BasicScreen.Status.route) { BasicStatusScreen(navController) }
    }
}

@Composable
private fun BasicMenuScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center
    ) {
        ScreenTitle("Головне меню")
        MenuButton("Про систему") { navController.navigate(Screen.BasicScreen.About.route) }
        MenuButton("Стан реактора") { navController.navigate(Screen.BasicScreen.Status.route) }
    }
}

@Composable
private fun BasicAboutScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        ScreenTitle("Про систему")
        Text("Біогазова установка переробляє органічну сировину у біогаз для подальшого використання як палива.")
        MenuButton("Повернутися в меню") { navController.navigate(Screen.BasicScreen.Menu.route) }
    }
}

@Composable
private fun BasicStatusScreen(navController: NavHostController) {
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
        MenuButton("Повернутися в меню") { navController.navigate(Screen.BasicScreen.Menu.route) }
    }
}
