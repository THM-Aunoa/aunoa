package de.mseprojekt.aunoa.feature_app.presentation.rule_details

import androidx.lifecycle.ViewModel
import de.mseprojekt.aunoa.feature_app.domain.use_case.rule.RuleUseCases
import de.mseprojekt.aunoa.feature_app.presentation.add_rule.AddRuleEvent
import javax.inject.Inject

class RuleDetailsViewModel @Inject constructor(
    private val ruleUseCases: RuleUseCases,
) : ViewModel() {

    fun onEvent(event: RuleDetailsEvent) {
        when (event) {

        }
    }
}