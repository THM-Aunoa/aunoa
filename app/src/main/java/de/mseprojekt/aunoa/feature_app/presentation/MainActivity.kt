package de.mseprojekt.aunoa.feature_app.presentation

import android.Manifest
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import de.mseprojekt.aunoa.feature_app.presentation.rule_details.RuleDetailsScreen
import de.mseprojekt.aunoa.feature_app.presentation.operation.ActivityScreen
import de.mseprojekt.aunoa.feature_app.presentation.add_rule.AddRuleScreen
import de.mseprojekt.aunoa.feature_app.presentation.my_rules.MyRulesScreen
import de.mseprojekt.aunoa.feature_app.presentation.rules_hub.RulesHubScreen
import de.mseprojekt.aunoa.feature_app.presentation.util.Screen
import de.mseprojekt.aunoa.other.foregroundStartService
import de.mseprojekt.aunoa.ui.theme.AunoaTheme


@ExperimentalPermissionsApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AunoaTheme {
                val permissionsState = rememberMultiplePermissionsState(
                    permissions = listOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_NOTIFICATION_POLICY
                    )
                )
                if (permissionsState.allPermissionsGranted) {
                    Log.d("Aunoa", "all permissions granted")
                    val manager =
                        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                    if (!manager.isNotificationPolicyAccessGranted) {
                        val intent = Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
                        startActivity(intent)
                    }
                    foregroundStartService("Start")
                }
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = Screen.ActivityScreen.route
                ) {
                    composable(route = Screen.ActivityScreen.route) {
                        ActivityScreen(
                            navController = navController,
                            permissionsState = permissionsState
                        )
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
