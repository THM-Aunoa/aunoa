package de.mseprojekt.aunoa.other

import android.content.Context
import android.content.Intent
import de.mseprojekt.aunoa.services.AunoaService
import de.mseprojekt.aunoa.services.INTENT_COMMAND

fun Context.foregroundStartService(command: String) {
    val intent = Intent(this, AunoaService::class.java)
    if (command == "Start") {
        intent.putExtra(INTENT_COMMAND, command)
        this.startForegroundService(intent)

    } else if (command == "Exit") {
        intent.putExtra(INTENT_COMMAND, command)
        this.stopService(intent)
    }
}