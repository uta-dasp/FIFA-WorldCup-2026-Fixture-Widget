# FIFA WorldCup 2026 Fixture Widget

Native Android app for FIFA World Cup 2026 fixtures and home-screen widgets.

## Data Source

This app bundles the 2026 schedule from:

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
- No network permission
- No user data collection
- `minSdk 23` for Android 6.0+
- `targetSdk 35`

## Features

- Clean offline schedule UI
- All fixtures view
- Today view
- Results view, ready for future scored data updates
- Schedule home-screen widget
- Compact score/next-match home-screen widget
- Device-local time display based on the openfootball UTC offsets

## Build

Open this folder in Android Studio and build the debug APK or release app bundle.

If Gradle and the Android SDK are available locally:

```powershell
.\gradlew :app:assembleDebug
```

The generated debug APK is normally written to:

```text
app/build/outputs/apk/debug/app-debug.apk
```

## Install To A Phone

With USB debugging enabled and `adb` available:

```powershell
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

## Updating The Schedule

Replace:

```text
app/src/main/assets/worldcup_2026.json
```

with the latest raw JSON from openfootball, then rebuild the APK.
