package de.mseprojekt.aunoa.feature_app.presentation.rule_details

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.gson.Gson
import de.mseprojekt.aunoa.feature_app.domain.model.triggerObjects.LocationTrigger
import de.mseprojekt.aunoa.feature_app.domain.model.triggerObjects.TimeTrigger
import de.mseprojekt.aunoa.feature_app.presentation.edit_rule.EditRuleViewModel
import de.mseprojekt.aunoa.feature_app.presentation.util.Screen
import de.mseprojekt.aunoa.feature_app.presentation.util.bottom_navigation_bar.BottomNavigationBar
import de.mseprojekt.aunoa.feature_app.presentation.util.chip.AunoaChip
import de.mseprojekt.aunoa.feature_app.presentation.util.top_app_bar.AunoaTopBar
import de.mseprojekt.aunoa.feature_app.presentation.util.top_app_bar.TopBarActionItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest

@ExperimentalMaterialApi
@Composable
fun RuleDetailsScreen(
    navController: NavController,
    viewModel: RuleDetailsViewModel = hiltViewModel(),
) {
    val state = viewModel.state.value
    val scaffoldState = rememberScaffoldState()
    val scrollState = rememberScrollState()
    val actionItems = listOf<TopBarActionItem>(
        TopBarActionItem(
            "edit",
            Icons.Filled.Edit,
            { navController.navigate(Screen.EditRuleScreen.route + "?ruleId=${state.rule?.rule?.ruleId}") }),
        TopBarActionItem(
            "delete",
            Icons.Filled.Delete,
            { viewModel.onEvent(RuleDetailsEvent.DeleteRule) }),
    )

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is RuleDetailsViewModel.UiEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }
                is RuleDetailsViewModel.UiEvent.DeleteRule -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message
                    )
                    delay(300L)
                    navController.navigateUp()
                }
            }
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { AunoaTopBar(actionItems) },
        content = {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier
                    .verticalScroll(scrollState)
                    .padding(horizontal = 15.dp)
                    .padding(bottom = 75.dp)
            ) {
                if (state.rule !== null) {
                    Text(state.rule.rule.title, style = MaterialTheme.typography.h3)
                }
                Column() {
                    Text("Description", style = MaterialTheme.typography.h6)
                    if (state.rule !== null) {
                        Text(state.rule.rule.description, style = MaterialTheme.typography.body1)
                    }
                }
                Column() {
                    Text("Tags", style = MaterialTheme.typography.h6)
                    Row() {
                        state.tags.forEach { tag ->
                            AunoaChip(label = tag.title)
                            Spacer(modifier = Modifier.width(5.dp))
                        }
                    }
                }
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text("Trigger", style = MaterialTheme.typography.h6)
                    Spacer(modifier = Modifier.height(10.dp))
                    if (state.rule != null) {
                        val trigger = state.rule.content.trig
                        when (trigger.triggerType) {
                            "TimeTrigger" -> {
                                val timeTrigger =
                                    Gson().fromJson(trigger.triggerObject, TimeTrigger::class.java)
                                Column() {
                                    Text(text = "Type: Time trigger")
                                    Spacer(modifier = Modifier.height(5.dp))
                                    Text(text = "Value: Starts on ${timeTrigger.startWeekday} at ${timeTrigger.startTime}")
                                    Text(text = "Value: Ends on ${timeTrigger.endWeekday} at ${timeTrigger.endTime}")
                                }
                            }
                            "LocationTrigger" -> {
                                val locationTrigger = Gson().fromJson(
                                    trigger.triggerObject,
                                    LocationTrigger::class.java
                                )
                                Column() {
                                    Text(text = "Type: Location trigger")
                                    Spacer(modifier = Modifier.height(5.dp))
                                    Text(text = "Latitude: ${locationTrigger.latitude}")
                                    Text(text = "Longitude: ${locationTrigger.longitude}")
                                    Text(text = "Radius: ${locationTrigger.radius}")
                                }
                            }
                        }
                    }
                }
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text("Action", style = MaterialTheme.typography.h6)
                    Spacer(modifier = Modifier.height(10.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(.8f)
                            .align(Alignment.CenterHorizontally)
                            .height(80.dp)
                            .border(
                                BorderStroke(2.dp, MaterialTheme.colors.primary),
                                RoundedCornerShape(5)
                            )
                    ) {
                        TextButton(
                            onClick = { /*TODO*/ },
                            modifier = Modifier.align(Alignment.Center)
                        ) {
                            Text("Add Action")
                        }
                    }
                }
            }
        },
        bottomBar = { BottomNavigationBar(navController = navController) }
    )
}