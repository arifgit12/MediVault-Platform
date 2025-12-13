import { combineReducers } from '@reduxjs/toolkit';
import authReducer from './slices/authSlice';
import patientReducer from './slices/patientSlice';
import prescriptionReducer from './slices/prescriptionSlice';

const rootReducer = combineReducers({
  auth: authReducer,
  patient: patientReducer,
  prescription: prescriptionReducer,
});

export type RootState = ReturnType<typeof rootReducer>;
export default rootReducer;
