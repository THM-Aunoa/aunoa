package de.mseprojekt.aunoa.feature_app.domain.use_case.state

data class StateUseCases(
    val getCurrentState: GetCurrentState,
    val insertState: InsertState,
    val isFirstRun: IsFirstRun
)
