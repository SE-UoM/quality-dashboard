import { createApi, fetchBaseQuery } from '@reduxjs/toolkit/query/react';

export const organizationApiSlice = createApi({
    baseQuery: fetchBaseQuery({
        baseUrl: 'http://localhost:8080/api/organization'
    }),
    tagTypes: ['Organization'],
    endpoints: (builder) => ({
        // this should probably be protected
        createOrganization: builder.mutation({
            query: ({ name, location, typeOfOrg }) => ({
                url: '/',
                method: 'POST',
                body: { name, location, typeOfOrg },
                headers: {
                    'Content-Type': 'application/json',
                    // not like this
                },
            }),

            invalidatesTags: ['Organization']
        }),
        getOrganizations: builder.query({
            query: () => ({
                url: '/',
                method: 'GET',
            }),
            providesTags: ['Organization'],
        }),
        getOrganizationProjects: builder.query({
            query: (organizationId) => ({
                url: `/${organizationId}/projects`,
                method: 'GET',
            })
        }),
        getOrganizationAnalysis: builder.query({
            query: (organizationId) => ({
                url: `/${organizationId}/organization-analysis`,
                method: 'GET',
            }),
        })

    }),
});
export const { useGetOrganizationsQuery, useCreateOrganizationMutation, useGetOrganizationAnalysisQuery, useGetOrganizationProjectsQuery } = organizationApiSlice;
