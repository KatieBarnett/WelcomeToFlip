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
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

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
-dontwarn dev.veryniche.welcometoflip.core.Constants
-dontwarn dev.veryniche.welcometoflip.core.ImageUtilKt
-dontwarn dev.veryniche.welcometoflip.core.di.DispatchersModule_ProvidesIODispatcherFactory
-dontwarn dev.veryniche.welcometoflip.storage.models.QRCodeItem
-dontwarn dev.veryniche.welcometoflip.storage.models.QRColor
-dontwarn dev.veryniche.welcometoflip.storage.models.QRColorKt
-dontwarn dev.veryniche.welcometoflip.storage.models.QRIcon
-dontwarn dev.veryniche.welcometoflip.storage.models.QRIconKt
-dontwarn dev.veryniche.welcometoflip.core.theme.Dimen
-dontwarn dev.veryniche.welcometoflip.core.theme.ThemeKt
-dontwarn dev.veryniche.welcometoflip.storage.QRCodesDataSource
-dontwarn dev.veryniche.welcometoflip.storage.QRCodesRepository
-dontwarn dev.veryniche.welcometoflip.storage.QrCodesSerializer
-dontwarn dev.veryniche.welcometoflip.storage.di.DataStoreModule_ProvidesQRCodesStoreFactory
