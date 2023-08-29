import { configureStore } from "@reduxjs/toolkit";

import authReducer from "./authSlice";
import { organizationApiSlice } from "./organizationApi";
import { registerApi } from "./registerApi";

export const store = configureStore({
    reducer: {
        [organizationApiSlice.reducerPath]: organizationApiSlice.reducer,
        [registerApi.reducerPath]: registerApi.reducer,
        auth: authReducer,
    },
    middleware: (getDefaultMiddleware) => getDefaultMiddleware().concat(organizationApiSlice.middleware, registerApi.middleware),
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