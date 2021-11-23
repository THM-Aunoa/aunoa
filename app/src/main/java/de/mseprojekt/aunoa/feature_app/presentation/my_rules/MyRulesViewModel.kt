package de.mseprojekt.aunoa.feature_app.presentation.my_rules

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import de.mseprojekt.aunoa.feature_app.domain.use_case.rule.RuleUseCases
import de.mseprojekt.aunoa.feature_app.presentation.add_rule.AddRuleEvent
import javax.inject.Inject

@HiltViewModel
class MyRulesViewModel @Inject constructor(
    private val ruleUseCases: RuleUseCases,
) : ViewModel() {

    fun onEvent(event: MyRulesEvent) {
        when (event) {

        }
    }
}