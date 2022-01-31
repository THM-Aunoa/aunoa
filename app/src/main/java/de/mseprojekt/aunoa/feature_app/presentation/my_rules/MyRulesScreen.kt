package de.mseprojekt.aunoa.feature_app.presentation.my_rules

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import de.mseprojekt.aunoa.feature_app.presentation.rules_hub.RulesHubEvent
import de.mseprojekt.aunoa.feature_app.presentation.util.Screen
import de.mseprojekt.aunoa.feature_app.presentation.util.bottom_navigation_bar.BottomNavigationBar
import de.mseprojekt.aunoa.feature_app.presentation.util.card.AunoaCard
import de.mseprojekt.aunoa.feature_app.presentation.util.card.CardActionItem
import de.mseprojekt.aunoa.feature_app.presentation.util.top_app_bar.AunoaTopBar
import de.mseprojekt.aunoa.feature_app.presentation.util.top_app_bar.TopBarActionItem

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
            { showSearch = !showSearch })
    )
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

                Column(modifier = Modifier.verticalScroll(scrollState).padding(bottom = 75.dp)) {
                    state.rules.forEach { rule ->
                        AunoaCard(
                            title = rule.rule.title,
                            content = rule.rule.description,
                            tags = rule.tags,
                            actions = listOf(CardActionItem("Edit Rule", {navController.navigate(Screen.EditRuleScreen.route + "?ruleId=${rule.rule.ruleId}")})),
                            navController = navController,
                            viewModel = viewModel,
                            onClickTag = { println("TAAAAG")},
                            onClickCard = { navController.navigate(Screen.RulesDetailsScreen.route + "?ruleId=${rule.rule.ruleId}") }
                            )
                    }
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screen.EditRuleScreen.route)
                }
            ) {
                Icon(Icons.Filled.Add, "Add Rule")
            }
        },
        bottomBar = { BottomNavigationBar(navController = navController) }
    )
}