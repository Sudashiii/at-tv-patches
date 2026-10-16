package app.orf.patches.consent

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.orf.patches.shared.Constants.COMPATIBILITY_ORF_ON

@Suppress("unused")
val rejectCookieBannerPatch = bytecodePatch(
    name = "Reject cookie banner",
    description = "Skips the cookie consent banner and saves 'Disagree to all' instead, as if you declined it. " +
        "The privacy settings in the app stay available.",
    default = true,
) {
    compatibleWith(COMPATIBILITY_ORF_ON)

    // The app itself already has this path: on Technogym gym-equipment boxes it skips Didomi.setupUI and calls
    // Didomi.setUserDisagreeToAll once the SDK is ready. That flag changes much more than the banner, so the
    // patch does the same inside Didomi instead.
    //
    // showNotice is only reached once Didomi is ready. It now asks Didomi whether consent still needs to be
    // collected (first launch, or when Didomi wants to ask again) and if so saves "disagree to all" instead of
    // showing the notice. The privacy settings screen (Didomi.showPreferences) is not affected, so the choice can
    // still be viewed and changed there.
    execute {
        // p1 (the activity) is free to reuse for the result, so no extra register is needed.
        DidomiShowNoticeFingerprint.method.addInstructions(
            0,
            """
                invoke-virtual { p0 }, Lio/didomi/sdk/Didomi;->shouldConsentBeCollected()Z
                move-result p1
                if-eqz p1, :orf_no_consent_needed
                invoke-virtual { p0 }, Lio/didomi/sdk/Didomi;->setUserDisagreeToAll()Z
                :orf_no_consent_needed
                return-void
            """,
        )
    }
}
