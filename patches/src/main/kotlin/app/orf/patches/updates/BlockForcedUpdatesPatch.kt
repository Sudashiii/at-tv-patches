package app.orf.patches.updates

import app.morphe.patcher.patch.bytecodePatch
import app.orf.patches.shared.Constants.COMPATIBILITY_ORF_ON
import app.servus.patches.shared.storeFalseInConstructors
import com.android.tools.smali.dexlib2.iface.instruction.ReferenceInstruction
import com.android.tools.smali.dexlib2.iface.reference.FieldReference

@Suppress("unused")
val blockForcedUpdatesPatch = bytecodePatch(
    name = "Block forced updates",
    description = "Makes the 'Update required' dialog dismissible, so the patched app can't be locked out by a " +
        "forced update.",
    default = true,
) {
    compatibleWith(COMPATIBILITY_ORF_ON)

    // A patched build is signed with a different key and can never be updated through the Play Store, so a
    // forced update would lock it out for good. On start, both apps load a version list from the ORF API and
    // pick the AndroidVersion entry newer than the installed build. If that entry is forced, the update dialog
    // can't be cancelled and has no "later" button. Otherwise it is an optional update: shown at most once a day,
    // with "later", and cancellable.
    //
    // Forcing `forced` to false on construction turns every update into that optional one, a path the backend
    // already uses itself. The dialog still shows that an update exists. The apps don't use Google Play in-app
    // updates.
    execute {
        val forcedField = AndroidVersionConstructorFingerprint.instructionMatches.last()
            .getInstruction<ReferenceInstruction>().reference as FieldReference
        AndroidVersionConstructorFingerprint.classDef.storeFalseInConstructors(forcedField)
    }
}
