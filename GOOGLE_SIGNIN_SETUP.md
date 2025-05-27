# Setting up Google Sign-in with Firebase

Follow these steps to enable Google Sign-in in your Firebase application:

## 1. Enable Google Sign-in in Firebase Console

1. Go to the [Firebase Console](https://console.firebase.google.com/) and select your project
2. In the left sidebar, click on "Authentication"
3. Go to the "Sign-in method" tab
4. Click on "Google" and enable it
5. Enter your project name if prompted
6. Save your changes

## 2. Get the Web Client ID

After enabling Google Sign-in, you need to get your Web Client ID:

1. In the Firebase Console, go to "Authentication" > "Sign-in method"
2. Find "Google" in the list and click on the pencil icon to edit
3. In the "Web SDK configuration" section, you'll see your "Web client ID"
4. Copy this ID

## 3. Update the Code

1. Open `app/src/main/java/com/example/appdeal/data/FirebaseManager.kt`
2. Find the `getGoogleSignInClient` method
3. Replace `YOUR_WEB_CLIENT_ID` with the actual Web Client ID you copied:

```kotlin
val webClientId = "123456789012-abcdefghijklmnopqrstuvwxyz.apps.googleusercontent.com" // Replace with your actual Web Client ID
```

## 4. Configure SHA Certificate Fingerprints

For Google Sign-in to work properly, you need to add your app's SHA-1 fingerprint to Firebase:

### For Debug Builds:

1. Open a terminal/command prompt
2. Navigate to your project directory
3. Run this command:
   - Windows: `gradlew signingReport`
   - Mac/Linux: `./gradlew signingReport`
4. Look for the "SHA1" value under "Variant: debug"
5. Copy this SHA1 fingerprint

### For Release Builds:

If you have a release keystore, use this command instead:
```
keytool -list -v -keystore your-release-keystore.jks
```

### Adding SHA-1 to Firebase:

1. Go to the Firebase Console
2. Select your project
3. Click the gear icon next to "Project Overview" and select "Project settings"
4. Go to the "General" tab
5. Scroll down to "Your apps" section and select your Android app
6. Click "Add fingerprint"
7. Paste your SHA-1 fingerprint and click "Save"

## 5. Test Google Sign-in

1. Build and run your app
2. On the login screen, tap "Continue with Google"
3. You should see the Google account picker dialog
4. Select an account to sign in

## Troubleshooting

If you encounter issues:

1. **"10: Error" when trying to sign in**: Check that your SHA-1 fingerprint is correctly added to Firebase
2. **"Developer Error" message**: Verify your Web Client ID is correct and that Google Sign-in is enabled in Firebase
3. **App crashes during Google Sign-in**: Make sure you've added all required dependencies and that your `google-services.json` file is up to date

Remember to update the Web Client ID for both debug and release builds if needed. 