# MediVault Platform - Implementation Guide

## Overview
Complete implementation of the MediVault Platform including backend API and mobile application.

## Architecture

### Backend (Spring Boot)
```
backend/
├── src/main/java/com/app/medivault/
│   ├── controller/          # REST API Controllers
│   │   ├── AuthController.java
│   │   ├── PatientController.java
│   │   ├── PrescriptionController.java
│   │   └── NotificationController.java
│   ├── service/            # Business Logic Layer
│   │   ├── AuthService.java
│   │   ├── PatientService.java
│   │   ├── PrescriptionService.java
│   │   ├── GoogleVisionOcrService.java
│   │   └── PrescriptionTextParser.java
│   ├── repository/         # Data Access Layer
│   │   ├── UserRepository.java
│   │   ├── PatientRepository.java
│   │   ├── PrescriptionRepository.java
│   │   └── MedicineRepository.java
│   ├── entity/            # JPA Entities
│   │   ├── User.java
│   │   ├── Patient.java
│   │   ├── Prescription.java
│   │   ├── Medicine.java
│   │   └── [Enums...]
│   ├── dtos/             # Data Transfer Objects
│   │   ├── LoginRequest.java
│   │   ├── RegisterRequest.java
│   │   ├── AuthResponse.java
│   │   ├── PatientRequest.java
│   │   ├── PatientResponse.java
│   │   ├── PrescriptionRequest.java
│   │   └── PrescriptionResponse.java
│   ├── config/           # Configuration
│   │   ├── SecurityConfig.java
│   │   └── FirebaseConfig.java
│   └── util/            # Utilities
│       └── JwtUtil.java
└── src/main/resources/
    └── application.properties
```

### Mobile App (React Native + TypeScript)
```
medical-patient-app/
├── src/
│   ├── screens/         # Application Screens
│   │   ├── SplashScreen.tsx
│   │   ├── LoginScreen.tsx
│   │   ├── HomeScreen.tsx
│   │   ├── PatientListScreen.tsx
│   │   └── AddPatientScreen.tsx
│   ├── navigation/      # Navigation Setup
│   │   └── AppNavigator.tsx
│   ├── store/          # Redux State Management
│   │   ├── index.ts
│   │   ├── rootReducer.ts
│   │   └── slices/
│   │       ├── authSlice.ts
│   │       ├── patientSlice.ts
│   │       └── prescriptionSlice.ts
│   ├── services/       # API & Services
│   │   ├── api.ts
│   │   ├── networkService.ts
│   │   └── offlineSyncService.ts
│   ├── types/         # TypeScript Types
│   │   └── index.ts
│   └── constants/     # App Constants
│       └── index.ts
└── App.tsx
```

## Features Implemented

### Backend Features
- ✅ **Authentication & Authorization**
  - JWT token-based authentication
  - OAuth2 support (Google/Apple)
  - BCrypt password encryption
  - Role-based access control

- ✅ **User Management**
  - User registration with email/password
  - OAuth login integration
  - User roles (Patient, Doctor, Clinic Admin)

- ✅ **Patient Management**
  - Create, Read, Update, Delete patients
  - Multi-patient support per user
  - Patient demographics (name, DOB, gender, blood group)
  - Allergy tracking
  - Relationship tracking (self, family members)

- ✅ **Prescription Management**
  - Upload prescription images
  - Idempotent upload (duplicate prevention via uploadId)
  - OCR integration (Google Vision)
  - Medicine extraction and parsing
  - Medical analysis status tracking
  - Prescription history

- ✅ **Database**
  - H2 in-memory database (development)
  - JPA/Hibernate ORM
  - Entity relationships
  - Audit timestamps

- ✅ **Security**
  - Spring Security configuration
  - CORS enabled for cross-origin requests
  - JWT validation
  - Stateless session management

### Mobile App Features
- ✅ **Authentication**
  - Login/Register screens
  - JWT token storage
  - Auto-login on app start
  - OAuth integration ready

- ✅ **Navigation**
  - Stack navigation for screens
  - Tab navigation for main sections
  - Deep linking ready

- ✅ **Patient Management**
  - List all patients
  - Add new patient with comprehensive form
  - Edit patient details
  - Patient selector for multi-patient families

- ✅ **State Management**
  - Redux Toolkit for global state
  - Separate slices for different features
  - Async thunks for API calls
  - Error handling

- ✅ **Offline Support**
  - AsyncStorage for local persistence
  - Offline queue architecture
  - Network state detection

## API Endpoints

### Authentication
- `POST /api/auth/register` - Register new user
- `POST /api/auth/login` - Login with email/password
- `POST /api/auth/oauth` - OAuth login (Google/Apple)

### Patients
- `GET /api/patients` - Get all patients for user
- `POST /api/patients` - Create new patient
- `GET /api/patients/{id}` - Get patient details
- `PUT /api/patients/{id}` - Update patient
- `DELETE /api/patients/{id}` - Delete patient

### Prescriptions
- `POST /api/prescriptions/upload` - Upload prescription image
- `GET /api/prescriptions/patient/{patientId}` - Get prescriptions for patient
- `GET /api/prescriptions/{id}` - Get prescription details
- `PUT /api/prescriptions/{id}` - Update prescription
- `DELETE /api/prescriptions/{id}` - Delete prescription

## Setup & Running

### Backend Setup
```bash
cd backend

# Compile
mvn clean compile

# Run tests
mvn test

# Package
mvn clean package

# Run application
java -jar target/medivault-0.0.1-SNAPSHOT.jar

# Or using Maven
mvn spring-boot:run
```

Backend runs on `http://localhost:8080`

### Mobile App Setup
```bash
cd medical-patient-app

# Install dependencies
npm install

# Run on Android
npm run android

# Run on iOS
npm run ios

# Start Metro bundler
npm start
```

## Configuration

### Backend Configuration (`application.properties`)
```properties
# Database
spring.datasource.url=jdbc:h2:mem:medivaultdb
spring.jpa.hibernate.ddl-auto=create-drop

# JWT
jwt.secret=MediVaultSecretKeyForJWTTokenGenerationAndValidation2024
jwt.expiration=86400000

# File Upload
spring.servlet.multipart.max-file-size=10MB

# Server
server.port=8080
```

### Mobile App Configuration
Update `API_BASE_URL` in `src/constants/index.ts`:
```typescript
export const API_BASE_URL = 'http://localhost:8080/api';
```

## Database Schema

### Users Table
- id (Primary Key)
- email (Unique)
- password
- name
- phone
- role (PATIENT, DOCTOR, CLINIC_ADMIN)
- oauth_provider
- oauth_id
- enabled
- created_at
- updated_at

### Patients Table
- id (Primary Key)
- name
- dob
- gender (MALE, FEMALE, OTHER)
- blood_group
- allergies
- relationship
- user_id (Foreign Key)
- created_at
- updated_at

### Prescriptions Table
- id (Primary Key)
- upload_id (Unique - for idempotency)
- image_url
- patient_id (Foreign Key)
- doctor_name
- hospital_name
- prescription_date
- diagnosis
- raw_ocr_text
- analysis_status
- risk_level
- alerts
- created_at
- updated_at

### Medicines Table
- id (Primary Key)
- name
- dosage
- frequency
- duration
- prescription_id (Foreign Key)

## Security

### Authentication Flow
1. User registers or logs in
2. Backend validates credentials
3. JWT token generated and returned
4. Mobile app stores token in AsyncStorage
5. Token included in Authorization header for all API calls
6. Backend validates token on each request

### Data Security
- Passwords encrypted with BCrypt
- JWT tokens for stateless authentication
- HTTPS recommended for production
- Field-level encryption ready for sensitive data

## Testing

### Backend Tests
```bash
cd backend
mvn test
```

### Manual API Testing
Use tools like Postman or curl:
```bash
# Register
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","password":"password123","name":"Test User","role":"PATIENT"}'

# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","password":"password123"}'
```

## Production Deployment

### Backend
1. Change H2 to PostgreSQL/MySQL
2. Configure production database credentials
3. Use environment variables for sensitive data
4. Enable HTTPS
5. Configure proper CORS origins
6. Set up cloud storage for prescription images
7. Configure Google Vision API credentials

### Mobile App
1. Configure production API URL
2. Enable ProGuard (Android) and code optimization
3. Set up push notification certificates
4. Configure OAuth credentials for production
5. Enable analytics and crash reporting

## Future Enhancements
- [ ] Real-time OCR processing
- [ ] Drug interaction detection
- [ ] Allergy conflict warnings
- [ ] Medicine reminders
- [ ] PDF generation and sharing
- [ ] Doctor portal
- [ ] Clinic admin dashboard
- [ ] Analytics and reporting
- [ ] Biometric authentication
- [ ] Dark mode
- [ ] Internationalization

## License
MIT

## Support
For issues and questions, please create an issue in the repository.
