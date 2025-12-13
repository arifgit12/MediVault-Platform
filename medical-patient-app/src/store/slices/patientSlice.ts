import { createSlice, createAsyncThunk, PayloadAction } from '@reduxjs/toolkit';
import { Patient, PatientRequest } from '../../types';
import * as api from '../../services/api';

interface PatientState {
  patients: Patient[];
  selectedPatient: Patient | null;
  loading: boolean;
  error: string | null;
}

const initialState: PatientState = {
  patients: [],
  selectedPatient: null,
  loading: false,
  error: null,
};

export const fetchPatients = createAsyncThunk(
  'patient/fetchAll',
  async (_, { rejectWithValue }) => {
    try {
      return await api.getPatients();
    } catch (error: any) {
      return rejectWithValue(error.response?.data?.message || 'Failed to fetch patients');
    }
  }
);

export const createPatient = createAsyncThunk(
  'patient/create',
  async (data: PatientRequest, { rejectWithValue }) => {
    try {
      return await api.createPatient(data);
    } catch (error: any) {
      return rejectWithValue(error.response?.data?.message || 'Failed to create patient');
    }
  }
);

export const updatePatient = createAsyncThunk(
  'patient/update',
  async ({ id, data }: { id: number; data: PatientRequest }, { rejectWithValue }) => {
    try {
      return await api.updatePatient(id, data);
    } catch (error: any) {
      return rejectWithValue(error.response?.data?.message || 'Failed to update patient');
    }
  }
);

export const deletePatient = createAsyncThunk(
  'patient/delete',
  async (id: number, { rejectWithValue }) => {
    try {
      await api.deletePatient(id);
      return id;
    } catch (error: any) {
      return rejectWithValue(error.response?.data?.message || 'Failed to delete patient');
    }
  }
);

const patientSlice = createSlice({
  name: 'patient',
  initialState,
  reducers: {
    selectPatient: (state, action: PayloadAction<Patient>) => {
      state.selectedPatient = action.payload;
    },
    clearPatientError: (state) => {
      state.error = null;
    },
  },
  extraReducers: (builder) => {
    builder
      .addCase(fetchPatients.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchPatients.fulfilled, (state, action) => {
        state.loading = false;
        state.patients = action.payload;
      })
      .addCase(fetchPatients.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload as string;
      })
      .addCase(createPatient.pending, (state) => {
        state.loading = true;
      })
      .addCase(createPatient.fulfilled, (state, action) => {
        state.loading = false;
        state.patients.push(action.payload);
      })
      .addCase(createPatient.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload as string;
      })
      .addCase(updatePatient.fulfilled, (state, action) => {
        const index = state.patients.findIndex(p => p.id === action.payload.id);
        if (index !== -1) {
          state.patients[index] = action.payload;
        }
      })
      .addCase(deletePatient.fulfilled, (state, action) => {
        state.patients = state.patients.filter(p => p.id !== action.payload);
      });
  },
});

export const { selectPatient, clearPatientError } = patientSlice.actions;
export default patientSlice.reducer;
