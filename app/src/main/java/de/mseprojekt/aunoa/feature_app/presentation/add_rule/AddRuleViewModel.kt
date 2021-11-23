package de.mseprojekt.aunoa.feature_app.presentation.add_rule

import androidx.lifecycle.ViewModel
import de.mseprojekt.aunoa.feature_app.domain.use_case.rule.RuleUseCases
import de.mseprojekt.aunoa.feature_app.presentation.actvity.ActivityEvent
import javax.inject.Inject

class AddRuleViewModel @Inject constructor(
    private val ruleUseCases: RuleUseCases,
) : ViewModel() {

    fun onEvent(event: AddRuleEvent) {
        when (event) {

        }
    }
}