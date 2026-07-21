# Add project specific ProGuard rules here.
-keepattributes *Annotation*
-keep class com.cultofcards.data.** { *; }
-keep class com.google.gson.** { *; }
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}
