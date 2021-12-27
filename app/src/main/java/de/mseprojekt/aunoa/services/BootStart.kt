package de.mseprojekt.aunoa.services

import android.content.Intent

import android.content.BroadcastReceiver
import android.content.Context
import android.util.Log

class BootStart : BroadcastReceiver() {
    override fun onReceive(context: Context, arg1: Intent?) {
        val action = arg1?.action

        if (action != null) {
            if (Intent.ACTION_BOOT_COMPLETED == action){
                startService(context)
            }
        }

    }
    private fun startService(context: Context){
        val intent = Intent(context, AunoaService::class.java)
        intent.putExtra(INTENT_COMMAND, "Start")
        context.startForegroundService(intent)
    }
}