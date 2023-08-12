import { createApi, fetchBaseQuery } from '@reduxjs/toolkit/query/react';


export const analysisApiSlice = createApi({
    baseQuery: fetchBaseQuery({
        baseUrl: 'http://localhost:8080/api/analysis'
    }),
    tagTypes: ['Analysis'],
    endpoints: (builder) => ({
        // this should probably be protected
        startAnalysis: builder.mutation({
            query: (data) => ({
                url: '/start',
                params: {
                    github_url: data
                }
            }),
        }),
    })

});

export const { useStartAnalysisMutation } = analysisApiSlice;
