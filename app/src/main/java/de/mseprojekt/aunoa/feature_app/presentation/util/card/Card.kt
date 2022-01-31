package de.mseprojekt.aunoa.feature_app.presentation.util.card

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Wash
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import de.mseprojekt.aunoa.feature_app.domain.model.Tag
import de.mseprojekt.aunoa.feature_app.presentation.rules_hub.RulesHubEvent
import de.mseprojekt.aunoa.feature_app.presentation.rules_hub.RulesHubViewModel
import de.mseprojekt.aunoa.feature_app.presentation.util.Screen
import de.mseprojekt.aunoa.feature_app.presentation.util.chip.AunoaChip
import de.mseprojekt.aunoa.other.AunoaViewModelInterface

@ExperimentalMaterialApi
@Composable
fun AunoaCard(
    navController: NavController,
    viewModel: AunoaViewModelInterface,
    title: String,
    subtitle: String = "",
    content: String = "",
    iconAction: CardIconAction? = null,
    actions: List<CardActionItem> = emptyList(),
    tags: List<Tag> = emptyList(),
    onClickTag: (Tag) -> Unit,
    onClickCard: () -> Unit = {},
    topRight: (@Composable () -> Unit)? = null
) {
    Card(
        elevation = 12.dp,
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
            .clickable { onClickCard() }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(title, style = MaterialTheme.typography.h5)
                    if (subtitle != "") {
                        Text(subtitle, style = MaterialTheme.typography.h6)
                    }
                }
                if (topRight != null) {
                    topRight()
                }
                if (iconAction != null) {
                    IconButton(
                        onClick = iconAction.onClick,
                        ) {
                        Icon(iconAction.icon, iconAction.label, tint = Color.Gray)
                    }
                }
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
                            onClick = { onClickTag(tag) })
                        Spacer(modifier = Modifier.width(4.dp))
                    }
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