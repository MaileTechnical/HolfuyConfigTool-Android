# Developer Workflow

This document describes the standard development and release workflow for
Holfuy Upgrader.

## Forks and Branching

Development is never performed directly on the [upstream repository](https://github.com/MaileTechnical/HolfuyConfigTool-Android).
Instead, each developer forks the upstream repository and operates on that fork.

Each logical change is developed on its own branch within the developer's fork.

Examples:

- `feature/...`
- `issue/...`
- `doc/...`
- `release/...`

Whenever possible, each new branch is created from a local `master` branch that has been synchronized with the current upstream `master` branch.

## Development

1. Open an [issue](https://github.com/MaileTechnical/HolfuyConfigTool-Android/issues) if necessary.  Each feature or defect must be covered by an issue.
2. Ensure the `master` branch of the developer fork is synchronized with the `master` branch in the upstream repository.
3. Update the local `master` branch from the `master` branch of the developer fork.
4. Create a new (local) development branch using the naming convention shown above, and check it out.  If an issue is associated with the branch, include the issue number in the branch name.
5. Implement and test the change, making semantically cohesive commits and pushing those commits to the developer fork.
6. Update the associated issue, storing a link to the branch along with an explanation of the work.
7. Open a pull request to pull the development branch into the upstream `master` branch.

## Servicing Pull Requests (maintainers)

1. Review the changes.
2. Request changes or clarification if necessary.
3. Merge the pull request.
4. Update the associated issue if there is one.

## Synchronizing Repositories

After merging:

1. Synchronize the `master` branch of the developer fork with the `master` branch of the upstream repository.
2. Pull the `master` branch of the developer fork into the local `master` branch.


## Documentation

Documentation changes are committed with the feature or bug fix they
describe whenever practical.

The repository maintains:

- README
- User's Guide
- Privacy Policy
- Test Plan
- CHANGELOG
- Release History
- Google Play assets

## Release Process

1. Create a release branch from `master`.
2. Merge each branch (feature, issue, documentation, etc.) targeted for this release into the newly created release branch.
3. Update:
   - `versionName`
   - `versionCode`
   - CHANGELOG
   - Release History
   - Google Play assets, as required
4. Build release APK from the release branch:

   ```bash
   ./gradlew clean assembleRelease
   ```
   
5. Install release APK:

   ```bash
   adb install -r app/build/outputs/apk/release/app-release.apk
   ```
   
6. Execute the release test plan on the release APK.
7. Build a signed Android App Bundle:

   ```bash
   ./gradlew clean bundleRelease
   ```

8. Create a Git tag (e.g., v1.0.0-rc1, v1.0.0, etc.) annotating it with `versionCode` and Play track.
9. Upload the bundle to the Google Play Console.
10. Upload updated Play Store assets, screenshots, and "What's New" text if necessary.
11. Submit the release for review by Play.
12. After approval from Play, install the app from the appropriate Play testing track and perform a brief acceptance test.
13. Promote the release to the next Play track if appropriate.

## Guiding Principles

- One logical change per branch.
- One authoritative copy of every artifact.
- Keep `master` releasable.
- Verify every release using the documented test plan.