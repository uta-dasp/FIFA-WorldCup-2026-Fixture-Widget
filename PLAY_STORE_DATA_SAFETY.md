# App Store Safety Notes

The app is designed to be simple to review for F-Droid and compatible with Google Play safety expectations.

## Data Collection

The app does not collect personal or sensitive user data.

## Data Sharing

The app does not share user data with third parties.

## Network Activity

The app connects once per day to GitHub raw content over HTTPS to download the public openfootball 2026 football tournament JSON schedule. The app does not upload user data.

## Permissions

- Internet access: used only to fetch the public schedule JSON.
- Network state: used by Android job scheduling for network-aware updates.
- Boot completed: used to reschedule the daily update job after device restart.

## Security Practices

- No ads
- No analytics SDK
- No account creation
- No location access
- No contacts access
- No camera or microphone access
- No device identifiers
- Cleartext traffic is disabled
- Backups are disabled

## Bundled Data

The 2026 football schedule is bundled from `openfootball/worldcup.json`, which is published under CC0-1.0 / public domain dedication.
