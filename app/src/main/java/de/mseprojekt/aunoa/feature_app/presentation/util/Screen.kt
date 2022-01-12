package de.mseprojekt.aunoa.feature_app.presentation.util

sealed class Screen(val route: String) {
    object OperationScreen: Screen("operation_screen")
    object AddRuleScreen: Screen("add_rule_screen")
    object MyRulesScreen: Screen("my_rules_screen")
    object RulesDetailsScreen: Screen("rules_details_screen")
    object RulesHubScreen: Screen("rules_hub_screen")
}