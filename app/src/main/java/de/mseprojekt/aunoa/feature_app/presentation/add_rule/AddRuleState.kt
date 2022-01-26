package de.mseprojekt.aunoa.feature_app.presentation.add_rule

import de.mseprojekt.aunoa.feature_app.data.data_source.relations.RuleWithTags
import de.mseprojekt.aunoa.feature_app.domain.model.actionObjects.ActionObject
import de.mseprojekt.aunoa.feature_app.domain.model.actionObjects.VolumeAction
import de.mseprojekt.aunoa.feature_app.domain.model.triggerObjects.TimeTrigger
import de.mseprojekt.aunoa.feature_app.domain.model.triggerObjects.TriggerObject
import java.time.DayOfWeek

data class AddRuleState(
    val title: String = "",
    val description: String = "",
    val priority: Int = 0,
    val action: ActionObject = VolumeAction(
        activateVolume = 0,
        deactivateVolume = 2
    ),
    val actionObjectName: String = "VolumeAction",
    val trigger: TriggerObject = TimeTrigger(
        startTime = 79200,
        endTime = 28800,
        startWeekday = DayOfWeek.THURSDAY,
        endWeekday = DayOfWeek.FRIDAY,
    ),
    val triggerObjectName: String = "TimeTrigger"
)