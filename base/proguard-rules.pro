# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\PortableApps\android-sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# bugly https://bugly.qq.com/docs/user-guide/instruction-manual-android/
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.** {*;}

# Router https://github.com/chenenyu/Router
-keep class com.chenenyu.router.** {*;}
-keep class * implements com.chenenyu.router.RouteInterceptor {*;}

# BaseRecyclerViewAdapterHelper https://github.com/CymChad/BaseRecyclerViewAdapterHelper
-keep class com.chad.library.adapter.** {*;}

# retrofit https://github.com/square/retrofit
-dontwarn okio.**
-dontwarn javax.annotation.**

# Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
    **[] $VALUES;
    public *;
}
# nineoldandroids
-keep interface com.nineoldandroids.view.** { *; }
-dontwarn com.nineoldandroids.**
-keep class com.nineoldandroids.** { *; }
# support-v7-appcompat
-keep public class android.support.v7.widget.** { *; }
-keep public class android.support.v7.internal.widget.** { *; }
-keep public class android.support.v7.internal.view.menu.** { *; }
-keep public class * extends android.support.v4.view.ActionProvider {
    public <init>(android.content.Context);
}
# support-design
-dontwarn android.support.design.**
-keep class android.support.design.** { *; }
-keep interface android.support.design.** { *; }
-keep public class android.support.design.R$* { *; }

#okhttp
-dontwarn okio.**
-dontwarn javax.annotation.Nullable
-dontwarn javax.annotation.ParametersAreNonnullByDefault