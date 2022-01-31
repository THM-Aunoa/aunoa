package de.mseprojekt.aunoa.feature_app.presentation.edit_rule

import android.app.TimePickerDialog
import android.content.Context
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
import de.mseprojekt.aunoa.feature_app.presentation.util.bottom_navigation_bar.BottomNavigationBar
import de.mseprojekt.aunoa.feature_app.presentation.util.chip.AunoaChip
import de.mseprojekt.aunoa.feature_app.presentation.util.spinner.Spinner
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
    var openActionDialog by remember { mutableStateOf(false) }
    var newTagText by remember { mutableStateOf("") }

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
                        state.tags.forEach { tag ->
                            AunoaChip(
                                label = tag.title,
                                icon = Icons.Outlined.Close,
                                onClick = { viewModel.onEvent(EditRuleEvent.RemoveTag(tag.tagId!!)) })
                            Spacer(modifier = Modifier.width(4.dp))
                        }
                    }
                }
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text("Trigger", style = MaterialTheme.typography.h6)
                    Spacer(modifier = Modifier.height(10.dp))
                    when (state.triggerObjectName) {
                        "TimeTrigger" -> {
                            TimeTriggerEdit()
                        }
                        "CellTrigger" -> {
                            CellTriggerEdit()
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
                    when (state.actionObjectName) {
                        "VolumeAction" -> {
                            VolumeActionEdit()
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
                                    onClick = { openActionDialog = true },
                                    modifier = Modifier.align(Alignment.Center)
                                ) {
                                    Text("Add Action")
                                }
                            }
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
                openTriggerDialog = false
            },
            title = {
                Text(text = "Please choose a Trigger type", style = MaterialTheme.typography.h6)
            },
            text = {
                val myData = listOf(
                    Triple("TimeTrigger", "Using day and time", Icons.Filled.AccessTime),
                    Triple("LocationTrigger", "Using coordinates", Icons.Filled.MyLocation),
                    Triple("CellTrigger", "Using your defined locations", Icons.Filled.NetworkCell)
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

    if (openActionDialog) {
        AlertDialog(
            onDismissRequest = {
                openActionDialog = false
            },
            title = {
                Text(text = "Please choose an Action type", style = MaterialTheme.typography.h6)
            },
            text = {
                val myData = listOf(
                    Triple("VolumeAction", "Mute and unmute your phone", Icons.Filled.RingVolume)
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
                                    EditRuleEvent.ChoosedAction(item.first)
                                )
                                openActionDialog = false
                            })

                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(
                    onClick = {
                        openActionDialog = false
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
                        viewModel.onEvent(EditRuleEvent.AddTag(newTagText))
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

@ExperimentalMaterialApi
@Composable
fun LocationTriggerEdit(
    viewModel: EditRuleViewModel = hiltViewModel()
) {
    val state = viewModel.state.value.locationTrigger
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
            Row() {
                IconButton(onClick = { viewModel.onEvent(EditRuleEvent.FillCurrentLocationBoxes) }) {
                    Icon(
                        imageVector = Icons.Filled.MyLocation,
                        contentDescription = "Get your location",
                        tint = Color.Gray,
                    )
                }
                IconButton(onClick = { viewModel.onEvent(EditRuleEvent.RemoveTrigger) }) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Remove Trigger",
                        tint = Color.Gray,
                    )
                }
            }
        }
        OutlinedTextField(
            value = state.latitude.toString(),
            onValueChange = { viewModel.onEvent(EditRuleEvent.EnteredLatitude(it)) },
            label = { Text("Latitude") },
            modifier = Modifier
                .fillMaxWidth(1f)
        )
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = state.longitude.toString(),
            onValueChange = { viewModel.onEvent(EditRuleEvent.EnteredLatitude(it)) },
            label = { Text("Longitude") },
            modifier = Modifier
                .fillMaxWidth(1f)
        )
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = state.radius.toString(),
            onValueChange = { viewModel.onEvent(EditRuleEvent.EnteredRadius(it.toInt())) },
            label = { Text("Radius") },
            modifier = Modifier
                .fillMaxWidth(1f)
        )
    }
}

@ExperimentalMaterialApi
@Composable
fun TimeTriggerEdit(
    viewModel: EditRuleViewModel = hiltViewModel()
) {
    val triggerState = viewModel.state.value.timeTrigger
    val state = viewModel.state.value

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
            Text(text = "Time Trigger", style = MaterialTheme.typography.h6)
            IconButton(onClick = { viewModel.onEvent(EditRuleEvent.RemoveTrigger) }) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Remove Trigger",
                    tint = Color.Gray,
                )
            }
        }
        Text(text = "Start")
        Spinner(
            label = "Weekday",
            options = listOf(
                "MONDAY",
                "TUESDAY",
                "WEDNESDAY",
                "THURSDAY",
                "FRIDAY",
                "SATURDAY",
                "SUNDAY"
            ),
            selected = triggerState.startWeekday.toString(),
            callback = { value -> viewModel.onEvent(EditRuleEvent.EnteredStartDay(value)) })
        Spinner(
            label = "Hour",
            options = (0..23).map { it.toString() },
            selected = state.startTimeHour.toString(),
            callback = { value -> viewModel.onEvent(EditRuleEvent.EnteredStartTimeHour(value)) }
        )
        Spinner(
            label = "Minutes",
            options = (0..59).map { it.toString() },
            selected = state.startTimeMinutes.toString(),
            callback = { value -> viewModel.onEvent(EditRuleEvent.EnteredStartTimeMinutes(value)) }
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = "End")
        Spinner(
            label = "Weekday",
            options = listOf(
                "MONDAY",
                "TUESDAY",
                "WEDNESDAY",
                "THURSDAY",
                "FRIDAY",
                "SATURDAY",
                "SUNDAY"
            ),
            selected = triggerState.endWeekday.toString(),
            callback = { value -> viewModel.onEvent(EditRuleEvent.EnteredEndDay(value)) })
        Spinner(
            label = "Hour",
            options = (0..23).map { it.toString() },
            selected = state.endTimeHour.toString(),
            callback = { value -> viewModel.onEvent(EditRuleEvent.EnteredEndTimeHour(value)) }
        )
        Spinner(
            label = "Minutes",
            options = (0..59).map { it.toString() },
            selected = state.endTimeMinutes.toString(),
            callback = { value -> viewModel.onEvent(EditRuleEvent.EnteredEndTimeMinutes(value)) }
        )
    }
}

@ExperimentalMaterialApi
@Composable
fun CellTriggerEdit(
    viewModel: EditRuleViewModel = hiltViewModel()
) {
    val state = viewModel.state.value.cellTrigger
    val regions = viewModel.state.value.regions
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
            Text(text = "Cell Trigger", style = MaterialTheme.typography.h6)
            IconButton(onClick = { viewModel.onEvent(EditRuleEvent.RemoveTrigger) }) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Remove Trigger",
                    tint = Color.Gray,
                )
            }
        }
        Column() {
            regions.forEach { region ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = region.name == state.name,
                        onClick = { viewModel.onEvent(EditRuleEvent.ChoosedRegion(region.name)) },
                        colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colors.primary),
                        modifier = Modifier.padding(end = 5.dp)
                    )
                    Text(text = region.name)
                }
                Spacer(modifier = Modifier.height(5.dp))
            }
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun VolumeActionEdit(
    viewModel: EditRuleViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
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
            Text(text = "Volume Action", style = MaterialTheme.typography.h6)
            IconButton(onClick = { viewModel.onEvent(EditRuleEvent.RemoveAction) }) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Remove Action",
                    tint = Color.Gray,
                )
            }
        }
        Row() {
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = state.volumeActionMute,
                    onClick = { viewModel.onEvent(EditRuleEvent.ToggleVolumeActionMute(true)) },
                    colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colors.primary),
                    modifier = Modifier.padding(end = 5.dp)
                )
                Text(text = "Mute")
            }
            Spacer(modifier = Modifier.width(10.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = !state.volumeActionMute,
                    onClick = { viewModel.onEvent(EditRuleEvent.ToggleVolumeActionMute(false)) },
                    colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colors.primary),
                    modifier = Modifier.padding(end = 5.dp)
                )
                Text(text = "Unmute")
            }
            Spacer(modifier = Modifier.height(5.dp))
        }
    }
}