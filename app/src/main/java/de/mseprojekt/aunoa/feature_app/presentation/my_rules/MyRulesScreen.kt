package de.mseprojekt.aunoa.feature_app.presentation.my_rules

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import de.mseprojekt.aunoa.feature_app.presentation.actvity.ActivityViewModel

@Composable
fun MyRulesScreen(
    navController: NavController,
    viewModel: MyRulesViewModel = hiltViewModel()
) {}