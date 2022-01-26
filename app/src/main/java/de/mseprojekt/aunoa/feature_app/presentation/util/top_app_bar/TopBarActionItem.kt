package de.mseprojekt.aunoa.feature_app.presentation.util.top_app_bar

import androidx.compose.ui.graphics.vector.ImageVector

data class TopBarActionItem(val name: String, val icon: ImageVector, val onClick: () -> Unit)
