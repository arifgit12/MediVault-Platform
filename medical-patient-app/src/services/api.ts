import axios from 'axios';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { API_BASE_URL } from '../constants';
import { 
  LoginRequest, RegisterRequest, AuthResponse, 
  Patient, PatientRequest, Prescription, ApiResponse 
} from '../types';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

api.interceptors.request.use(async (config) => {
  const token = await AsyncStorage.getItem('token');
  if (token && config.headers) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// Auth APIs
export const login = async (credentials: LoginRequest): Promise<AuthResponse> => {
  const response = await api.post<ApiResponse<AuthResponse>>('/auth/login', credentials);
  return response.data.data;
};

export const register = async (data: RegisterRequest): Promise<AuthResponse> => {
  const response = await api.post<ApiResponse<AuthResponse>>('/auth/register', data);
  return response.data.data;
};

// Patient APIs
export const getPatients = async (): Promise<Patient[]> => {
  const response = await api.get<ApiResponse<Patient[]>>('/patients');
  return response.data.data;
};

export const createPatient = async (data: PatientRequest): Promise<Patient> => {
  const response = await api.post<ApiResponse<Patient>>('/patients', data);
  return response.data.data;
};

export const updatePatient = async (id: number, data: PatientRequest): Promise<Patient> => {
  const response = await api.put<ApiResponse<Patient>>(`/patients/${id}`, data);
  return response.data.data;
};

export const deletePatient = async (id: number): Promise<void> => {
  await api.delete(`/patients/${id}`);
};

// Prescription APIs
export const getPrescriptionsByPatient = async (patientId: number): Promise<Prescription[]> => {
  const response = await api.get<ApiResponse<Prescription[]>>(`/prescriptions/patient/${patientId}`);
  return response.data.data;
};

export const uploadPrescription = async (uploadId: string, patientId: number, file: any): Promise<Prescription> => {
  const formData = new FormData();
  formData.append('uploadId', uploadId);
  formData.append('patientId', patientId.toString());
  formData.append('file', file);

  const response = await api.post<ApiResponse<Prescription>>('/prescriptions/upload', formData, {
    headers: {
      'Content-Type': 'multipart/form-data',
    },
  });
  return response.data.data;
};

export const updatePrescription = async (id: number, data: any): Promise<Prescription> => {
  const response = await api.put<ApiResponse<Prescription>>(`/prescriptions/${id}`, data);
  return response.data.data;
};

export default api;
