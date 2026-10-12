# Pocket Tap Game (Android 9+)

A small offline Android game for learning app development. Tap the mint-green target as many times as possible in 20 seconds.

## What it includes
- Score counter and countdown timer
- Target appears in a random position after each tap
- Play-again button
- No internet access, accounts, payments, or Android permissions
- Android 9+ support (`minSdk 28`)

## Build an APK with GitHub Actions (no Android Studio required on your PC)
1. Create a new GitHub repository.
2. Upload the contents of this folder to the repository root. The `app` and `.github` folders must be at the root.
3. Commit/push to `main` or `master`.
4. Open the repository's **Actions** tab and select **Build Pocket Tap Game APK**.
5. Wait for the workflow to finish successfully.
6. Under the workflow run's **Artifacts**, download `PocketTapGame-debug-apk`.
7. Extract the ZIP. It contains `app-debug.apk`.

## Install on your Android phone
1. Transfer `app-debug.apk` to your phone.
2. Open it from your phone's Files app and tap Install.
3. If Android asks, allow installation from that file app temporarily. Only install APKs you built or trust.
4. After installation, turn off that file app's “Allow from this source” permission if you enabled it.

## Build locally
Install JDK 17, Android Studio/Android SDK, and Gradle. From this folder, run:

```bash
gradle assembleDebug
```

The APK is created at `app/build/outputs/apk/debug/app-debug.apk`.

## Beginner learning challenges
1. Change the game duration from 20 seconds to 30 seconds.
2. Change the target color.
3. Add a best-score display.
4. Add a difficulty mode where the target gets smaller.
5. Save the best score between app launches.
