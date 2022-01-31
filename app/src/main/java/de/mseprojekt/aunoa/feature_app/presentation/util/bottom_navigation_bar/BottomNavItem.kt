package de.mseprojekt.aunoa.feature_app.presentation.util.bottom_navigation_bar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import de.mseprojekt.aunoa.feature_app.presentation.util.Screen

sealed class BottomNavItem(var label:String, var icon:ImageVector, var route:String){

    object Activity : BottomNavItem("Operations", Icons.Filled.Update, Screen.OperationScreen.route)
    object YourRules: BottomNavItem("Your Rules", Icons.Filled.VideoLibrary, Screen.MyRulesScreen.route)
    object RulesHub: BottomNavItem("Rules Hub", Icons.Filled.Apps, Screen.RulesHubScreen.route)
}