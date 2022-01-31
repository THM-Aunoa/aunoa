package de.mseprojekt.aunoa.feature_app.presentation.edit_rule

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.gson.Gson
import de.mseprojekt.aunoa.feature_app.domain.model.triggerObjects.LocationTrigger
import de.mseprojekt.aunoa.feature_app.domain.model.triggerObjects.TimeTrigger
import de.mseprojekt.aunoa.feature_app.domain.model.triggerObjects.TriggerObject
import de.mseprojekt.aunoa.feature_app.presentation.util.bottom_navigation_bar.BottomNavigationBar
import de.mseprojekt.aunoa.feature_app.presentation.util.chip.AunoaChip
import de.mseprojekt.aunoa.feature_app.presentation.util.top_app_bar.AunoaTopBar
import de.mseprojekt.aunoa.feature_app.presentation.util.top_app_bar.TopBarActionItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlin.math.roundToInt

@ExperimentalMaterialApi
@Composable
fun EditRuleScreen(
    navController: NavController,
    viewModel: EditRuleViewModel = hiltViewModel()
) {

    var state = viewModel.state.value
    val scaffoldState = rememberScaffoldState()
    val scrollState = rememberScrollState()
    var openTagDialog = remember { mutableStateOf(false) }
    var openTriggerDialog by remember { mutableStateOf(false) }
    var newTagText by remember { mutableStateOf("") }

    var trigger by remember { mutableStateOf("") }
    val actionItems = listOf<TopBarActionItem>(
        /*TopBarActionItem("undo", Icons.Filled.Undo, {}),
        TopBarActionItem("redo", Icons.Filled.Redo, {}),*/
        TopBarActionItem(
            "delete",
            Icons.Filled.Delete,
            { viewModel.onEvent(EditRuleEvent.DeleteRule) }),
        TopBarActionItem("save", Icons.Filled.Save, { viewModel.onEvent(EditRuleEvent.SaveRule) }),
    )

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is EditRuleViewModel.UiEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }
                is EditRuleViewModel.UiEvent.SaveRule -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message
                    )
                    delay(300L)
                    navController.navigateUp()
                }
                is EditRuleViewModel.UiEvent.DeleteRule -> {
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
                if (state.ruleId != -1) {
                    Text("Edit Rule", style = MaterialTheme.typography.h3)
                } else {
                    Text("New Rule", style = MaterialTheme.typography.h3)
                }
                Column() {
                    Text("Name", style = MaterialTheme.typography.h6)
                    OutlinedTextField(
                        value = state.title,
                        onValueChange = { viewModel.onEvent(EditRuleEvent.EnteredTitle(it)) },
                        //label = { Text("Name") },
                        modifier = Modifier.fillMaxWidth(1f)
                    )
                }
                Column() {
                    Text("Description", style = MaterialTheme.typography.h6)
                    OutlinedTextField(
                        value = state.description,
                        onValueChange = { viewModel.onEvent(EditRuleEvent.EnteredDescription(it)) },
                        //label = { Text("Description") },
                        modifier = Modifier
                            .height(120.dp)
                            .fillMaxWidth(1f)
                    )
                }
                Column() {
                    Text("Priority", style = MaterialTheme.typography.h6)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = state.priority.toString())
                        Spacer(modifier = Modifier.width(10.dp))
                        Slider(
                            value = state.priority.toFloat(),
                            onValueChange = { viewModel.onEvent(EditRuleEvent.EnteredPriority(it.roundToInt())) },
                            valueRange = 0f..10f,
                            steps = 9,
                            colors = SliderDefaults.colors(
                                thumbColor = MaterialTheme.colors.primary,
                                activeTrackColor = MaterialTheme.colors.primary
                            )
                        )

                    }
                }
                Column() {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth(1f)
                    ) {
                        Text("Tags", style = MaterialTheme.typography.h6)
                        IconButton(onClick = { openTagDialog.value = true }) {
                            Icon(imageVector = Icons.Filled.Add, contentDescription = "Add Tag")
                        }
                    }
                    Row() {
                        AunoaChip(label = "WIFI", icon = Icons.Outlined.Close)
                        Spacer(modifier = Modifier.width(4.dp))
                        AunoaChip(label = "SOUND")
                    }
                }
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text("Trigger", style = MaterialTheme.typography.h6)
                    Spacer(modifier = Modifier.height(10.dp))
                    when (state.triggerObjectName) {
                        "TimeTrigger" -> {
                        }
                        "LocationTrigger" -> {
                            LocationTriggerEdit()
                        }
                        "" -> {
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
                                    onClick = { openTriggerDialog = true },
                                    modifier = Modifier.align(Alignment.Center)
                                ) {
                                    Text("Add Trigger")
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

    if (openTriggerDialog) {
        AlertDialog(
            onDismissRequest = {
                openTagDialog.value = false
            },
            title = {
                Text(text = "Please choose a Trigger type", style = MaterialTheme.typography.h6)
            },
            text = {
                val myData = listOf(
                    Triple("TimeTrigger", "Using day and time", Icons.Filled.AccessTime),
                    Triple("LocationTrigger", "Using coordinates", Icons.Filled.MyLocation)
                )
                LazyColumn {
                    items(myData) { item ->
                        ListItem(
                            text = { Text(item.first) },
                            secondaryText = { Text(item.second) },
                            trailing = {
                                Icon(
                                    imageVector = item.third,
                                    contentDescription = ""
                                )
                            },
                            modifier = Modifier.clickable {
                                viewModel.onEvent(
                                    EditRuleEvent.ChoosedTrigger(item.first)
                                )
                                openTriggerDialog = false
                            })

                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(
                    onClick = {
                        openTriggerDialog = false
                    }) {
                    Text("Cancel")
                }
            }
        )
    }

    if (openTagDialog.value) {
        AlertDialog(
            onDismissRequest = {
                openTagDialog.value = false
            },
            title = {
                Text(text = "Please enter a Tag name", style = MaterialTheme.typography.h6)
            },
            text = {
                Column() {
                    TextField(
                        value = newTagText,
                        onValueChange = { newTagText = it }
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        openTagDialog.value = false
                    }) {
                    Text("Add Tag")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        openTagDialog.value = false
                    }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun LocationTriggerEdit(
    viewModel: EditRuleViewModel = hiltViewModel()
) {
    var latitude by remember { mutableStateOf("") }
    var longitude by remember { mutableStateOf("") }
    val state: TriggerObject = viewModel.state.value.trigger
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                BorderStroke(1.dp, Color.LightGray),
                RoundedCornerShape(5)
            )
            .padding(horizontal = 20.dp)
            .padding(bottom = 20.dp, top = 5.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Location Trigger", style = MaterialTheme.typography.h6)
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Remove Trigger",
                    tint = Color.Gray,
                )
            }
        }
        //Text("Latitude", style = MaterialTheme.typography.h6)
        /*OutlinedTextField(
            value = ,
            onValueChange = { latitude = it },
            label = { Text("Latitude") },
            modifier = Modifier
                .fillMaxWidth(1f)
        )*/
        Spacer(modifier = Modifier.height(10.dp))
        //Text("Longitude", style = MaterialTheme.typography.h6)
        OutlinedTextField(
            value = longitude,
            onValueChange = { longitude = it },
            label = { Text("Longitude") },
            modifier = Modifier
                .fillMaxWidth(1f)
        )
    }
}
