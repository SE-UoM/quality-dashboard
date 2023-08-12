import { configureStore } from "@reduxjs/toolkit";

import authReducer from "./authSlice";
import { organizationApiSlice } from "./organizationApi";

export const store = configureStore({
    reducer: {
        [organizationApiSlice.reducerPath]: organizationApiSlice.reducer,
    },
    middleware: (getDefaultMiddleware) => getDefaultMiddleware().concat(organizationApiSlice.middleware),
    devTools: true,
});
// import { apiSlice } from "./authApi"

// export const store = configureStore({
//     reducer: {
//         auth: authReducer,
//         [apiSlice.reducerPath]: apiSlice.reducer,
//     },
//     middleware: (getDefaultMiddleware) => getDefaultMiddleware().concat(apiSlice.middleware),
//     devTools: true,
// });