package de.mseprojekt.aunoa.other

import android.content.Context
import android.content.Intent
import androidx.compose.material.ExperimentalMaterialApi
import de.mseprojekt.aunoa.services.AunoaService
import de.mseprojekt.aunoa.services.INTENT_COMMAND

@ExperimentalMaterialApi
fun Context.foregroundStartService(command: String) {
    val intent = Intent(this, AunoaService::class.java)
    when (command) {
        "Start" -> {
            intent.putExtra(INTENT_COMMAND, command)
            this.startForegroundService(intent)

        }
        "Exit" -> {
            intent.putExtra(INTENT_COMMAND, command)
            this.stopService(intent)
        }
        "Update" -> {
            intent.putExtra(INTENT_COMMAND, command)
            this.startForegroundService(intent)
        }
    }
}