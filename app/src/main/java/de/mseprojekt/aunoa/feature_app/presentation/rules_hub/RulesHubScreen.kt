package de.mseprojekt.aunoa.feature_app.presentation.rules_hub

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import de.mseprojekt.aunoa.feature_app.presentation.actvity.ActivityViewModel

@Composable
fun RulesHubScreen(
    navController: NavController,
    viewModel: RulesHubViewModel = hiltViewModel()
) {}