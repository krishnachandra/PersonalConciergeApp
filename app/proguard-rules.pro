# Davinci ProGuard Rules

# Keep Supabase models
-keep class io.github.jan.supabase.** { *; }
-keep class com.davinci.app.data.remote.dto.** { *; }

# Keep Kotlin serialization
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt
-keepclassmembers class kotlinx.serialization.json.** { *** Companion; }

# Keep Room entities
-keep class com.davinci.app.data.local.entity.** { *; }

# Firebase
-keep class com.google.firebase.** { *; }
