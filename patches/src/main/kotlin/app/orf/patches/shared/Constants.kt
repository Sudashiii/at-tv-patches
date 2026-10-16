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
            AppTarget(version = "6.13.5-mobile"),
            AppTarget(version = "6.14.15-tv"),
            // Fingerprints rely on strings and on SDK/model names that the app keeps, not on obfuscated names,
            // so later releases likely work too.
            AppTarget(version = null, isExperimental = true),
        ),
    )
}
