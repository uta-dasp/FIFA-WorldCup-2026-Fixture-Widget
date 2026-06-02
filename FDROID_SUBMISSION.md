# F-Droid Submission Notes

F-Droid normally builds and signs apps from source. Do not submit only an APK. Publish this source repository first, then submit metadata to F-Droid.

## Prepared Locally

This project includes:

- `fdroid/metadata/com.dasp.worldcup2026.yml`
- `fastlane/metadata/android/en-US/` listing text

A local fdroiddata checkout was also prepared at:

`C:\Users\dasp\Documents\New project\fdroiddata-submission`

Branch:

`add-com.dasp.worldcup2026`

`fdroid lint com.dasp.worldcup2026` passed locally. The only warnings were local environment warnings unrelated to the app metadata.

## Submission Path

1. Push this project to a public GitHub repository.
2. Create a tagged release, for example:

```powershell
git tag v0.1.0
git push origin main --tags
```

3. Create a GitLab account if you do not already have one.
4. Fork `https://gitlab.com/fdroid/fdroiddata`.
5. Push the prepared `add-com.dasp.worldcup2026` branch to your fdroiddata fork.
6. Open a merge request to F-Droid's fdroiddata repository.

Official docs:

- https://f-droid.org/docs/Submitting_to_F-Droid_Quick_Start_Guide/
- https://fdroid.gitlab.io/jekyll-fdroid/docs/Inclusion_How-To/

## Suggested Metadata Starting Point

Use this as a draft, then validate it with F-Droid tools before opening the merge request.

```yaml
Categories:
  - Sports & Health
License: MIT
AuthorName: uta-dasp
SourceCode: https://github.com/uta-dasp/FIFA-WorldCup-2026-Fixture-Widget
IssueTracker: https://github.com/uta-dasp/FIFA-WorldCup-2026-Fixture-Widget/issues

AutoName: 2026 Football Fixtures Widget

RepoType: git
Repo: https://github.com/uta-dasp/FIFA-WorldCup-2026-Fixture-Widget.git

Builds:
  - versionName: 0.1.0
    versionCode: 1
    commit: <full source commit hash>
    subdir: app
    gradle:
      - yes

AutoUpdateMode: Version
UpdateCheckMode: Tags
CurrentVersion: 0.1.0
CurrentVersionCode: 1
```

## Notes

- The release APK in this repository is for your local testing and GitHub Releases.
- F-Droid will build its own APK from source and sign it with F-Droid's key.
- Keep proprietary services and closed-source SDKs out of the project.
