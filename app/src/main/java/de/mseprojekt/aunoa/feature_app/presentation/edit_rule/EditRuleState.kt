package de.mseprojekt.aunoa.feature_app.presentation.edit_rule

import de.mseprojekt.aunoa.feature_app.domain.model.Tag
import de.mseprojekt.aunoa.feature_app.domain.model.actionObjects.ActionObject
import de.mseprojekt.aunoa.feature_app.domain.model.actionObjects.VolumeAction
import de.mseprojekt.aunoa.feature_app.domain.model.triggerObjects.TimeTrigger
import de.mseprojekt.aunoa.feature_app.domain.model.triggerObjects.TriggerObject
import java.time.DayOfWeek

data class EditRuleState(
    val ruleId: Int = -1,
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
    val triggerObjectName: String = "",
    val tags: List<Tag> = emptyList()
)