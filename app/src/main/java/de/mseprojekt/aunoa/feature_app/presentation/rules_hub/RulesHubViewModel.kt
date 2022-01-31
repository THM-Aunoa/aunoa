package de.mseprojekt.aunoa.feature_app.presentation.rules_hub

import android.app.Application
import android.content.Intent
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.mseprojekt.aunoa.feature_app.domain.model.UnzippedRuleWithTags
import de.mseprojekt.aunoa.feature_app.domain.model.triggerObjects.CellTrigger
import de.mseprojekt.aunoa.feature_app.domain.use_case.cell.CellUseCases
import de.mseprojekt.aunoa.feature_app.domain.use_case.rule.RuleUseCases
import de.mseprojekt.aunoa.feature_app.domain.use_case.rulesHub.RulesHubUseCases
import de.mseprojekt.aunoa.other.AunoaEventInterface
import de.mseprojekt.aunoa.other.AunoaViewModelInterface
import de.mseprojekt.aunoa.services.AunoaService
import de.mseprojekt.aunoa.services.INTENT_COMMAND
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@ExperimentalMaterialApi
@HiltViewModel
class RulesHubViewModel @Inject constructor(
    private val rulesHubUseCases: RulesHubUseCases,
    private val ruleUseCases: RuleUseCases,
    private val cellUseCases: CellUseCases,
    private val application: Application,
) : ViewModel(), AunoaViewModelInterface {

    private val initRules: List<UnzippedRuleWithTags> = rulesHubUseCases.getHubRules()
    private val _state = mutableStateOf(RulesHubState())
    val state: State<RulesHubState> = _state

    private val _eventFlow = MutableSharedFlow<RulesHubViewModel.UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        _state.value = state.value.copy(rules = initRules)
    }

    override fun onEvent(event: AunoaEventInterface) {
        when (event) {
            is RulesHubEvent.SearchRules -> {
                val rules = initRules.filter {
                    it.rule.title.lowercase().contains(event.searchText.lowercase())
                }
                _state.value = state.value.copy(rules = rules, searchText = event.searchText)
            }
            is RulesHubEvent.FilterRules -> {
                val rules = initRules.filter { it.tags.contains(event.filterTag) }
                _state.value = state.value.copy(rules = rules, filterTag = event.filterTag)
            }
            is RulesHubEvent.ResetFilter -> {
                _state.value = state.value.copy(rules = initRules)
            }
            is RulesHubEvent.AddRule -> {
                val rule = event.value
                viewModelScope.launch {
                    try {
                        if(rule.trigger is CellTrigger){
                            val regionId= cellUseCases.getRegionIdByName(rule.trigger.name)
                            if (regionId==null){
                                cellUseCases.insertRegion(rule.trigger.name)
                            }
                        }
                        val ruleId = ruleUseCases.insertRule(
                            rule.action,
                            rule.trigger,
                            rule.rule.title,
                            rule.rule.description,
                            rule.rule.priority
                        )
                        val newTags = ruleUseCases.insertTags(rule.tags)
                        newTags.forEach { tag ->
                            ruleUseCases.insertRuleTagCrossRef(ruleId, tag.tagId!!)
                        }
                        val intent = Intent(application, AunoaService::class.java)
                        intent.putExtra(INTENT_COMMAND, "Update")
                        application.startForegroundService(intent)
                        _eventFlow.emit(RulesHubViewModel.UiEvent.AddRule(message = "Successfully added", ruleId = ruleId))
                    } catch (e: Error) {
                        println(e)
                    }
                }
            }
        }
    }

    sealed class UiEvent {
        data class AddRule(val message: String, val ruleId: Int) : UiEvent()
    }

}