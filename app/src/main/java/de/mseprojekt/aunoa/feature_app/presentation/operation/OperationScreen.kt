package de.mseprojekt.aunoa.feature_app.presentation.operation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import de.mseprojekt.aunoa.feature_app.presentation.util.Screen
import de.mseprojekt.aunoa.feature_app.presentation.util.bottom_navigation_bar.BottomNavigationBar
import de.mseprojekt.aunoa.feature_app.presentation.util.card.AunoaCard
import de.mseprojekt.aunoa.feature_app.presentation.util.card.CardActionItem
import de.mseprojekt.aunoa.feature_app.presentation.util.top_app_bar.AunoaTopBar
import de.mseprojekt.aunoa.feature_app.presentation.util.top_app_bar.TopBarActionItem
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.*

@ExperimentalMaterialApi
@ExperimentalPermissionsApi
@Composable
fun ActivityScreen(
    navController: NavController,
    viewModel: OperationViewModel = hiltViewModel(),
    permissionsState: MultiplePermissionsState,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(
        key1 = lifecycleOwner,
        effect = {
            val observer = LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_START) {
                    permissionsState.launchMultiplePermissionRequest()
                }
            }
            lifecycleOwner.lifecycle.addObserver(observer)

            onDispose {
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        }
    )

    val state = viewModel.state.value
    val scaffoldState = rememberScaffoldState()
    val scrollState = rememberScrollState()
    var showSearch by remember { mutableStateOf(false) }
    val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())

    val actionItems = listOf<TopBarActionItem>(
        TopBarActionItem(
            "info",
            Icons.Filled.Info,
            {}),
        TopBarActionItem(
            "settings",
            Icons.Filled.Settings,
            {}),
        TopBarActionItem(
            "user",
            Icons.Filled.Person,
            {}),
    )
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { AunoaTopBar(actionItems) },
        content = {
            //Column() {
                LazyColumn() {
                    items(state.operations.asReversed()) { operation ->
                        AunoaCard(
                            navController = navController,
                            title = operation.operationId.toString(),
                            // TODO different time format
                            subtitle = sdf.format(Date(operation.date)),
                            actions = listOf(
                                CardActionItem(
                                    "Go to Rule",
                                    { navController.navigate(Screen.RulesHubScreen.route) })
                            )
                        )
                    }
                }
            //}
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screen.AddRuleScreen.route)
                }
            ) {
                Icon(Icons.Filled.Add, "Add Rule")
            }
        },
        bottomBar = { BottomNavigationBar(navController = navController) }
    )
}