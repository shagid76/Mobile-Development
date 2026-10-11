package com.example.practicalwork2_variant6.composables.lesson5

sealed class Screen(val route: String) {

    sealed class BasicScreen(route: String) : Screen(route) {
        object Menu : BasicScreen("basic_menu")
        object About : BasicScreen("basic_about")
        object Status : BasicScreen("basic_status")
    }

    sealed class MediumScreen(route: String) : Screen(route) {
        object Menu : MediumScreen("medium_menu")
        object About : MediumScreen("medium_about")
        object OperatorInfo : MediumScreen("medium_operator_info")
        object Status : MediumScreen("medium_status")
    }

    sealed class AdvancedScreen(route: String) : Screen(route) {
        object Menu : AdvancedScreen("advanced_menu")
        object About : AdvancedScreen("advanced_about")
        object OperatorInfo : AdvancedScreen("advanced_operator_info")
        object Status : AdvancedScreen("advanced_status")
        object Details : AdvancedScreen("advanced_details")
    }
}
