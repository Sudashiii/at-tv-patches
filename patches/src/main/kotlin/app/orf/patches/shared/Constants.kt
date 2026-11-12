package app.orf.patches.shared

import app.morphe.patcher.patch.ApkFileType
import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

object Constants {
    // The phone/tablet app and the Android TV app are separate APKMirror listings with separate version lines
    // ("-mobile" and "-tv"), but share this package name. The patches work on both.
    val COMPATIBILITY_ORF_ON = Compatibility(
        name = "ORF ON",
        packageName = "com.nousguide.android.orftvthek",
        // APKMirror only offers this app as a split bundle.
        apkFileType = ApkFileType.APKM,
        // Background of the launcher icon (res/drawable-anydpi-v24/ic_launcher_background.xml).
        appIconColor = 0x14161A,
        targets = listOf(
            // Developed and tested against these releases (APKMirror, versionCodes 5000169 and 5000184).
            // Morphe shows both under one app card, so the descriptions say which build is for which device.
            AppTarget(version = "6.13.5-mobile", description = "Phone and tablet"),
            AppTarget(version = "6.14.15-tv", description = "Android TV"),
            // Fingerprints rely on strings and on SDK/model names that the app keeps, not on obfuscated names,
            // so later releases likely work too.
            AppTarget(version = null, isExperimental = true),
        ),
        // One entry for both builds: Morphe Manager merges entries by package name, so separate MOBILE and TV
        // entries wouldn't show up as separate apps yet. The target descriptions tell the builds apart instead.
        formFactor = null,
    )
}
