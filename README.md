# 2026 Football Fixtures Widget

Native Android app for 2026 international football fixtures and home-screen widgets.

## Data Source

This app updates once per day from:

`https://raw.githubusercontent.com/openfootball/worldcup.json/master/2026/worldcup.json`

It also bundles a fallback copy of the 2026 schedule from:

`openfootball/worldcup.json`

Raw data file used:

`https://raw.githubusercontent.com/openfootball/worldcup.json/master/2026/worldcup.json`

The openfootball project publishes its schema, data, and scripts under CC0-1.0 / public domain dedication.

## F-Droid Friendly Shape

- App license: MIT, see `LICENSE`
- Bundled data: CC0-1.0, see `THIRD_PARTY_NOTICES.md`
- Java source only
- No proprietary SDKs
- No analytics
- No ads
- No account system
- No user data collection
- Network access is only used to fetch the public openfootball JSON schedule once per day
- `minSdk 23` for Android 6.0+
- `targetSdk 35`

This is an unofficial fan app. It is not affiliated with, endorsed by, or sponsored by FIFA, the tournament organizers, or any football federation.

## Features

- Clean schedule UI
- Daily online refresh from openfootball
- Bundled fallback schedule when offline
- All fixtures view
- Today view
- Results view, ready for future scored data updates
- Schedule home-screen widget
- Compact score/next-match home-screen widget
- Device-local time display based on the openfootball UTC offsets

## Build

Open this folder in Android Studio and build the debug or release APK.

If Gradle and the Android SDK are available locally:

```powershell
.\gradlew :app:assembleRelease
```

The unsigned release APK is normally written to:

```text
app/build/outputs/apk/release/app-release-unsigned.apk
```

## Install To A Phone

With USB debugging enabled and `adb` available:

```powershell
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

## Updating The Schedule

Replace the bundled fallback:

```text
app/src/main/assets/worldcup_2026.json
```

with the latest raw JSON from openfootball, then rebuild the APK. The app will still try to update from the online JSON once per day at runtime.
