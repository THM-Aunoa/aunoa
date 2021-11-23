package de.mseprojekt.aunoa.feature_app.presentation.rule_details

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import de.mseprojekt.aunoa.feature_app.presentation.actvity.ActivityViewModel

@Composable
fun RuleDetailsScreen(
    navController: NavController,
    viewModel: RuleDetailsViewModel = hiltViewModel()
) {}