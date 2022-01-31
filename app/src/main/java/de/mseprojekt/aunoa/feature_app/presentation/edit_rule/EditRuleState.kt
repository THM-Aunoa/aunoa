package de.mseprojekt.aunoa.feature_app.presentation.edit_rule

import android.location.Location
import de.mseprojekt.aunoa.feature_app.domain.model.Region
import de.mseprojekt.aunoa.feature_app.domain.model.Tag
import de.mseprojekt.aunoa.feature_app.domain.model.actionObjects.ActionObject
import de.mseprojekt.aunoa.feature_app.domain.model.actionObjects.VolumeAction
import de.mseprojekt.aunoa.feature_app.domain.model.triggerObjects.CellTrigger
import de.mseprojekt.aunoa.feature_app.domain.model.triggerObjects.LocationTrigger
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
    val timeTrigger: TimeTrigger = TimeTrigger(
        startTime = 79200,
        endTime = 28800,
        startWeekday = DayOfWeek.THURSDAY,
        endWeekday = DayOfWeek.FRIDAY,
    ),
    val locationTrigger: LocationTrigger = LocationTrigger(
        latitude = 50.568988963340395,
        longitude = 8.156204391209968,
        radius = 1.0
    ),
    val cellTrigger: CellTrigger = CellTrigger("Home"),
    val triggerObjectName: String = "",
    val trigger: TriggerObject? = null,
    val tags: List<Tag> = emptyList(),
    val regions: List<Region> = emptyList()
)