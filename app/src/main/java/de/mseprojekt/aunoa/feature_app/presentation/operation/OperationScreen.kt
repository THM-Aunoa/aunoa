package de.mseprojekt.aunoa.feature_app.presentation.operation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import de.mseprojekt.aunoa.R
import de.mseprojekt.aunoa.feature_app.presentation.util.Screen
import de.mseprojekt.aunoa.feature_app.presentation.util.bottom_navigation_bar.BottomNavigationBar
import de.mseprojekt.aunoa.feature_app.presentation.util.card.AunoaCard
import de.mseprojekt.aunoa.feature_app.presentation.util.card.CardActionItem
import de.mseprojekt.aunoa.feature_app.presentation.util.top_app_bar.AunoaTopBar
import de.mseprojekt.aunoa.feature_app.presentation.util.top_app_bar.TopBarActionItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@ExperimentalMaterialApi
@ExperimentalPermissionsApi
@Composable
fun ActivityScreen(
    navController: NavController,
    viewModel: OperationViewModel = hiltViewModel(),
) {
    val scope = rememberCoroutineScope()
    val bottomScaffoldState = rememberBottomSheetScaffoldState()
    var currentBottomSheet: BottomSheetScreen? by remember {
        mutableStateOf(null)
    }

    if (bottomScaffoldState.bottomSheetState.isCollapsed)
        currentBottomSheet = null

    // to set the current sheet to null when the bottom sheet closes
    if (bottomScaffoldState.bottomSheetState.isCollapsed)
        currentBottomSheet = null


    val closeSheet: () -> Unit = {
        scope.launch {
            bottomScaffoldState.bottomSheetState.collapse()

        }
    }


    val openSheet: (BottomSheetScreen) -> Unit = {
        scope.launch {
            currentBottomSheet = it
            bottomScaffoldState.bottomSheetState.expand()
        }

    }


    val state = viewModel.state.value
    val scaffoldState = rememberScaffoldState()
    val scrollState = rememberScrollState()
    var showSearch by remember { mutableStateOf(false) }
    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
    val actionItems = listOf(
        TopBarActionItem(
            "info",
            Icons.Filled.Info,
            { openSheet(BottomSheetScreen.InfoScreen) }),
        TopBarActionItem(
            "settings",
            Icons.Filled.Settings,
            { openSheet(BottomSheetScreen.SettingsScreen) }),
        TopBarActionItem(
            "user",
            Icons.Filled.Person,
            { openSheet(BottomSheetScreen.UserScreen) }),
    )

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is OperationViewModel.UiEvent.SaveUser -> {
                    closeSheet()
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message
                    )
                    delay(300)
                }
            }
        }
    }

    BottomSheetScaffold(sheetPeekHeight = 0.dp, scaffoldState = bottomScaffoldState,
        sheetShape = BottomSheetShape,
        sheetContent = {
            currentBottomSheet?.let { currentSheet ->
                SheetLayout(currentSheet, closeSheet)
            }
        }) {
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
                            subtitle = LocalDateTime.ofEpochSecond(
                                operation.date,
                                0,
                                ZoneOffset.UTC
                            ).format(formatter),
                            actions = listOf(
                                CardActionItem(
                                    "Go to Rule",
                                    { navController.navigate(Screen.RulesHubScreen.route) })
                            ),
                            onClickTag = { println("TAAAAG") },
                            viewModel = viewModel
                        )
                    }
                }
                //}
            },
            bottomBar = { BottomNavigationBar(navController = navController) }
        )
    }
}

@ExperimentalMaterialApi
@Composable
fun SheetLayout(currentScreen: BottomSheetScreen, onCloseBottomSheet: () -> Unit) {
    BottomSheetWithCloseDialog(onCloseBottomSheet) {
        when (currentScreen) {
            BottomSheetScreen.InfoScreen -> InfoScreen()
            BottomSheetScreen.UserScreen -> UserScreen()
            BottomSheetScreen.SettingsScreen -> SettingsScreen()
        }

    }
}

@Composable
fun BottomSheetWithCloseDialog(
    onClosePressed: () -> Unit,
    modifier: Modifier = Modifier,
    closeButtonColor: Color = Color.Gray,
    content: @Composable() () -> Unit
) {
    Box(modifier.fillMaxWidth()) {
        content()

        IconButton(
            onClick = onClosePressed,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
                .size(29.dp)

        ) {
            Icon(Icons.Filled.Close, tint = closeButtonColor, contentDescription = null)
        }

    }
}

sealed class BottomSheetScreen() {
    object InfoScreen : BottomSheetScreen()
    object SettingsScreen : BottomSheetScreen()
    object UserScreen : BottomSheetScreen()
}

@Composable
fun InfoScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White, shape = RectangleShape)
    ) {
        Column(modifier = Modifier.padding(all = 15.dp)) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.icon),
                    contentDescription = "",
                    modifier = Modifier.padding(20.dp),
                    alignment = Alignment.Center
                )
                Text(text = "Information", style = MaterialTheme.typography.h3)
            }
            Spacer(modifier = Modifier.height(25.dp))
            Text(
                text = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren",
                style = MaterialTheme.typography.body1
            )
        }
    }
}

@Composable
fun UserScreen(
    viewModel: OperationViewModel = hiltViewModel(),
) {
    val scrollState = rememberScrollState()
    val username = viewModel.state.value.username
    val email = viewModel.state.value.email
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White, shape = RectangleShape)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier
                .verticalScroll(scrollState)
                .padding(horizontal = 15.dp)
                .padding(top = 25.dp)
        ) {
            Text("User settings", style = MaterialTheme.typography.h4)
            Spacer(modifier = Modifier.height(10.dp))
            Column() {
                Text("Username", style = MaterialTheme.typography.h6)
                OutlinedTextField(
                    value = username,
                    onValueChange = { viewModel.onEvent(OperationEvent.EnteredUsername(it)) },
                    label = { Text("Username") },
                    modifier = Modifier
                        .fillMaxWidth(1f)
                )
            }
            Column() {
                Text("E-Mail", style = MaterialTheme.typography.h6)
                OutlinedTextField(
                    value = email,
                    onValueChange = { viewModel.onEvent(OperationEvent.EnteredEmail(it)) },
                    label = { Text("E-Mail") },
                    modifier = Modifier
                        .fillMaxWidth(1f)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                modifier = Modifier.align(Alignment.End),
                onClick = { viewModel.onEvent(OperationEvent.SaveUserDetails) }) {
                Text(text = "Save")
            }
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun SettingsScreen(
    viewModel: OperationViewModel = hiltViewModel(),
) {
    val scrollState = rememberScrollState()
    var openRegionDialog = remember { mutableStateOf(false) }
    var newRegionText by remember { mutableStateOf("") }
    var newRegionTime by remember { mutableStateOf("10") }
    val appState = viewModel.state.value.appState
    val regions = viewModel.state.value.regions
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White, shape = RectangleShape)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier
                .verticalScroll(scrollState)
                .padding(horizontal = 15.dp)
                .padding(top = 25.dp)
        ) {
            Text("App settings", style = MaterialTheme.typography.h4)
            Spacer(modifier = Modifier.height(10.dp))
            Column() {
                Text(
                    "App status",
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.padding(bottom = 10.dp)
                )
                Row {
                    Row {
                        RadioButton(
                            selected = appState,
                            onClick = { viewModel.onEvent(OperationEvent.ToggleAppState(true)) },
                            enabled = true,
                            colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colors.primary)
                        )
                        Text(text = "Active", modifier = Modifier.padding(start = 8.dp))
                    }
                    Spacer(modifier = Modifier.width(15.dp))
                    Row {
                        RadioButton(
                            selected = !appState,
                            onClick = { viewModel.onEvent(OperationEvent.ToggleAppState(false)) },
                            enabled = true,
                            colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colors.primary)
                        )
                        Text(text = "Disabled", modifier = Modifier.padding(start = 8.dp))
                    }
                }
            }
            Column() {
                Text("Regions", style = MaterialTheme.typography.h6)
                Column() {
                    regions.forEach { region ->
                        ListItem(text = { Text(text = region.name)}, trailing = {IconButton(
                            onClick = { viewModel.onEvent(OperationEvent.DeleteRegion(region.regionId!!)) },
                        ){
                            Icon(
                                Icons.Filled.Delete,
                                contentDescription = "Delete Region",
                                tint = Color.Red
                            )
                        }})
                    }
                }
                Spacer(modifier = Modifier.height(5.dp))
                Button(onClick = { openRegionDialog.value = true }) {
                    Text(text = "Add region")
                }
            }
        }
    }

    if (openRegionDialog.value) {
        AlertDialog(
            onDismissRequest = {
                openRegionDialog.value = false
            },
            text = {
                Column() {
                    Text(text = "Please enter a Region name")
                    TextField(
                        value = newRegionText,
                        onValueChange = { newRegionText = it }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = "Please choose the scanning time in min")
                    TextField(value = newRegionTime, onValueChange = {})
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.onEvent(OperationEvent.AddRegion(newRegionText))
                        openRegionDialog.value = false
                    }) {
                    Text("Add Region")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        openRegionDialog.value = false
                    }) {
                    Text("Cancel")
                }
            }
        )
    }
}


val BottomSheetShape = RoundedCornerShape(
    topStart = 20.dp,
    topEnd = 20.dp,
    bottomEnd = 0.dp,
    bottomStart = 0.dp
)
