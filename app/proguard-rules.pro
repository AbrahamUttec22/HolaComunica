# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\ANDROID\SDK/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
   public *;
}
-keepattributes EnclosingMethod
-keepattributes InnerClasses

-dontwarn org.xmlpull.v1.**
-dontnote org.xmlpull.v1.**
-keep class org.xmlpull.** { *; }
-keepclassmembers class org.xmlpull.** { *; }
# Firebase
-keep class com.google.firebase.** { *; }
-keep class org.apache.** { *; }
-keepnames class com.fasterxml.jackson.** { *; }
-keepnames class javax.servlet.** { *; }
-keepnames class org.ietf.jgss.** { *; }
-dontwarn org.apache.**
-dontwarn org.w3c.dom.**
# Retrofit 1.X
-keep class com.squareup.okhttp.** { *; }
-keep class retrofit.** { *; }
-keep interface com.squareup.okhttp.** { *; }

-dontwarn com.squareup.okhttp.**
-dontwarn okio.**
-dontwarn retrofit.**
-dontwarn rx.**

-keepclasseswithmembers class * {
    @retrofit.http.* <methods>;
}
# Platform calls Class.forName on types which do not exist on Android to determine platform.
-dontnote retrofit2.Platform
# Platform used when running on Java 8 VMs. Will not be used at runtime.
-dontwarn retrofit2.Platform$Java8
# Retain generic type information for use by reflection by converters and adapters.
-keepattributes Signature
# Retain declared checked exceptions for use by a Proxy instance.
-keepattributes Exceptions

-keepclassmembers class net.tecgurus.holacomunicate.actividadesfragment.** {
 *;
}
-keepclassmembers class net.tecgurus.holacomunicate.actividadesfragmentadmin.** {
 *;
}
-keepclassmembers class net.tecgurus.holacomunicate.adapter.** {
 *;
}
-keepclassmembers class net.tecgurus.holacomunicate.admin.** {
 *;
}
-keepclassmembers class net.tecgurus.holacomunicate.checador.** {
 *;
}
-keepclassmembers class net.tecgurus.holacomunicate.Empresa.** {
 *;
}
-keepclassmembers class net.tecgurus.holacomunicate.formularios.** {
 *;
}
-keepclassmembers class net.tecgurus.holacomunicate.message.** {
 *;
}
-keepclassmembers class net.tecgurus.holacomunicate.model.** {
 *;
}
-keepclassmembers class net.tecgurus.holacomunicate.paypal.** {
 *;
}

-keepclassmembers class net.tecgurus.holacomunicate.register.** {
 *;
}

-keepclassmembers class net.tecgurus.holacomunicate.utils.** {
 *;
}

-keep class net.tecgurus.holacomunicate.** {*;}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-dontwarn com.beloo.widget.chipslayoutmanager.**
