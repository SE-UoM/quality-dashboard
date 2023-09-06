import { configureStore } from "@reduxjs/toolkit";

import authReducer, { authMiddleware } from "./authSlice";
import { organizationApiSlice } from "./organizationApi";
import { registerApi } from "./registerApi";
import { screen1Api } from "./screen1Api";
import { screen2Api } from "./screen2Api";
import { screen3Api } from "./screen3Api";

export const store = configureStore({
  reducer: {
    [organizationApiSlice.reducerPath]: organizationApiSlice.reducer,
    auth: authReducer,
    [registerApi.reducerPath]: registerApi.reducer,

    [screen1Api.reducerPath]: screen1Api.reducer,
    [screen2Api.reducerPath]: screen2Api.reducer,
    [screen3Api.reducerPath]: screen3Api.reducer,
  },
  middleware: (getDefaultMiddleware) =>
    getDefaultMiddleware().concat(
      organizationApiSlice.middleware,
      authMiddleware,
      registerApi.middleware,
      screen1Api.middleware,
      screen2Api.middleware,
      screen3Api.middleware
    ),
  devTools: true,
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;
// import { apiSlice } from "./authApi"

// export const store = configureStore({
//     reducer: {
//         auth: authReducer,
//         [apiSlice.reducerPath]: apiSlice.reducer,
//     },
//     middleware: (getDefaultMiddleware) => getDefaultMiddleware().concat(apiSlice.middleware),
//     devTools: true,
// });
