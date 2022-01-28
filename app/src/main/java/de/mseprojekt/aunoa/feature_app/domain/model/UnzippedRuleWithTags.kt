package de.mseprojekt.aunoa.feature_app.domain.model

import de.mseprojekt.aunoa.feature_app.domain.model.actionObjects.ActionObject
import de.mseprojekt.aunoa.feature_app.domain.model.triggerObjects.TriggerObject

data class UnzippedRuleWithTags(
    val rule: Rule,
    val action: ActionObject,
    val trigger: TriggerObject,
    val tags: List<Tag>
)
