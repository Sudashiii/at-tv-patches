package app.orf.patches.tracking

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.methodCall
import com.android.tools.smali.dexlib2.AccessFlags

/**
 * Constructor of the app's ÖWA wrapper (obfuscated, e.g. `I5.e` / `ta.e`, source file `OEWA.java`). It starts the
 * INFOnline SDKs: the legacy IOL session and an IOMB measurement for offer `at_a_orftvthek`, which also tracks app
 * starts and foreground/background on its own:
 *
 * ```
 * invoke-direct {p0}, Ljava/lang/Object;-><init>()V
 * ...
 * const-string v1, "OEWA tracking initializing"
 * ```
 */
object OewaConstructorFingerprint : Fingerprint(
    name = "<init>",
    returnType = "V",
    strings = listOf("OEWA tracking initializing"),
    filters = listOf(
        methodCall(smali = "Ljava/lang/Object;-><init>()V"),
    ),
)

/**
 * `OEWA.logEvent(event, path, replacements...)` (obfuscated), which every screen view goes through. It sends the
 * page view to both INFOnline SDKs and already returns early for a null event:
 *
 * ```
 * const-string v1, "Event is not set -> ignoring oewa"
 * ```
 */
object OewaLogEventFingerprint : Fingerprint(
    returnType = "V",
    strings = listOf("Event is not set -> ignoring oewa"),
)

/**
 * `Collector.loadConfig(configUrl, callback)` of the GfK (Sensic) S2S SDK. Not obfuscated. Every `S2SAgent`
 * creates a `Processor`, which calls this last; the SDK only starts once the config arrived:
 *
 * ```
 * invoke-static {p1, v0, p2, v1}, Lcom/gfk/s2s/utils/HTTPClient;->get(...)V
 * ```
 */
object GfkLoadConfigFingerprint : Fingerprint(
    definingClass = "Lcom/gfk/s2s/collector/Collector;",
    name = "loadConfig",
    returnType = "V",
    parameters = listOf("Ljava/lang/String;", "Lcom/gfk/s2s/collector/ICollectorConfigCallback;"),
)

/**
 * `AnalyticsConfiguratorKt.configureAnalytics(Player, Context, AnalyticsPlayerConfig)` of the Bitmovin player. Not
 * obfuscated. `Player.create`/`PlayerBuilder` call it for every player; for `AnalyticsPlayerConfig.Disabled` it
 * returns the player unchanged, for `Enabled` it attaches a Bitmovin Analytics collector.
 */
object ConfigurePlayerAnalyticsFingerprint : Fingerprint(
    definingClass = "Lcom/bitmovin/player/analytics/internal/AnalyticsConfiguratorKt;",
    name = "configureAnalytics",
    returnType = "Lcom/bitmovin/player/api/Player;",
    parameters = listOf(
        "Lcom/bitmovin/player/api/Player;",
        "Landroid/content/Context;",
        "Lcom/bitmovin/player/api/analytics/AnalyticsPlayerConfig;",
    ),
)

/**
 * `AnalyticsConfiguratorKt.configureAnalytics(Source, AnalyticsSourceConfig)`, the per-source counterpart that
 * attaches source metadata for the collector.
 */
object ConfigureSourceAnalyticsFingerprint : Fingerprint(
    definingClass = "Lcom/bitmovin/player/analytics/internal/AnalyticsConfiguratorKt;",
    name = "configureAnalytics",
    returnType = "Lcom/bitmovin/player/api/source/Source;",
    parameters = listOf(
        "Lcom/bitmovin/player/api/source/Source;",
        "Lcom/bitmovin/player/api/analytics/AnalyticsSourceConfig;",
    ),
)

/**
 * `SentryAndroid.init(context, configuration, logger)` (obfuscated, e.g. `io.sentry.android.core.O.b`). Automatic
 * init is off in the manifest (`io.sentry.auto-init`), so this call in the main activity is what starts Sentry:
 *
 * ```
 * const-string v1, "Fatal error during SentryAndroid.init(...)"
 * ```
 */
object SentryAndroidInitFingerprint : Fingerprint(
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.STATIC),
    returnType = "V",
    parameters = listOf("Landroid/content/Context;", "L", "L"),
    strings = listOf("Fatal error during SentryAndroid.init(...)"),
)
