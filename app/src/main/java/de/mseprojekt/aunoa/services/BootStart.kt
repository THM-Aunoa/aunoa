package de.mseprojekt.aunoa.services

import android.content.Intent

import android.content.BroadcastReceiver
import android.content.Context
import android.util.Log
import de.mseprojekt.aunoa.feature_app.domain.use_case.rule.RuleUseCases
import de.mseprojekt.aunoa.feature_app.domain.use_case.state.StateUseCases
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class BootStart : BroadcastReceiver() {
    @Inject
    lateinit var stateUseCases: StateUseCases
    override fun onReceive(context: Context, arg1: Intent?) {
        val action = arg1?.action

        if (action != null) {
            if (Intent.ACTION_BOOT_COMPLETED == action || Intent.ACTION_LOCKED_BOOT_COMPLETED == action){
                CoroutineScope(Dispatchers.Main).launch {
                    if (stateUseCases.getCurrentState()) {
                        startService(context)
                    }
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