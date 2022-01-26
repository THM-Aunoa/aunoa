package de.mseprojekt.aunoa.feature_app.presentation.util.card

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import de.mseprojekt.aunoa.feature_app.domain.model.Tag
import de.mseprojekt.aunoa.feature_app.presentation.util.Screen
import de.mseprojekt.aunoa.feature_app.presentation.util.chip.AunoaChip

@ExperimentalMaterialApi
@Composable
fun AunoaCard(
    navController: NavController,
    title: String,
    subtitle: String,
    content: String = "",
    actions: List<CardActionItem> = emptyList(),
    tags: List<Tag> = emptyList()
) {
    println(tags)
    Card(
        elevation = 12.dp,
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row() {
                Column(modifier = Modifier.weight(1f)) {
                    Text(title, style = MaterialTheme.typography.h5)
                    Text(subtitle, style = MaterialTheme.typography.h6)
                }
                Icon(Icons.Filled.Share, contentDescription = "Share")
            }
            if (content != "") {
                Text(
                    content,
                    modifier = Modifier.padding(vertical = 10.dp)
                )
            }
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row() {
                    tags.forEach { tag ->
                        AunoaChip(
                            label = tag.title,
                            onClick = { navController.navigate(Screen.OperationScreen.route) })
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                    AunoaChip(
                        label = "WIFI",
                        onClick = { navController.navigate(Screen.OperationScreen.route) })
                    Spacer(modifier = Modifier.width(4.dp))
                    AunoaChip(label = "SOUND")
                }
                Row() {
                    actions.forEach { action ->
                        TextButton(onClick = action.onClick) {
                            Text(action.label)
                        }
                    }
                }
            }
        }
    }
}