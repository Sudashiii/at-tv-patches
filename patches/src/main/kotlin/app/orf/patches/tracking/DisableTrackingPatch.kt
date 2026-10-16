package app.orf.patches.tracking

import app.morphe.patcher.extensions.InstructionExtensions.addInstruction
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.orf.patches.shared.Constants.COMPATIBILITY_ORF_ON

@Suppress("unused")
val disableTrackingPatch = bytecodePatch(
    name = "Disable tracking",
    description = "Stops ÖWA/INFOnline reach measurement, GfK video measurement, Bitmovin player analytics and " +
        "Sentry crash reporting from starting.",
    default = true,
) {
    compatibleWith(COMPATIBILITY_ORF_ON)

    // Unlike ServusTV, ORF ON doesn't tie these SDKs to the cookie consent: ÖWA and GfK always run (ÖWA with a
    // "custom consent"), so rejecting the cookie banner doesn't turn them off. Each one is stopped where it starts,
    // in a state the SDK or the app already handles:
    //
    // - ÖWA/INFOnline: the app's OEWA wrapper starts both INFOnline SDKs in its constructor and sends every page
    //   view through one log method. The constructor now returns right after Object.<init> (no SDK session, no
    //   IOMB measurement, so no automatic app start/foreground events), and the log method returns at once, like
    //   it already does for a null event. The IOMB core is still created by its manifest provider, but without a
    //   measurement it sends nothing.
    // - GfK S2S: every S2SAgent's Processor loads its config from GfK first and only starts sending once it
    //   arrived. loadConfig now reports a failed load right away, the same path as when the config server is
    //   unreachable: no transmitter or heartbeat is created and events stay in the SDK's in-memory buffer.
    // - Bitmovin Analytics: configureAnalytics returns the player/source unchanged, which is what it does for
    //   AnalyticsPlayerConfig.Disabled. No collector is attached. (The timeshift player also builds a collector
    //   but never attaches it to a player.)
    // - Sentry: automatic init is off in the manifest and the main activity calls SentryAndroid.init itself. Init
    //   now returns right away, so Sentry keeps its no-op hub and the app's Sentry calls do nothing.
    execute {
        OewaConstructorFingerprint.let {
            it.method.addInstruction(it.instructionMatches.first().index + 1, "return-void")
        }
        OewaLogEventFingerprint.method.addInstruction(0, "return-void")

        // p1 (the config URL) as the null Config and the false success flag, so no extra register is needed.
        GfkLoadConfigFingerprint.method.addInstructions(
            0,
            """
                const/4 p1, 0x0
                invoke-interface { p2, p1, p1 }, Lcom/gfk/s2s/collector/ICollectorConfigCallback;->onCompletion(Lcom/gfk/s2s/collector/Config;Z)V
                return-void
            """,
        )

        ConfigurePlayerAnalyticsFingerprint.method.addInstruction(0, "return-object p0")
        ConfigureSourceAnalyticsFingerprint.method.addInstruction(0, "return-object p0")

        SentryAndroidInitFingerprint.method.addInstruction(0, "return-void")
    }
}
