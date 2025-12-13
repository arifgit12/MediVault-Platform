export interface User {
  id: number;
  email: string;
  name: string;
  role: string;
  phone?: string;
}

export interface Patient {
  id: number;
  name: string;
  dob: string;
  gender: 'MALE' | 'FEMALE' | 'OTHER';
  bloodGroup?: string;
  allergies?: string;
  relationship?: string;
  age: number;
  createdAt: string;
}

export interface Medicine {
  id?: number;
  name: string;
  dosage: string;
  frequency: string;
  duration: string;
}

export interface Prescription {
  id: number;
  uploadId: string;
  imageUrl: string;
  patientId: number;
  patientName: string;
  doctorName?: string;
  hospitalName?: string;
  prescriptionDate?: string;
  diagnosis?: string;
  medicines: Medicine[];
  analysisStatus: string;
  riskLevel?: string;
  alerts?: string;
  createdAt: string;
  updatedAt: string;
}

export interface AuthResponse {
  token: string;
  type: string;
  userId: number;
  email: string;
  name: string;
  role: string;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface RegisterRequest {
  email: string;
  password: string;
  name: string;
  phone?: string;
  role: string;
}

export interface PatientRequest {
  name: string;
  dob: string;
  gender: string;
  bloodGroup?: string;
  allergies?: string;
  relationship?: string;
}

export interface ApiResponse<T> {
  success: boolean;
  message: string;
  data: T;
}
