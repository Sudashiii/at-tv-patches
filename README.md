# 🧩 AT TV Patches (unofficial)

Morphe patches for Austrian TV apps: ServusTV On and ORF ON.

## ❓ About

Unofficial patches for, on phones and Android TV:

- [ServusTV On](https://play.google.com/store/apps/details?id=com.mautilus.servus) (`com.mautilus.servus`)
  - **Disable video ads**: removes the pre-roll and mid-roll ads (Google IMA) from videos and live channels.
- [ORF ON](https://play.google.com/store/apps/details?id=com.nousguide.android.orftvthek) (`com.nousguide.android.orftvthek`)
  - **Disable video ads**: removes the pre-roll and post-roll ads from videos, live channels and timeshift.

### How to use these patches

Click here to add these patches to Morphe: https://morphe.software/add-source?github=Sudashiii/at-tv-patches

## 🩹 Patches list

<!-- PATCHES_START EXPANDED -->
> **[v1.0.0-dev.5](https://github.com/Sudashiii/at-tv-patches/releases/tag/v1.0.0-dev.5)**&nbsp;&nbsp;•&nbsp;&nbsp;`dev`&nbsp;&nbsp;•&nbsp;&nbsp;9 patches total
<details open>
<summary>📦 ORF ON&nbsp;&nbsp;•&nbsp;&nbsp;4 patches</summary>
<br>

**🎯 Supported versions:**

| 6.13.5-mobile | 6.14.15-tv |
| :---: | :---: |

| 💊&nbsp;Patch | 📜&nbsp;Description | ⚙️&nbsp;Options |
|----------|----------------|-----------|
| [Block forced updates](#block-forced-updates) | Makes the 'Update required' dialog dismissible, so the patched app can't be locked out by a forced update. |  |
| [Disable tracking](#disable-tracking) | Stops ÖWA/INFOnline reach measurement, GfK video measurement, Bitmovin player analytics and Sentry crash reporting from starting. |  |
| [Disable video ads](#disable-video-ads) | Removes pre-roll and post-roll ads from videos, live channels and timeshift. |  |
| [Reject cookie banner](#reject-cookie-banner) | Skips the cookie consent banner and saves 'Disagree to all' instead, as if you declined it. The privacy settings in the app stay available. |  |

</details>

<details open>
<summary>📦 ServusTV On&nbsp;&nbsp;•&nbsp;&nbsp;5 patches</summary>
<br>

**🎯 Supported versions:**

| 7.5.2.0 |
| :---: |

| 💊&nbsp;Patch | 📜&nbsp;Description | ⚙️&nbsp;Options |
|----------|----------------|-----------|
| [Block forced updates](#block-forced-updates) | Disables Google Play in-app update prompts and makes 'Update required' messages dismissible, so the patched app can't be locked out by a forced update. |  |
| [Disable review prompts](#disable-review-prompts) | Stops the app from asking for a Play Store rating after watching videos or adding favorites. |  |
| [Disable tracking](#disable-tracking) | Makes the app behave as if all tracking consent was declined, so Braze, Datadog, Red Bull analytics, GfK and Datazoom never start and Crashlytics and Firebase Analytics don't collect data. |  |
| [Disable video ads](#disable-video-ads) | Removes pre-roll and mid-roll ads (Google IMA) from videos and live channels. |  |
| [Reject cookie banner](#reject-cookie-banner) | Skips the cookie consent banner on first launch and saves 'Reject all' instead, as if you declined it. The privacy settings in the app stay available. |  |

</details>

<!-- PATCHES_END -->

### 🛠️ Building locally

- Run `./gradlew buildAndroid`
- The built patches .mpp file is found in `patches/build/libs/patches-*.mpp`
- Patch the mpp file using [Morphe-Desktop](https://github.com/MorpheApp/morphe-desktop)
  like any other patch bundle.

See the [Morphe documentation](https://github.com/MorpheApp/morphe-documentation) for more information.

## ⚖️ Disclaimer

- This is an unofficial, independent, non-commercial project. It is **not affiliated with, endorsed, or sponsored by
  ServusTV, Red Bull Media House, ORF (Österreichischer Rundfunk), or the developers of the ServusTV On or ORF ON
  apps**, nor by the Morphe project. "ServusTV", "ServusTV On", "ORF" and "ORF ON" are trademarks of their
  respective owners and are used here only to describe which apps these patches work with.
- This repository **does not contain or distribute any code, APKs, assets, or decompiled sources of ServusTV On or
  ORF ON**, and no modified versions of the apps are published. It only contains patch instructions, which users apply
  themselves to their own copy of the app.
- The patches **do not circumvent DRM** (Widevine) or any other technical protection measure, and do not unlock
  paid or geo-restricted content.
- Use at your own risk and responsibility. Modifying the app may violate its terms of use. The software is provided
  "as is", without warranty of any kind (see the license).
- Rights holders with concerns can [open an issue](https://github.com/Sudashiii/at-tv-patches/issues).

## 📜 License

AT TV Patches are licensed under the [GNU General Public License v3.0](LICENSE)
