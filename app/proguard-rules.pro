# Preserve line numbers and source file names for crash reporting
-keepattributes Exceptions, InnerClasses, Signature, Deprecated, SourceFile, LineNumberTable, *Annotation*, EnclosingMethod

# Start.io (StartApp) ProGuard Rules
-keep class com.startapp.** { *; }
-keep class com.truenet.** { *; }
-dontwarn android.webkit.JavascriptInterface
-dontwarn com.startapp.**
-dontwarn org.jetbrains.annotations.**

# Room & Persistence
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-dontwarn androidx.room.paging.**
