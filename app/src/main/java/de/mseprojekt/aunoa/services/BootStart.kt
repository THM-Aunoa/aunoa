package de.mseprojekt.aunoa.services

import android.content.Intent

import android.content.BroadcastReceiver
import android.content.Context
import androidx.compose.material.ExperimentalMaterialApi
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
class BootStart : BroadcastReceiver() {


    override fun onReceive(context: Context, arg1: Intent?) {
        val action = arg1?.action

        if (action != null) {
            if (Intent.ACTION_BOOT_COMPLETED == action || Intent.ACTION_LOCKED_BOOT_COMPLETED == action){
                CoroutineScope(Dispatchers.Main).launch {
                    startService(context)
                }
            }
        }
    }

    private fun startService(context: Context){
        val intent = Intent(context, AunoaService::class.java)
        intent.putExtra(INTENT_COMMAND, "Start")
        context.startForegroundService(intent)
    }
}