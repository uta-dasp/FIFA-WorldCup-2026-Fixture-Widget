# F-Droid Submission Notes

F-Droid normally builds and signs apps from source. Do not submit only an APK. Publish this source repository first, then submit metadata to F-Droid.

## Recommended Path

1. Push this project to a public GitHub repository.
2. Create a tagged release, for example:

```powershell
git tag v0.1.0
git push origin main --tags
```

3. Create a GitLab account if you do not already have one.
4. Fork `https://gitlab.com/fdroid/fdroiddata`.
5. Add metadata for this app in your fdroiddata fork.
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

AutoName: FIFA WorldCup 2026 Fixture Widget
Summary: FIFA World Cup 2026 fixtures and home-screen widgets
Description: |-
  FIFA WorldCup 2026 Fixture Widget shows the 2026 World Cup fixture list
  with home-screen widgets. It updates once per day from the public
  openfootball/worldcup.json schedule and falls back to bundled CC0 data.

RepoType: git
Repo: https://github.com/uta-dasp/FIFA-WorldCup-2026-Fixture-Widget.git

Builds:
  - versionName: 0.1.0
    versionCode: 1
    commit: v0.1.0
    subdir: .
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
