package de.mseprojekt.aunoa.feature_app.presentation.operation

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import de.mseprojekt.aunoa.feature_app.presentation.util.Screen
import de.mseprojekt.aunoa.feature_app.presentation.util.bottom_navigation_bar.BottomNavigationBar
import de.mseprojekt.aunoa.feature_app.presentation.util.card.AunoaCard
import de.mseprojekt.aunoa.feature_app.presentation.util.card.CardActionItem
import de.mseprojekt.aunoa.feature_app.presentation.util.top_app_bar.AunoaTopBar
import de.mseprojekt.aunoa.feature_app.presentation.util.top_app_bar.TopBarActionItem
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*

@ExperimentalMaterialApi
@ExperimentalPermissionsApi
@Composable
fun ActivityScreen(
    navController: NavController,
    viewModel: OperationViewModel = hiltViewModel(),
) {

    val state = viewModel.state.value
    val scaffoldState = rememberScaffoldState()
    val scrollState = rememberScrollState()
    var showSearch by remember { mutableStateOf(false) }
    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
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
                            subtitle = LocalDateTime.ofEpochSecond(operation.date, 0, ZoneOffset.UTC).format(formatter),
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