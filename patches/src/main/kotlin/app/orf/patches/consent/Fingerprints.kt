package app.orf.patches.consent

import app.morphe.patcher.Fingerprint

/**
 * `Didomi.showNotice(activity)` of the Didomi consent SDK. Class and method name are not obfuscated (the parameter
 * type, `FragmentActivity`, is). The app calls `Didomi.setupUI(activity)` in its main activity; Didomi's lifecycle
 * observer then calls showNotice on resume once the SDK is ready, which shows the consent notice when consent still
 * needs to be collected.
 */
object DidomiShowNoticeFingerprint : Fingerprint(
    definingClass = "Lio/didomi/sdk/Didomi;",
    name = "showNotice",
    returnType = "V",
    parameters = listOf("L"),
)
