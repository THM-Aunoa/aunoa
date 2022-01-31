package de.mseprojekt.aunoa.feature_app.presentation.util.spinner

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun Spinner(options: List<String>) {
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf("") }

    val icon = if (expanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    Column() {
        OutlinedTextField(
            value = selectedText,
            enabled = false,
            onValueChange = { selectedText = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .clickable { expanded = !expanded },
            label = { Text("Hour") },
            trailingIcon = {
                Icon(icon, "", Modifier.clickable { expanded = !expanded })
            }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
                options.forEach { option ->
                    DropdownMenuItem(onClick = {
                    }) {
                        Text(text = option)
                        expanded = false
                    }
                }
        }
    }

}