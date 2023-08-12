import { createSlice } from "@reduxjs/toolkit";


const authSlice = createSlice({
    name: 'auth',
    initialState: {
        token: null,
        user: null,
    },
    reducers: {
        setCredentials: (state, action) => {
            state.token = action.payload.token;
            state.user = action.payload.user;
        },
        logOut: (state) => {
            state.user = null;
            state.token = null;
        }

    },

})

export const { setCredentials, logOut } = authSlice.actions;

export default authSlice.reducer;