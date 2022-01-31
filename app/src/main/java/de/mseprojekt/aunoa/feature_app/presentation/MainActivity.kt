package de.mseprojekt.aunoa.feature_app.presentation

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionsRequired
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import de.mseprojekt.aunoa.feature_app.domain.model.actionObjects.ActionObject
import de.mseprojekt.aunoa.feature_app.domain.model.actionObjects.VolumeAction
import de.mseprojekt.aunoa.feature_app.domain.model.triggerObjects.CellTrigger
import de.mseprojekt.aunoa.feature_app.domain.model.triggerObjects.TimeTrigger
import de.mseprojekt.aunoa.feature_app.domain.model.triggerObjects.TriggerObject
import de.mseprojekt.aunoa.feature_app.domain.use_case.cell.CellUseCases
import de.mseprojekt.aunoa.feature_app.domain.use_case.rule.RuleUseCases
import de.mseprojekt.aunoa.feature_app.domain.use_case.state.StateUseCases
import de.mseprojekt.aunoa.feature_app.presentation.edit_rule.EditRuleScreen
import de.mseprojekt.aunoa.feature_app.presentation.my_rules.MyRulesScreen
import de.mseprojekt.aunoa.feature_app.presentation.operation.OperationScreen
import de.mseprojekt.aunoa.feature_app.presentation.rule_details.RuleDetailsScreen
import de.mseprojekt.aunoa.feature_app.presentation.rules_hub.RulesHubScreen
import de.mseprojekt.aunoa.feature_app.presentation.util.Screen
import de.mseprojekt.aunoa.other.foregroundStartService
import de.mseprojekt.aunoa.ui.theme.AunoaTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import javax.inject.Inject

@ExperimentalMaterialApi
@ExperimentalPermissionsApi
@AndroidEntryPoint
class MainActivity : ComponentActivity(
) {
    @Inject
    lateinit var stateUseCases: StateUseCases

    @Inject
    lateinit var cellUseCases: CellUseCases

    @Inject
    lateinit var ruleUseCases: RuleUseCases


    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AunoaTheme {
                val manager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                var timer by remember {
                    mutableStateOf(100)
                }
                var isRunning by remember {
                    mutableStateOf(false)
                }
                var policyAccess by remember {
                    mutableStateOf(manager.isNotificationPolicyAccessGranted)
                }
                LaunchedEffect(key1 = isRunning, key2 = timer) {
                    if (isRunning && timer > 0) {
                        delay(100L)
                        timer += 100
                        policyAccess = manager.isNotificationPolicyAccessGranted
                    }
                }
                val permissionsState =
                    rememberMultiplePermissionsState(
                        permissions = listOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_NOTIFICATION_POLICY,
                            Manifest.permission.ACCESS_WIFI_STATE,
                            Manifest.permission.ACCESS_NETWORK_STATE,
                            Manifest.permission.INTERNET
                        )
                    )
                PermissionsRequired(
                    multiplePermissionsState = permissionsState,
                    permissionsNotGrantedContent = {
                        AlertDialog(
                            onDismissRequest = {
                            },
                            title = {
                                Text(text = "Hallo Nutzer")
                            },
                            text = {
                                Text(
                                    "wir wollen dir helfen dein Zusammenleben mit deinem Smartphone zu verbessern. Wir brauchen dafür Zugriff auf deinen Standort, damit wir deine Aktionen auch an den richtigen Orten ausführen können."
                                )
                            },
                            confirmButton = {
                                Button(onClick = {
                                    permissionsState.launchMultiplePermissionRequest()
                                }) {
                                    Text("Ok!")
                                }

                            }
                        )
                    },
                    permissionsNotAvailableContent = {
                        Column(Modifier.padding(30.dp)) {
                            val context = LocalContext.current
                            Text(
                                "Du hast uns leider nicht die benötigten Berechtigungen zur Abfrage deines Standorts erteilt." +
                                        "Da wir dir die bestmögliche Erfahrung bieten können, musst du diese Berechtigungen erteilen. " +
                                        "Du kannst dafür direkt über den Button in die Einstellungen wechseln und danach direkt zurück in die App wechseln."
                            )
                            Button(onClick = {
                                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                val uri: Uri = Uri.fromParts("package", packageName, null)
                                intent.data = uri
                                startActivity(intent)
                            }, Modifier.padding(top = 20.dp)) {
                                Text("Einstellungen öffnen")
                            }
                        }
                    },
                    content = {
                        val secondPermissionsState =
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                rememberMultiplePermissionsState(
                                    permissions = listOf(
                                        Manifest.permission.ACCESS_FINE_LOCATION,
                                        Manifest.permission.ACCESS_COARSE_LOCATION,
                                        Manifest.permission.ACCESS_NOTIFICATION_POLICY,
                                        Manifest.permission.ACCESS_WIFI_STATE,
                                        Manifest.permission.ACCESS_NETWORK_STATE,
                                        Manifest.permission.INTERNET,
                                        Manifest.permission.ACCESS_BACKGROUND_LOCATION
                                    )
                                )
                            } else {
                                permissionsState
                            }
                        PermissionsRequired(
                            multiplePermissionsState = secondPermissionsState,
                            permissionsNotGrantedContent = {
                                AlertDialog(
                                    onDismissRequest = {
                                    },
                                    title = {
                                        Text(text = "Hallo Nutzer")
                                    },
                                    text = {
                                        Text(
                                            "wir wollen dir helfen dein Zusammenleben mit deinem Smartphone zu verbessern. Wir brauchen dafür Zugriff auf deinen Standort, damit wir deine Aktionen auch an den richtigen Orten ausführen können."
                                        )
                                    },
                                    confirmButton = {
                                        Button(onClick = {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                                val permissions = arrayOf(
                                                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                                                )
                                                this.requestPermissions(permissions, 1)
                                            }
                                        }) {
                                            Text("Ok!")
                                        }

                                    }
                                )
                            },
                            permissionsNotAvailableContent = {
                                Column(Modifier.padding(30.dp)) {
                                    val context = LocalContext.current
                                    Text(
                                        "Du hast uns leider nicht die benötigten Berechtigungen zur Abfrage deines Standorts erteilt." +
                                                "Da wir dir die bestmögliche Erfahrung bieten können, musst du diese Berechtigungen erteilen. " +
                                                "Du kannst dafür direkt über den Button in die Einstellungen wechseln und danach direkt zurück in die App wechseln."
                                    )
                                    Button(onClick = {
                                        val intent =
                                            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                        val uri: Uri = Uri.fromParts("package", packageName, null)
                                        intent.data = uri
                                        startActivity(intent)
                                    }, Modifier.padding(top = 20.dp)) {
                                        Text("Einstellungen öffnen")
                                    }
                                }
                            },
                            content = {
                                when {
                                    !policyAccess -> {
                                        isRunning = true
                                        AlertDialog(
                                            onDismissRequest = {
                                            },
                                            title = {
                                                Text(text = "Hallo Nutzer")
                                            },
                                            text = {
                                                Text(
                                                    "wir wollen dir Helfen dein Zusammenleben mit deinem Smartphone zu verbessern. Du musst uns erlauben dein Smartphone stummzuschalten, damit wir dies für dich in Zukunft automatisch erledigen können."
                                                )
                                            },
                                            confirmButton = {
                                                Button(onClick = {
                                                    val intent =
                                                        Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
                                                    startActivity(intent)
                                                }) {
                                                    Text("Ok!")
                                                }

                                            }
                                        )
                                    }
                                    else -> {
                                        isRunning = false
                                        CoroutineScope(Dispatchers.Main).launch {
                                            if (stateUseCases.isFirstRun()) {
                                                stateUseCases.insertState(true)
                                                cellUseCases.insertRegion("Home", 1)
                                            }
                                            foregroundStartService("Start")

                                        }

                                        val systemUiController = rememberSystemUiController()
                                        systemUiController.setSystemBarsColor(
                                            color = MaterialTheme.colors.primary
                                        )

                                        val navController = rememberNavController()
                                        NavHost(
                                            navController = navController,
                                            startDestination = Screen.OperationScreen.route
                                        ) {
                                            composable(route = Screen.OperationScreen.route) {
                                                OperationScreen(
                                                    navController = navController
                                                )
                                            }
                                            composable(
                                                route = Screen.RulesDetailsScreen.route +
                                                        "?ruleId={ruleId}",
                                                arguments = listOf(
                                                    navArgument(
                                                        name = "ruleId"
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
                                            composable(route = Screen.EditRuleScreen.route +
                                                    "?ruleId={ruleId}",
                                                arguments = listOf(
                                                    navArgument(
                                                        name = "ruleId"
                                                    ) {
                                                        type = NavType.IntType
                                                        defaultValue = -1
                                                    }
                                                )
                                            ) {
                                                EditRuleScreen(navController = navController)
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
                            })

                    })
            }
        }
    }
}
