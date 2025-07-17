# Tams Wallet ProGuard Configuration
# Optimized for production release with security and performance

# Keep line numbers for crash reporting
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# Keep annotations
-keepattributes *Annotation*

# Keep generic signatures for reflection
-keepattributes Signature

# Keep inner classes
-keepattributes InnerClasses,EnclosingMethod

# Optimization settings
-optimizations !code/simplification/arithmetic,!code/simplification/cast,!field/*,!class/merging/*
-optimizationpasses 5
-allowaccessmodification
-dontpreverify

# Remove logging in release builds
-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int i(...);
    public static int w(...);
    public static int d(...);
    public static int e(...);
}

# Room Database
-keep class androidx.room.** { *; }
-keep @androidx.room.Entity class * { *; }
-keep @androidx.room.Dao class * { *; }
-keep @androidx.room.Database class * { *; }
-keep @androidx.room.TypeConverter class * { *; }
-dontwarn androidx.room.paging.**

# MPAndroidChart
-keep class com.github.mikephil.charting.** { *; }
-dontwarn com.github.mikephil.charting.**

# Material Components
-keep class com.google.android.material.** { *; }
-dontwarn com.google.android.material.**

# Biometric Authentication
-keep class androidx.biometric.** { *; }
-dontwarn androidx.biometric.**

# Lottie Animations
-keep class com.airbnb.lottie.** { *; }
-dontwarn com.airbnb.lottie.**

# Shimmer Effect
-keep class com.facebook.shimmer.** { *; }
-dontwarn com.facebook.shimmer.**

# Keep all model classes (important for database and serialization)
-keep class com.tamswallet.app.data.model.** { *; }

# Keep repository classes (they use reflection)
-keep class com.tamswallet.app.data.repository.** { *; }

# Keep database classes
-keep class com.tamswallet.app.data.database.** { *; }

# Keep utility classes that might be accessed via reflection
-keep class com.tamswallet.app.utils.** { *; }

# Keep ViewBinding classes
-keep class com.tamswallet.app.databinding.** { *; }

# Keep custom views
-keep class com.tamswallet.app.ui.custom.** { *; }

# AndroidX and Support Library
-keep class androidx.** { *; }
-dontwarn androidx.**

# Kotlin
-keep class kotlin.** { *; }
-keep class kotlin.Metadata { *; }
-dontwarn kotlin.**
-keepclassmembers class **$WhenMappings {
    <fields>;
}
-keepclassmembers class kotlin.Metadata {
    public <methods>;
}

# Security - Keep encryption related classes
-keep class javax.crypto.** { *; }
-keep class java.security.** { *; }

# Prevent obfuscation of classes that use reflection
-keepclassmembers class * {
    @androidx.room.* <methods>;
}

# Keep native methods
-keepclasseswithmembernames class * {
    native <methods>;
}

# Keep setters and getters for model classes
-keepclassmembers public class * extends androidx.room.** {
    public void set*(***);
    public *** get*();
}

# Keep Parcelable implementations
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

# Keep Serializable classes
-keepnames class * implements java.io.Serializable
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# Missing classes - Add rules for R8 optimization
-dontwarn javax.annotation.**
-dontwarn okio.**
-dontwarn okhttp3.**
-dontwarn retrofit2.**

# Keep javax.annotation classes
-keep class javax.annotation.** { *; }

# OkHttp and OkIO
-keep class okhttp3.** { *; }
-keep class okio.** { *; }
-dontwarn okhttp3.**
-dontwarn okio.**

# Retrofit
-keep class retrofit2.** { *; }
-dontwarn retrofit2.**

# Keep all classes that might be referenced by reflection
-keep class * extends java.lang.annotation.Annotation { *; }

# Additional safety rules for missing classes
-dontwarn java.lang.invoke.**
-dontwarn **$$serializer
-dontwarn javax.annotation.processing.**
-dontwarn javax.lang.model.**
-dontwarn org.codehaus.mojo.animal_sniffer.**