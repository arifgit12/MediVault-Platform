
import { createSlice, PayloadAction } from '@reduxjs/toolkit';

interface OfflineAction {
  id: string;
  type: string;
  payload: any;
}

const slice = createSlice({
  name: 'offline',
  initialState: { queue: [] as OfflineAction[] },
  reducers: {
    enqueue(state, action: PayloadAction<OfflineAction>) {
      if (!state.queue.find(q => q.id === action.payload.id)) {
        state.queue.push(action.payload);
      }
    },
    dequeue(state) {
      state.queue.shift();
    }
  }
});

export const { enqueue, dequeue } = slice.actions;
export default slice.reducer;
