import { createApi, fetchBaseQuery } from '@reduxjs/toolkit/query/react';

interface LoginResponse {
    accessToken: string;
    refreshToken: string;
}

interface LoginRequest {
    email: string;
    password: string;
}

export const registerApi = createApi({

    baseQuery: fetchBaseQuery({
        baseUrl: 'http://localhost:8080',
    }),
    endpoints: (builder) => ({
        register: builder.mutation({
            query: ({ email, password, name }) => ({
                url: '/api/user/register',
                method: 'POST',
                body: { email, password, name },
                headers: {
                    'Content-Type': 'application/json',
                },
            }),
        }),
        login: builder.mutation<LoginResponse, LoginRequest>({
            query: ({ email, password }) => {
                const formData = new URLSearchParams();
                formData.append('email', email);
                formData.append('password', password);
                return {
                    url: '/login',
                    method: 'POST',
                    body: formData.toString(),
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded',
                    },
                }
            },
            // transformResponse: (response: Response) => {
            //     const accessToken = response.body?.;
            //     const refreshToken = response.body?.get('refreshToken');
            //     if (accessToken && refreshToken) {
            //         return { accessToken, refreshToken };
            //     } else {
            //         return response.json();
            //     }
            // }
        })
    }),

});

export const { useRegisterMutation, useLoginMutation } = registerApi;