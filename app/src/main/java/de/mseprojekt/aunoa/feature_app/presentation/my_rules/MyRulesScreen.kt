package de.mseprojekt.aunoa.feature_app.presentation.my_rules

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import de.mseprojekt.aunoa.feature_app.presentation.operation.BottomSheetScreen
import de.mseprojekt.aunoa.feature_app.presentation.rule_details.RuleDetailsViewModel
import de.mseprojekt.aunoa.feature_app.presentation.rules_hub.RulesHubEvent
import de.mseprojekt.aunoa.feature_app.presentation.util.Screen
import de.mseprojekt.aunoa.feature_app.presentation.util.bottom_navigation_bar.BottomNavigationBar
import de.mseprojekt.aunoa.feature_app.presentation.util.card.AunoaCard
import de.mseprojekt.aunoa.feature_app.presentation.util.card.CardActionItem
import de.mseprojekt.aunoa.feature_app.presentation.util.card.CardIconAction
import de.mseprojekt.aunoa.feature_app.presentation.util.top_app_bar.AunoaTopBar
import de.mseprojekt.aunoa.feature_app.presentation.util.top_app_bar.TopBarActionItem
import kotlinx.coroutines.flow.collectLatest

@ExperimentalMaterialApi
@Composable
fun MyRulesScreen(
    navController: NavController,
    viewModel: MyRulesViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val scaffoldState = rememberScaffoldState()
    val scrollState = rememberScrollState()
    var showSearch by remember { mutableStateOf(false) }
    val actionItems = listOf<TopBarActionItem>(
        TopBarActionItem(
            "search",
            Icons.Filled.Search,
            { showSearch = !showSearch }),
        TopBarActionItem(
            "filter",
            Icons.Filled.Block,
            { viewModel.onEvent(MyRulesEvent.ResetFilter) })
    )

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is MyRulesViewModel.UiEvent.DeleteRule -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message
                    )
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
                            viewModel.onEvent(RulesHubEvent.SearchRules(value))
                        },
                        label = { Text("Search Rules") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 15.dp)
                    )
                }
                if (state.rules.isEmpty()) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Button(onClick = { navController.navigate(Screen.EditRuleScreen.route) }) {
                            Text(text = "Create first rule")
                        }
                    }
                } else {
                    Column(
                        modifier = Modifier
                            .verticalScroll(scrollState)
                            .padding(bottom = 75.dp)
                    ) {
                        state.rules.forEach { rule ->
                            AunoaCard(
                                title = rule.rule.title,
                                content = rule.rule.description,
                                tags = rule.tags,
                                actions = listOf(
                                    CardActionItem(
                                        "Edit Rule",
                                        { navController.navigate(Screen.EditRuleScreen.route + "?ruleId=${rule.rule.ruleId}") })
                                ),
                                navController = navController,
                                viewModel = viewModel,
                                onClickTag = { tag -> viewModel.onEvent(MyRulesEvent.FilterRules(tag)) },
                                onClickCard = { navController.navigate(Screen.RulesDetailsScreen.route + "?ruleId=${rule.rule.ruleId}") },
                                iconAction = CardIconAction(
                                    Icons.Filled.Delete,
                                    { viewModel.onEvent(MyRulesEvent.DeleteRule(rule.rule.ruleId!!)) },
                                    "Delete Rule"
                                )
                            )
                        }
                    }
                }
            }
        },
        floatingActionButton = {
            val backgroundColor: Color
            if (isSystemInDarkTheme()) {
                backgroundColor = colors.primary
            } else {
                backgroundColor = colors.secondary
            }
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screen.EditRuleScreen.route)
                },
                backgroundColor = backgroundColor
            ) {
                Icon(Icons.Filled.Add, "Add Rule")
            }
        },
        bottomBar = { BottomNavigationBar(navController = navController) }
    )
}