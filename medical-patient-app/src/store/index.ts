
import { configureStore } from '@reduxjs/toolkit';
import offlineReducer from './slices/offlineSlice';

export const store = configureStore({
  reducer: {
    offline: offlineReducer
  }
});
