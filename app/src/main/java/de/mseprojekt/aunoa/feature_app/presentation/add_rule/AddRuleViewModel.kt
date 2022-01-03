package de.mseprojekt.aunoa.feature_app.presentation.add_rule

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import de.mseprojekt.aunoa.feature_app.domain.use_case.rule.RuleUseCases
import javax.inject.Inject

@HiltViewModel
class AddRuleViewModel @Inject constructor(
    private val ruleUseCases: RuleUseCases,
) : ViewModel() {

    fun onEvent(event: AddRuleEvent) {
        when (event) {

        }
    }
}