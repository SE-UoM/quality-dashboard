import { createSlice, PayloadAction } from "@reduxjs/toolkit";
import { RootState } from "./store";

export interface AuthState {
  accessToken: string | null;
  refreshToken: string | null;
}

const initialState: AuthState = {
  accessToken: null,
  refreshToken: null,
};

const authSlice = createSlice({
  name: "auth",
  initialState,
  reducers: {
    setCredentials: (
      state,
      action: PayloadAction<{ accessToken: string; refreshToken: string }>
    ) => {
      state.accessToken = action.payload.accessToken;
      state.refreshToken = action.payload.refreshToken;
    },
    logOut: (state) => {
      state.accessToken = null;
      state.refreshToken = null;
    },
  },
});

export const selectAuth = (state: RootState) => state.auth;

export const { setCredentials, logOut } = authSlice.actions;

export const authMiddleware = (/* store */) => (next) => (action) => {
  // You can apply middleware logic here for the 'auth' reducer actions
  // For example, you can intercept actions, modify them, or perform side effects

  // Call the next middleware or dispatch the action
  return next(action);
};

export default authSlice.reducer;
