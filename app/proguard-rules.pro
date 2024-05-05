# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.kts.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
-renamesourcefileattribute SourceFile

-keepclassmembers class * extends com.google.protobuf.GeneratedMessageLite {
    <fields>;
}
-keep class * extends com.google.crypto.tink.shaded.protobuf.GeneratedMessageLite { *; }

# from Missing classes detected while running R8 message
-dontwarn dev.veryniche.welcometoflip.core.models.Action
-dontwarn dev.veryniche.welcometoflip.core.models.Astronaut
-dontwarn dev.veryniche.welcometoflip.core.models.Card
-dontwarn dev.veryniche.welcometoflip.core.models.CardFace
-dontwarn dev.veryniche.welcometoflip.core.models.GameDataKt
-dontwarn dev.veryniche.welcometoflip.core.models.GameType
-dontwarn dev.veryniche.welcometoflip.core.models.GameTypeKt
-dontwarn dev.veryniche.welcometoflip.core.models.Letter
-dontwarn dev.veryniche.welcometoflip.core.models.Lightning
-dontwarn dev.veryniche.welcometoflip.core.models.Number10
-dontwarn dev.veryniche.welcometoflip.core.models.Number12
-dontwarn dev.veryniche.welcometoflip.core.models.Number1
-dontwarn dev.veryniche.welcometoflip.core.models.Number2
-dontwarn dev.veryniche.welcometoflip.core.models.Number3
-dontwarn dev.veryniche.welcometoflip.core.models.Plant
-dontwarn dev.veryniche.welcometoflip.core.models.Robot
-dontwarn dev.veryniche.welcometoflip.core.models.SavedGame
-dontwarn dev.veryniche.welcometoflip.core.models.SoloA
-dontwarn dev.veryniche.welcometoflip.core.models.SoloB
-dontwarn dev.veryniche.welcometoflip.core.models.SoloC
-dontwarn dev.veryniche.welcometoflip.core.models.Water
-dontwarn dev.veryniche.welcometoflip.core.models.X
-dontwarn dev.veryniche.welcometoflip.core.theme.ColorKt
-dontwarn dev.veryniche.welcometoflip.core.di.DispatchersModule_ProvidesIODispatcherFactory
-dontwarn dev.veryniche.welcometoflip.storage.SavedGamesDataSource
-dontwarn dev.veryniche.welcometoflip.storage.SavedGamesRepository
-dontwarn dev.veryniche.welcometoflip.storage.SavedGamesSerializer
-dontwarn dev.veryniche.welcometoflip.storage.UserPreferences
-dontwarn dev.veryniche.welcometoflip.storage.UserPreferencesRepository
-dontwarn dev.veryniche.welcometoflip.storage.di.DataStoreModule_ProvidePreferencesDataStoreFactory
-dontwarn dev.veryniche.welcometoflip.storage.di.DataStoreModule_ProvidesSavedGamesDataStoreFactory
