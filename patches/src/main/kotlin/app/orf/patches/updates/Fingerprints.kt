package app.orf.patches.updates

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.fieldAccess
import com.android.tools.smali.dexlib2.Opcode

/**
 * `AndroidVersion(version, message, forced, minandroidversion, isDisplayAllowed)`, one entry of the backend's
 * version list (`Versions.android` on phones, `androidtv`/`firetv` on TV). Moshi model, so class, constructor and
 * field names are not obfuscated:
 *
 * ```
 * iput-boolean p4, p0, Lcom/nousguide/android/orftvthek/feature/relaunch/legacy/shared/data/modelsTV/AndroidVersion;->forced:Z
 * ```
 */
object AndroidVersionConstructorFingerprint : Fingerprint(
    definingClass = "Lcom/nousguide/android/orftvthek/feature/relaunch/legacy/shared/data/modelsTV/AndroidVersion;",
    name = "<init>",
    returnType = "V",
    filters = listOf(
        fieldAccess(
            opcode = Opcode.IPUT_BOOLEAN,
            definingClass = "this",
            name = "forced",
            type = "Z",
        ),
    ),
)
