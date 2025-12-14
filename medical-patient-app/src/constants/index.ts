export const API_BASE_URL = 'http://localhost:8080/api';

export const COLORS = {
  primary: '#2196F3',
  secondary: '#03A9F4',
  success: '#4CAF50',
  warning: '#FF9800',
  error: '#F44336',
  background: '#F5F5F5',
  white: '#FFFFFF',
  black: '#000000',
  gray: '#9E9E9E',
  lightGray: '#E0E0E0',
};

export const GENDER_OPTIONS = [
  { label: 'Male', value: 'MALE' },
  { label: 'Female', value: 'FEMALE' },
  { label: 'Other', value: 'OTHER' },
];

export const BLOOD_GROUP_OPTIONS = [
  'A+', 'A-', 'B+', 'B-', 'AB+', 'AB-', 'O+', 'O-'
];

export const RELATIONSHIP_OPTIONS = [
  'Self', 'Spouse', 'Father', 'Mother', 'Son', 'Daughter', 'Brother', 'Sister', 'Other'
];

export const ROLE_OPTIONS = [
  { label: 'Patient', value: 'PATIENT' },
  { label: 'Doctor', value: 'DOCTOR' },
  { label: 'Clinic Admin', value: 'CLINIC_ADMIN' },
];
