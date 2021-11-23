package de.mseprojekt.aunoa.feature_app.presentation.actvity

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import de.mseprojekt.aunoa.feature_app.domain.use_case.activity.ActivityUseCases
import javax.inject.Inject

@HiltViewModel
class ActivityViewModel @Inject constructor(
    private val activityUseCases: ActivityUseCases,
) : ViewModel()  {

    fun onEvent(event: ActivityEvent) {
        when (event) {

        }
    }
}