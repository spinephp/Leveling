# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\Android\sdk/tools/proguard/proguard-android.txt
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

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
   public *;
}
-keepattributes EnclosingMethod
-keepattributes InnerClasses
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }

-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

-keepattributes SourceFile,LineNumberTable
-keep class com.parse.*{ *; }
-dontwarn com.parse.**
-dontwarn com.squareup.picasso.**
-keepclasseswithmembernames class * {
    native <methods>;
}
-dontshrink
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
-ignorewarnings
-dontwarn
-allowaccessmodification
-dontskipnonpubliclibraryclassmembers
-keepattributes *Annotation*
-libraryjars <java.home>\lib\rt.jar

-dontwarn org.apache.commons.**

-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.support.v4.**
-keep public class com.android.vending.licensing.ILicensingService
-keep class **.R$* {   *;  }
 -optimizationpasses 5
 -dontusemixedcaseclassnames
 -dontskipnonpubliclibraryclasses
 -dontpreverify
 -ignorewarnings
 -verbose
 -optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
 -ignorewarnings
 -keep class javax.ws.rs.** { *; }
 -dontwarn com.alibaba.fastjson.**
 -keep class com.alibaba.fastjson.** { *; }

 -keep class com.alipay.android.app.IAlixPay{*;}
 -keep class com.alipay.android.app.IAlixPay$Stub{*;}
 -keep class com.alipay.android.app.IRemoteServiceCallback{*;}
 -keep class com.alipay.android.app.IRemoteServiceCallback$Stub{*;}
 -keep class com.alipay.sdk.app.PayTask{ public *;}
 -keep class com.alipay.sdk.app.AuthTask{ public *;}
 -keep class com.alipay.sdk.app.H5PayCallback {
     <fields>;
     <methods>;
 }
 -keep class com.alipay.android.phone.mrpc.core.** { *; }
 -keep class com.alipay.apmobilesecuritysdk.** { *; }
 -keep class com.alipay.mobile.framework.service.annotation.** { *; }
 -keep class com.alipay.mobilesecuritysdk.face.** { *; }
 -keep class com.alipay.tscenter.biz.rpc.** { *; }
 -keep class org.json.alipay.** { *; }
 -keep class com.alipay.tscenter.** { *; }
 -keep class com.ta.utdid2.** { *;}
 -keep class com.ut.device.** { *;}

-keepattributes InnerClasses-dontoptimize
-optimizations optimization_filter