# MediVault Mobile Application

## Overview
Cross-platform mobile application for MediVault Platform built with React Native and TypeScript.

## Features
- 📱 Cross-platform (Android & iOS)
- 🔐 JWT Authentication with OAuth2 support
- 👥 Multi-patient management
- 📷 Prescription scanning and OCR
- 💾 Offline-first architecture
- 🔄 Automatic sync
- 📊 Medical history tracking
- 🔔 Push notifications

## Tech Stack
- React Native 0.74
- TypeScript
- Redux Toolkit (State Management)
- React Navigation (Navigation)
- AsyncStorage (Offline Storage)
- Axios (API Client)
- React Native Paper (UI Components)

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
- React Native CLI
- Android Studio (for Android)
- Xcode (for iOS)

### Installation
```bash
# Install dependencies
npm install

# Install iOS pods (macOS only)
cd ios && pod install && cd ..
```

### Running the App
```bash
# Start Metro bundler
npm start

# Run on Android
npm run android

# Run on iOS
npm run ios
```

## API Configuration
Update the API_BASE_URL in `src/constants/index.ts`:
```typescript
export const API_BASE_URL = 'http://YOUR_SERVER_URL:8080/api';
```

## Screens Implemented
1. ✅ Splash Screen - App initialization
2. ✅ Login/Register Screen - Authentication
3. ⏳ Role Selection Screen
4. ✅ Home Dashboard - Main dashboard
5. ✅ Patient List Screen - Patient management
6. ⏳ Add/Edit Patient Screen
7. ⏳ Scan Prescription Screen
8. ⏳ Image Preview Screen
9. ⏳ Upload & OCR Processing Screen
10. ⏳ Extracted Prescription Data Screen
11. ⏳ Medical Analysis Screen
12. ⏳ Medical History Screen
13. ⏳ Prescription Details Screen
14. ⏳ Notification Screen
15. ⏳ Profile & Settings Screen
16. ⏳ Security & Privacy Screen
17. ⏳ Error/Empty States

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

## Dependencies
See `package.json` for full list of dependencies including:
- @reduxjs/toolkit
- react-navigation
- react-native-paper
- axios
- @react-native-async-storage/async-storage

## Development Notes
- TypeScript for type safety
- ESLint for code quality
- Component-based architecture
- Modular and scalable structure

## Future Enhancements
- Camera integration for prescription scanning
- Real-time OCR processing
- Push notifications
- Biometric authentication
- Dark mode support
- Internationalization (i18n)

## License
MIT
