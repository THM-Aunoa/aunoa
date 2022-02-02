package de.mseprojekt.aunoa.feature_app.presentation.util.chip

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@ExperimentalMaterialApi
@Composable
fun AunoaChip(label: String, icon: ImageVector? = null, onClick: () -> Unit = {}) {
    Box() {
        Surface(
            elevation = 1.dp,
            shape = RoundedCornerShape(8.dp),
            color = Color.LightGray,
            onClick = onClick
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    label,
                    modifier = Modifier.padding(vertical = 4.dp, horizontal = 6.dp),
                    fontSize = 12.sp,
                    color = Color.DarkGray
                    //style = MaterialTheme.typography.button.copy(color = Color.DarkGray)
                )
                if (icon != null) Icon(
                    icon,
                    "",
                    tint = Color.DarkGray,
                    modifier = Modifier
                        .scale(.8f)
                )
            }
        }
    }
}

