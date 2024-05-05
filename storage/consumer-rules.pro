
-keepclassmembers class * extends androidx.datastore.preferences.protobuf.GeneratedMessageLite {
    <fields>;
}

-dontwarn dev.veryniche.welcometoflip.storage.SavedGamesDataSource
-dontwarn dev.veryniche.welcometoflip.storage.SavedGamesRepository
-dontwarn dev.veryniche.welcometoflip.storage.SavedGamesSerializer
-dontwarn dev.veryniche.welcometoflip.storage.UserPreferences
-dontwarn dev.veryniche.welcometoflip.storage.UserPreferencesRepository
-dontwarn dev.veryniche.welcometoflip.storage.di.DataStoreModule_ProvidePreferencesDataStoreFactory
-dontwarn dev.veryniche.welcometoflip.storage.di.DataStoreModule_ProvidesSavedGamesDataStoreFactory