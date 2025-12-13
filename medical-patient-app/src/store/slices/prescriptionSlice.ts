import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import { Prescription } from '../../types';
import * as api from '../../services/api';

interface PrescriptionState {
  prescriptions: Prescription[];
  selectedPrescription: Prescription | null;
  loading: boolean;
  error: string | null;
}

const initialState: PrescriptionState = {
  prescriptions: [],
  selectedPrescription: null,
  loading: false,
  error: null,
};

export const fetchPrescriptionsByPatient = createAsyncThunk(
  'prescription/fetchByPatient',
  async (patientId: number, { rejectWithValue }) => {
    try {
      return await api.getPrescriptionsByPatient(patientId);
    } catch (error: any) {
      return rejectWithValue(error.response?.data?.message || 'Failed to fetch prescriptions');
    }
  }
);

export const uploadPrescription = createAsyncThunk(
  'prescription/upload',
  async ({ uploadId, patientId, file }: { uploadId: string; patientId: number; file: any }, { rejectWithValue }) => {
    try {
      return await api.uploadPrescription(uploadId, patientId, file);
    } catch (error: any) {
      return rejectWithValue(error.response?.data?.message || 'Failed to upload prescription');
    }
  }
);

const prescriptionSlice = createSlice({
  name: 'prescription',
  initialState,
  reducers: {
    selectPrescription: (state, action) => {
      state.selectedPrescription = action.payload;
    },
    clearPrescriptionError: (state) => {
      state.error = null;
    },
  },
  extraReducers: (builder) => {
    builder
      .addCase(fetchPrescriptionsByPatient.pending, (state) => {
        state.loading = true;
      })
      .addCase(fetchPrescriptionsByPatient.fulfilled, (state, action) => {
        state.loading = false;
        state.prescriptions = action.payload;
      })
      .addCase(fetchPrescriptionsByPatient.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload as string;
      })
      .addCase(uploadPrescription.pending, (state) => {
        state.loading = true;
      })
      .addCase(uploadPrescription.fulfilled, (state, action) => {
        state.loading = false;
        state.prescriptions.unshift(action.payload);
      })
      .addCase(uploadPrescription.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload as string;
      });
  },
});

export const { selectPrescription, clearPrescriptionError } = prescriptionSlice.actions;
export default prescriptionSlice.reducer;
