// Generated by Dagger (https://dagger.dev).
package de.mseprojekt.aunoa.feature_app.presentation.operation;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

@DaggerGenerated
@SuppressWarnings({
    "unchecked",
    "rawtypes"
})
public final class ActivityViewModel_HiltModules_KeyModule_ProvideFactory implements Factory<String> {
  @Override
  public String get() {
    return provide();
  }

  public static ActivityViewModel_HiltModules_KeyModule_ProvideFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static String provide() {
    return Preconditions.checkNotNullFromProvides(ActivityViewModel_HiltModules.KeyModule.provide());
  }

  private static final class InstanceHolder {
    private static final ActivityViewModel_HiltModules_KeyModule_ProvideFactory INSTANCE = new ActivityViewModel_HiltModules_KeyModule_ProvideFactory();
  }
}
