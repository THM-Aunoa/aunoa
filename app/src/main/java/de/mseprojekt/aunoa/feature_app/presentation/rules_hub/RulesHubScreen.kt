package de.mseprojekt.aunoa.feature_app.presentation.rules_hub

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import de.mseprojekt.aunoa.feature_app.presentation.operation.OperationViewModel
import de.mseprojekt.aunoa.feature_app.presentation.util.Screen
import de.mseprojekt.aunoa.feature_app.presentation.util.bottom_navigation_bar.BottomNavigationBar
import de.mseprojekt.aunoa.feature_app.presentation.util.card.AunoaCard
import de.mseprojekt.aunoa.feature_app.presentation.util.card.CardActionItem
import de.mseprojekt.aunoa.feature_app.presentation.util.top_app_bar.AunoaTopBar
import de.mseprojekt.aunoa.feature_app.presentation.util.top_app_bar.TopBarActionItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest

@ExperimentalMaterialApi
@Composable
fun RulesHubScreen(
    navController: NavController,
    viewModel: RulesHubViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val scaffoldState = rememberScaffoldState()
    val scrollState = rememberScrollState()
    var showSearch by remember { mutableStateOf(false) }
    val actionItems = listOf(
        TopBarActionItem(
            "search",
            Icons.Filled.Search,
            { showSearch = !showSearch })
    ,
    TopBarActionItem(
        "filter",
        Icons.Filled.Block,
        { viewModel.onEvent(RulesHubEvent.ResetFilter) }))

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is RulesHubViewModel.UiEvent.AddRule -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message
                    )
                    delay(100)
                    navController.navigate(Screen.EditRuleScreen.route + "?ruleId=${event.ruleId}")
                }
            }
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { AunoaTopBar(actionItems) },
        content = {
            Column() {
                if (showSearch) {
                    OutlinedTextField(
                        value = state.searchText,
                        onValueChange = { value ->
                            println(value)
                            viewModel.onEvent(RulesHubEvent.SearchRules(value))
                        },
                        label = { Text("Search Rules") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 15.dp)
                    )
                }

                Column(modifier = Modifier
                    .verticalScroll(scrollState)
                    .padding(bottom = 75.dp)) {
                    state.rules.forEach { rule ->
                        AunoaCard(
                            title = rule.rule.title,
                            content = rule.rule.description,
                            tags = rule.tags,
                            onClickTag = { tag -> viewModel.onEvent(RulesHubEvent.FilterRules(tag)) },
                            actions = listOf(CardActionItem("Add Rule", { viewModel.onEvent(RulesHubEvent.AddRule(rule))})),
                            //iconAction = CardIconAction(Icons.Filled.Add, {}, "Add Rule"),
                            navController = navController,
                            viewModel = viewModel
                        )
                    }
                }
            }
        },
        bottomBar = { BottomNavigationBar(navController = navController) }
    )
}