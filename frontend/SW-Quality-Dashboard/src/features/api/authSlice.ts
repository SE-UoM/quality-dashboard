import { createSlice } from "@reduxjs/toolkit";


const authSlice = createSlice({
    name: 'auth',
    initialState: {
        accessToken: null,
        refreshToken: null,
    },
    reducers: {
        setCredentials: (state, action) => {
            state.accessToken = action.payload.accessToken;
            state.refreshToken = action.payload.refreshToken;
        },
        logOut: (state) => {
            state.accessToken = null;
            state.refreshToken = null;
        }

    },

})

export const { setCredentials, logOut } = authSlice.actions;

export default authSlice.reducer;