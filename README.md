# Quiz de Culture Générale

A modern Android application that tests users' general knowledge through an engaging quiz interface. This application is built using modern Android development practices and libraries.

## Features

- Interactive quiz interface
- Multiple choice questions
- Score tracking
- Beautiful animations using Lottie
- Offline support with Room database
- Modern Material Design UI
- Google Maps integration
- Image loading with Glide
- Network operations with Retrofit

## Technical Stack

- **Minimum SDK**: 29 (Android 10)
- **Target SDK**: 35
- **Language**: Java
- **Architecture**: MVVM (Model-View-ViewModel)

### Key Dependencies

- **AndroidX Core Libraries**
  - AppCompat
  - Material Design
  - ConstraintLayout
  - Activity

- **Room Database**
  - Version: 2.6.1
  - For local data persistence

- **Google Services**
  - Maps
  - Location Services

- **Image Loading**
  - Glide 4.12.0

- **Animation**
  - Lottie 6.1.0

- **Networking**
  - Retrofit 2.9.0
  - Gson 2.10.1
 
## Development Environment Setup

### IDE Requirements
- **Android Studio** (Recommended version: Arctic Fox or newer)
  - Download from: https://developer.android.com/studio
  - Required plugins:
    - Android SDK Tools
    - Android SDK Platform-Tools
    - Android Emulator
    - Android SDK Build-Tools

### API Keys Setup
1. **Acquire Google Maps API Key**:
   - Go to the [Google Cloud Console](https://console.cloud.google.com/)
   - Create a new project or select an existing one
   - Enable the Maps SDK for Android
   - Go to Credentials > Create Credentials > API Key
   - Copy your new API key

2. **Configure API Key in AndroidManifest.xml**:
   Add the following inside the `<application>` tag in `app/src/main/AndroidManifest.xml`:
   ```xml
   <meta-data
       android:name="com.google.android.geo.API_KEY"
       android:value="your_google_maps_api_key" />

## Getting Started

### Prerequisites

- Android Studio Arctic Fox or newer
- JDK 11
- Android SDK 29 or higher

### Installation

1. Clone the repository:
```bash
git clone [repository-url]
```

2. Open the project in Android Studio

3. Sync the project with Gradle files

4. Run the application on an emulator or physical device

## Building the Project

The project uses Gradle for building. You can build the project using:

```bash
./gradlew build
```

For a debug build:
```bash
./gradlew assembleDebug
```

For a release build:
```bash
./gradlew assembleRelease
```

## Testing

The project includes both unit tests and instrumentation tests:

- Unit tests using JUnit
- UI tests using Espresso
- Room database tests

Run tests using:
```bash
./gradlew test        # for unit tests
./gradlew connectedAndroidTest  # for instrumentation tests
```

## Contact

ouafae.lasri@gmail.com
elhazzatkaoutar@gmail.com
