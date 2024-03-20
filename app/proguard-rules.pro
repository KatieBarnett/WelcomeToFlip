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
-dontwarn dev.katiebarnett.welcometoflip.core.Constants
-dontwarn dev.katiebarnett.welcometoflip.core.ImageUtilKt
-dontwarn dev.katiebarnett.welcometoflip.core.di.DispatchersModule_ProvidesIODispatcherFactory
-dontwarn dev.katiebarnett.welcometoflip.storage.models.QRCodeItem
-dontwarn dev.katiebarnett.welcometoflip.storage.models.QRColor
-dontwarn dev.katiebarnett.welcometoflip.storage.models.QRColorKt
-dontwarn dev.katiebarnett.welcometoflip.storage.models.QRIcon
-dontwarn dev.katiebarnett.welcometoflip.storage.models.QRIconKt
-dontwarn dev.katiebarnett.welcometoflip.core.theme.Dimen
-dontwarn dev.katiebarnett.welcometoflip.core.theme.ThemeKt
-dontwarn dev.katiebarnett.welcometoflip.storage.QRCodesDataSource
-dontwarn dev.katiebarnett.welcometoflip.storage.QRCodesRepository
-dontwarn dev.katiebarnett.welcometoflip.storage.QrCodesSerializer
-dontwarn dev.katiebarnett.welcometoflip.storage.di.DataStoreModule_ProvidesQRCodesStoreFactory
