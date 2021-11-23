package de.mseprojekt.aunoa.feature_app.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import de.mseprojekt.aunoa.feature_app.presentation.rule_details.RuleDetailsScreen
import de.mseprojekt.aunoa.feature_app.presentation.actvity.ActivityScreen
import de.mseprojekt.aunoa.feature_app.presentation.add_rule.AddRuleScreen
import de.mseprojekt.aunoa.feature_app.presentation.my_rules.MyRulesScreen
import de.mseprojekt.aunoa.feature_app.presentation.rules_hub.RulesHubScreen
import de.mseprojekt.aunoa.feature_app.presentation.util.Screen
import de.mseprojekt.aunoa.ui.theme.AunoaTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AunoaTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = Screen.ActivityScreen.route
                ) {
                    composable(route = Screen.ActivityScreen.route) {
                        ActivityScreen(navController = navController)
                    }
                    composable(
                        route = Screen.RulesDetailsScreen.route +
                                "?rulesId={rulesId}",
                        arguments = listOf(
                            navArgument(
                                name = "rulesId"
                            ) {
                                type = NavType.IntType
                                defaultValue = -1
                            }
                        )
                    ) {
                        RuleDetailsScreen(
                            navController = navController
                        )
                    }
                    composable(route = Screen.AddRuleScreen.route) {
                        AddRuleScreen(navController = navController)
                    }
                    composable(route = Screen.MyRulesScreen.route) {
                        MyRulesScreen(navController = navController)
                    }
                    composable(route = Screen.RulesHubScreen.route) {
                        RulesHubScreen(navController = navController)
                    }
                }

            }
        }
    }
}
