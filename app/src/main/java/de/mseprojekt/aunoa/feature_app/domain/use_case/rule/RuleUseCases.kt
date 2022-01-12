package de.mseprojekt.aunoa.feature_app.domain.use_case.rule

data class RuleUseCases(
    val getRule: GetRule,
    val getRules: GetRules,
    val insertRule: InsertRule,
    val getRulesWithTags: GetRulesWithTags,
    val getRulesWithoutFlow: GetRulesWithoutFlow,
    val setActive: SetActive,
    val setEnabled: SetEnabled,
    val removeRule: RemoveRule
)
