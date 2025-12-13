# MediVault Mobile Application

## Overview
Cross-platform mobile application for MediVault Platform built with **Expo** and TypeScript.

## Features
- 📱 Cross-platform (Android & iOS & Web)
- 🔐 JWT Authentication with OAuth2 support
- 👥 Multi-patient management
- 📷 Prescription scanning and OCR
- 💾 Offline-first architecture
- 🔄 Automatic sync
- 📊 Medical history tracking
- 🔔 Push notifications

## Tech Stack
- **Expo SDK 54**
- React Native 0.76.3
- TypeScript
- Redux Toolkit (State Management)
- React Navigation (Navigation)
- AsyncStorage (Offline Storage)
- Axios (API Client)
- React Native Paper (UI Components)
- Expo Camera (Camera functionality)
- Expo Image Picker (Image selection)

## Project Structure
```
src/
├── screens/          # Application screens
├── components/       # Reusable components
├── navigation/       # Navigation configuration
├── store/           # Redux store and slices
├── services/        # API and services
├── types/           # TypeScript type definitions
├── constants/       # Constants and configuration
└── utils/           # Utility functions
```

## Setup Instructions

### Prerequisites
- Node.js 18+
- npm or yarn
- Expo CLI: `npm install -g expo-cli`
- Expo Go app on your mobile device (for development)
- **No need for Android Studio or Xcode for development!**

### Installation
```bash
# Install dependencies
npm install

# Start Expo development server
npm start
```

### Running the App

**Development with Expo Go (Easiest)**
```bash
# Start Expo
npm start

# Scan QR code with:
# - Expo Go app (Android)
# - Camera app (iOS)
```

**Platform-Specific Development**
```bash
# Android (with Android emulator or device)
npm run android

# iOS (with iOS simulator - macOS only)
npm run ios

# Web browser
npm run web
```

**Building for Production**
```bash
# Build for Android
expo build:android

# Build for iOS (requires Apple Developer account)
expo build:ios
```

## API Configuration
Update the API_BASE_URL in `src/constants/index.ts`:
```typescript
export const API_BASE_URL = 'http://YOUR_SERVER_URL:8080/api';
```

For local development with Expo:
- Android emulator: `http://10.0.2.2:8080/api`
- iOS simulator: `http://localhost:8080/api`
- Physical device: `http://YOUR_COMPUTER_IP:8080/api`

## Screens Implemented
1. ✅ Splash Screen - App initialization
2. ✅ Login/Register Screen - Authentication
3. ⏳ Role Selection Screen
4. ✅ Home Dashboard - Main dashboard
5. ✅ Patient List Screen - Patient management
6. ✅ Add Patient Screen - Comprehensive form
7. ⏳ Edit Patient Screen
8. ⏳ Scan Prescription Screen (Expo Camera integration ready)
9. ⏳ Image Preview Screen
10. ⏳ Upload & OCR Processing Screen
11. ⏳ Extracted Prescription Data Screen
12. ⏳ Medical Analysis Screen
13. ⏳ Medical History Screen
14. ⏳ Prescription Details Screen
15. ⏳ Notification Screen
16. ⏳ Profile & Settings Screen
17. ⏳ Security & Privacy Screen
18. ⏳ Error/Empty States

## Key Features Implementation

### Authentication
- JWT token-based authentication
- OAuth2 support (Google/Apple)
- Secure token storage using AsyncStorage
- Auto-login on app launch

### State Management
- Redux Toolkit for global state
- Separate slices for auth, patient, prescription
- Async thunks for API calls
- Error handling

### Offline Support
- Automatic data sync when online
- Queue for failed operations
- Local data persistence

## API Integration
All API calls are handled through the API service (`src/services/api.ts`):
- Auth APIs (login, register, OAuth)
- Patient APIs (CRUD operations)
- Prescription APIs (upload, fetch, update)

## Expo Advantages
- ✅ **No Native Build Tools Required**: No need for Android Studio or Xcode during development
- ✅ **Cross-Platform**: Develop for iOS on Windows/Linux
- ✅ **Live Reload**: Instant updates on device
- ✅ **Easy Setup**: Get started in minutes
- ✅ **OTA Updates**: Push updates without app store approval
- ✅ **Built-in Camera/ImagePicker**: No complex native setup

## Camera & Image Features
Using Expo's managed workflow:
- `expo-camera`: Camera functionality for prescription scanning
- `expo-image-picker`: Gallery access for uploading existing images
- Permissions handled automatically by Expo

## Troubleshooting

**"TurboModuleRegistry" or "PlatformConstants could not be found"**
1. Delete node_modules: `rm -rf node_modules`
2. Clear npm cache: `npm cache clean --force`
3. Reinstall dependencies: `npm install`
4. Clear Expo cache: `expo start -c`
5. If still failing, ensure React Native version matches Expo SDK (0.76.3 for SDK 54)

**"Unable to resolve module"**
- Run `npm install` again
- Clear cache: `expo start -c`
- Delete node_modules and reinstall

**"Network request failed"**
- Check API_BASE_URL configuration
- Ensure backend is running
- Use correct IP address for physical devices

**"Asset resolution failed"**
- Ensure all assets exist in the `assets/` folder
- Restart Metro bundler: `expo start -c`

**Camera not working**
- Grant camera permissions when prompted
- Check `app.json` for camera plugin configuration

**App won't start in Expo Go**
- Ensure Expo Go version matches SDK version (SDK 54)
- Restart Expo server: `expo start -c`
- Update Expo Go app from App Store/Play Store

## Dependencies
See `package.json` for full list of dependencies including:
- expo (~54.0.0)
- react-native (0.76.3 - exact version for SDK 54 compatibility)
- @reduxjs/toolkit
- react-navigation
- react-native-paper
- axios
- @react-native-async-storage/async-storage

## Development Notes
- TypeScript for type safety
- Component-based architecture
- Modular and scalable structure
- Expo managed workflow for easier development

## Future Enhancements
- Real-time OCR processing with expo-camera
- Push notifications with Expo Notifications
- Biometric authentication with expo-local-authentication
- Dark mode support
- Internationalization (i18n)
- Offline-first data sync

## License
MIT
