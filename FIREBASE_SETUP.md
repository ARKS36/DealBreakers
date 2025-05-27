# Firebase Setup Instructions

Follow these steps to complete the Firebase setup for your app:

## 1. Create a Firebase Project

1. Go to the [Firebase Console](https://console.firebase.google.com/)
2. Click on "Add project" and follow the setup wizard
3. Enter a project name (e.g., "DealBreakers App")
4. Choose whether to enable Google Analytics (recommended)
5. Accept the terms and create the project

## 2. Register your Android App

1. In the Firebase console, click on the Android icon to add an Android app
2. Enter your package name: `com.example.appdeal`
3. Enter a nickname for your app (optional)
4. Enter your SHA-1 signing certificate (needed for some Firebase services like Authentication)
   - You can get this by running: `./gradlew signingReport` in your project directory
5. Click "Register app"

## 3. Download and Add Configuration File

1. Download the `google-services.json` file
2. Replace the placeholder file in your project at `app/google-services.json` with this downloaded file

## 4. Enable Firebase Services

### Authentication

1. In the Firebase console, go to "Authentication"
2. Click "Get started"
3. Enable the "Email/Password" sign-in method
4. Optionally enable other sign-in methods like Google, Facebook, etc.

### Firestore Database

1. Go to "Firestore Database"
2. Click "Create database"
3. Choose "Start in production mode" or "Start in test mode" (for development)
4. Select a location for your database
5. Click "Enable"

### Storage

1. Go to "Storage"
2. Click "Get started"
3. Review and accept the default security rules (or customize them)
4. Select a location for your storage bucket
5. Click "Done"

## 5. Security Rules

For development purposes, you may want to set up less restrictive security rules. Here are some examples:

### Firestore Rules

```
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /{document=**} {
      allow read, write: if request.auth != null;
    }
  }
}
```

### Storage Rules

```
rules_version = '2';
service firebase.storage {
  match /b/{bucket}/o {
    match /{allPaths=**} {
      allow read, write: if request.auth != null;
    }
  }
}
```

## 6. Test Your Setup

After completing the setup, run your app to verify that the Firebase connection is working correctly. You should see successful initialization in the logs and be able to use Firebase services.

## Troubleshooting

- If you encounter build errors, make sure the Firebase SDK versions are compatible with your app's other dependencies
- Check that your `google-services.json` file is correctly placed in the app directory
- Verify that you've added the required permissions in your AndroidManifest.xml
- Ensure your app has internet connectivity
- Check the Android Studio logcat for Firebase-specific error messages 