package de.mseprojekt.aunoa.feature_app.presentation.util.top_app_bar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AunoaTopBar(actionItems: List<TopBarActionItem>) {
    Column() {
        Spacer(
            modifier = Modifier
                .fillMaxWidth(1f)
                .height(5.dp)
                .background(colors.primary)
        )
        Row(
            modifier = Modifier
                .height(80.dp)
                .background(colors.background)
                .fillMaxWidth(1f)
        ) {
            Box(
                Modifier
                    .background(colors.primary)
                    .padding(top = 15.dp, end = 30.dp, bottom = 15.dp, start = 30.dp)
            ) {
                Text(text = "AUNOA", color = Color.White, fontSize = 30.sp)
            }
            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxSize(1f)
                    .background(colors.background)
                    .padding(end = 10.dp)
            ) {
                for (actionItem in actionItems) {
                    IconButton(onClick = actionItem.onClick) {
                        Icon(
                            actionItem.icon,
                            contentDescription = actionItem.name,
                            tint = Color.Gray,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }
        }
    }

}



/*@Composable
fun AunoaTopBar() {
    Column() {
        Spacer(
            modifier = Modifier
                .fillMaxWidth(1f)
                .height(5.dp)
                .background(colors.primary)
        )
        Row(
            modifier = Modifier
                .height(80.dp)
                .background(Color.White)
                .fillMaxWidth(1f)
        ) {
            Box(
                Modifier
                    .background(colors.primary)
                    .padding(top = 15.dp, end = 30.dp, bottom = 15.dp, start = 30.dp)
            ) {
                Text(text = "AUNOA", color = Color.White, fontSize = 30.sp)
            }
            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxSize(1f)
                    .background(Color.White)
                    .padding(end = 10.dp)
            ) {
                IconButton(onClick = { *//*TODO*//* }) {
                    Icon(
                        Icons.Filled.Extension,
                        contentDescription = "Plugins",
                        tint = Color.Gray,
                        modifier = Modifier.size(28.dp)
                    )
                }
                IconButton(onClick = { *//*TODO*//* }) {
                    Icon(
                        Icons.Filled.Settings,
                        contentDescription = "Plugins",
                        tint = Color.Gray,
                        modifier = Modifier.size(28.dp)
                    )
                }
                IconButton(onClick = { *//*TODO*//* }) {
                    Icon(
                        Icons.Filled.Person,
                        contentDescription = "Plugins",
                        tint = Color.Gray,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
    }

}*/