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
import com.example.practicalwork2_variant6.composables.common.InfoRow
import com.example.practicalwork2_variant6.composables.common.MenuButton
import com.example.practicalwork2_variant6.composables.common.ScreenTitle
import com.example.practicalwork2_variant6.composables.common.statusLabelColor
import com.example.practicalwork2_variant6.domain.ProcessStatus
import com.example.practicalwork2_variant6.domain.label

@Composable
fun AdvancedNavApp() {
    val navController = rememberNavController()
    AdvancedNavHostController(navController)
}

@Composable
private fun AdvancedNavHostController(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.AdvancedScreen.Menu.route) {
        composable(Screen.AdvancedScreen.Menu.route) { AdvancedMenuScreen(navController, "Оператор") }
        composable(
            Screen.AdvancedScreen.Menu.route + "/{operatorName}",
            arguments = listOf(navArgument("operatorName") { type = NavType.StringType })
        ) { backStackEntry ->
            val operatorName = backStackEntry.arguments?.getString("operatorName") ?: "Оператор"
            AdvancedMenuScreen(navController, operatorName)
        }
        composable(Screen.AdvancedScreen.About.route) { AdvancedAboutScreen(navController) }
        composable(Screen.AdvancedScreen.OperatorInfo.route) { AdvancedOperatorInfoScreen(navController) }
        composable(Screen.AdvancedScreen.Status.route) { AdvancedStatusScreen(navController) }
        composable(
            Screen.AdvancedScreen.Details.route + "/{methane}",
            arguments = listOf(navArgument("methane") { type = NavType.IntType })
        ) { backStackEntry ->
            val methane = backStackEntry.arguments?.getInt("methane") ?: 0
            AdvancedDetailsScreen(navController, methane)
        }
    }
}

@Composable
private fun AdvancedMenuScreen(navController: NavHostController, operatorName: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center
    ) {
        ScreenTitle("Головне меню")
        Text("Поточний оператор: $operatorName")
        MenuButton("Про систему") { navController.navigate(Screen.AdvancedScreen.About.route) }
        MenuButton("Інформація про оператора") { navController.navigate(Screen.AdvancedScreen.OperatorInfo.route) }
        MenuButton("Стан реактора") { navController.navigate(Screen.AdvancedScreen.Status.route) }
    }
}

@Composable
private fun AdvancedAboutScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        ScreenTitle("Про систему")
        Text("Біогазова установка переробляє органічну сировину у біогаз для подальшого використання як палива.")
        MenuButton("Повернутися в меню") { navController.navigate(Screen.AdvancedScreen.Menu.route) }
    }
}

@Composable
private fun AdvancedOperatorInfoScreen(navController: NavHostController) {
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
            navController.navigate(Screen.AdvancedScreen.Menu.route + "/$safeName") {
                popUpTo(Screen.AdvancedScreen.Menu.route) { inclusive = true }
            }
        }
    }
}

@Composable
private fun AdvancedStatusScreen(navController: NavHostController) {
    val methane = 55
    val status = ProcessStatus.fromMethane(methane)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        ScreenTitle("Стан реактора")
        InfoRow("Наповнення", "60%")
        InfoRow("Концентрація метану", "$methane%")
        InfoRow("Статус процесу", status.label(), statusLabelColor(status == ProcessStatus.CRITICAL, status == ProcessStatus.WARNING))
        MenuButton("Детальніше") { navController.navigate(Screen.AdvancedScreen.Details.route + "/$methane") }
        MenuButton("Повернутися в меню") { navController.navigate(Screen.AdvancedScreen.Menu.route) }
    }
}

@Composable
private fun AdvancedDetailsScreen(navController: NavHostController, methane: Int) {
    val status = ProcessStatus.fromMethane(methane)
    val description = when (status) {
        ProcessStatus.STABLE -> "Концентрація метану в нормі (50–70%), процес бродіння протікає стабільно."
        ProcessStatus.WARNING -> "Концентрація метану відхиляється від норми — рекомендується посилений контроль."
        ProcessStatus.CRITICAL -> "Критичне відхилення концентрації метану — потрібне негайне втручання оператора."
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        ScreenTitle("Деталі стану процесу")
        InfoRow("Концентрація метану", "$methane%")
        InfoRow("Статус", status.label(), statusLabelColor(status == ProcessStatus.CRITICAL, status == ProcessStatus.WARNING))
        Text(description, modifier = Modifier.padding(top = 12.dp, bottom = 12.dp))
        MenuButton("Повернутися до стану") { navController.popBackStack() }
        MenuButton("Повернутися в меню") { navController.navigate(Screen.AdvancedScreen.Menu.route) }
    }
}
