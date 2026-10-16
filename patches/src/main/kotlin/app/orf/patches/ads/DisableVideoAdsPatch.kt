package app.orf.patches.ads

import app.morphe.patcher.extensions.InstructionExtensions.replaceInstruction
import app.morphe.patcher.patch.PatchException
import app.morphe.patcher.patch.bytecodePatch
import app.orf.patches.shared.Constants.COMPATIBILITY_ORF_ON
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.iface.ClassDef
import com.android.tools.smali.dexlib2.iface.instruction.Instruction
import com.android.tools.smali.dexlib2.iface.instruction.ReferenceInstruction
import com.android.tools.smali.dexlib2.iface.instruction.TwoRegisterInstruction
import com.android.tools.smali.dexlib2.iface.reference.FieldReference

/**
 * The API's per-video "show_instream_ads" flag, under the names the app's models use for it:
 * `Episode`, `Livestream` and `LiveStream` (`showInstreamAds:Ljava/lang/Boolean;`), the TV app's `TVthekEpisode`,
 * `TVthekLivestream` and `TVthekSegment` (`showInstreamAds:Z`), and `TimeshiftLivestream` (`show_instream_ads:Z`).
 * The models are (de)serialized by Moshi, so their class and field names are not obfuscated.
 */
private val SHOW_INSTREAM_ADS_FIELDS = setOf("showInstreamAds", "show_instream_ads")

private fun Instruction.isShowInstreamAdsRead() =
    (opcode == Opcode.IGET_BOOLEAN || opcode == Opcode.IGET_OBJECT) &&
        (this as ReferenceInstruction).reference.let {
            it is FieldReference && it.name in SHOW_INSTREAM_ADS_FIELDS &&
                (it.type == "Z" || it.type == "Ljava/lang/Boolean;")
        }

@Suppress("unused")
val disableVideoAdsPatch = bytecodePatch(
    name = "Disable video ads",
    description = "Removes pre-roll and post-roll ads from videos, live channels and timeshift.",
    default = true,
) {
    compatibleWith(COMPATIBILITY_ORF_ON)

    // Every episode, livestream and timeshift stream from the API carries show_instream_ads. Content without ads
    // has it set to false, and every ad path checks it before building an ad URL:
    //
    // - Phone app: the players' shouldShowAds checks (episode or livestream showInstreamAds, plus the Austria-only
    //   rule) gate the preroll and postroll URLs; the legacy VOD player and the timeshift player check
    //   showInstreamAds / show_instream_ads themselves before scheduling an ad. ORF's own channel intro clips
    //   (video bumpers) are played through the same ad slot but are not ads and are not affected.
    // - TV app: the video loader logs "Don't add ad rolls" and uses empty AdRolls when showInstreamAds is false,
    //   for VOD and for live/timeshift.
    //
    // So every read of the flag returns false, which is the path the app takes for content without ads. No ad URL
    // is built, nothing is scheduled on the Bitmovin player, and the content starts directly.
    execute {
        val classes = mutableListOf<ClassDef>()
        classDefForEach { classDef ->
            if (classDef.methods.any { method ->
                    method.implementation?.instructions?.any { it.isShowInstreamAdsRead() } == true
                }
            ) {
                classes += classDef
            }
        }

        var patched = 0
        classes.forEach { classDef ->
            mutableClassDefBy(classDef).methods.forEach { method ->
                val reads = method.implementation?.instructions?.withIndex()
                    ?.filter { (_, instruction) -> instruction.isShowInstreamAdsRead() } ?: return@forEach

                // Same instruction count either way, so the indices stay valid.
                reads.forEach { (index, instruction) ->
                    // iget-boolean vA, vB, showInstreamAds:Z  ->  const/4 vA, 0x0
                    // iget-object vA, vB, showInstreamAds:Ljava/lang/Boolean;  ->  sget-object vA, Boolean.FALSE
                    // iget takes only v0-v15 as vA, so const/4 always fits.
                    val register = (instruction as TwoRegisterInstruction).registerA
                    method.replaceInstruction(
                        index,
                        if (instruction.opcode == Opcode.IGET_BOOLEAN) {
                            "const/4 v$register, 0x0"
                        } else {
                            "sget-object v$register, Ljava/lang/Boolean;->FALSE:Ljava/lang/Boolean;"
                        },
                    )
                    patched++
                }
            }
        }

        if (patched == 0) throw PatchException("No reads of ${SHOW_INSTREAM_ADS_FIELDS.joinToString("/")} found")
    }
}
