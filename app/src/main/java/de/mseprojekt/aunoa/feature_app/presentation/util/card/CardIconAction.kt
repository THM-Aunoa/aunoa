package de.mseprojekt.aunoa.feature_app.presentation.util.card

import androidx.compose.ui.graphics.vector.ImageVector

data class CardIconAction(val icon: ImageVector, val onClick: () -> Unit, val label: String)
