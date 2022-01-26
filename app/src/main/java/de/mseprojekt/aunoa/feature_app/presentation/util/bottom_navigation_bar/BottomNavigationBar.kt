package de.mseprojekt.aunoa.feature_app.presentation.util.bottom_navigation_bar

import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavigationBar(navController: NavController) {

    BottomNavigation(

        // set background color
        backgroundColor = colors.primary
    ) {

        // observe the backstack
        val navBackStackEntry by navController.currentBackStackEntryAsState()

        // observe current route to change the icon
        // color,label color when navigated
        val currentRoute = navBackStackEntry?.destination?.route

        val navItems = listOf<BottomNavItem>(
            BottomNavItem.YourRules,
            BottomNavItem.Activity,
            BottomNavItem.RulesHub
        )

        // Bottom nav items we declared
        navItems.forEach { navItem ->

            // Place the bottom nav items
            BottomNavigationItem(

                // it currentRoute is equal then its selected route
                selected = currentRoute == navItem.route,

                // navigate on click
                onClick = {
                    navController.navigate(navItem.route)
                },

                // Icon of navItem
                icon = {
                    Icon(navItem.icon, contentDescription = navItem.label)
                },

                // label
                label = {
                    Text(text = navItem.label, fontSize = 16.sp, modifier = Modifier.padding(top = 5.dp))
                },
            )
        }
    }
}