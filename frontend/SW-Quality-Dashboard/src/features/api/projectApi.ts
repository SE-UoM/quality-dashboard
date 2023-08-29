import { createApi, fetchBaseQuery } from '@reduxjs/toolkit/query/react';


export const projectApiSlice = createApi({
    baseQuery: fetchBaseQuery({
        baseUrl: 'http://localhost:8080/api/projects',
    }),
    tagTypes: ['Project'],
    endpoints: (builder) => ({
        // this should not probably be protected
        getProjects: builder.query({
            query: () => ({
                url: '/',
                method: 'GET',
            }),
            providesTags: ['Project'],
        }),
        getProjectCommits: builder.query({
            query: (projectId) => ({
                url: `/${projectId}/commits`,
                method: 'GET',
            }),
        }),
        getProjectDevelopers: builder.query({
            query: (projectId) => ({
                url: `/${projectId}/developers`,
                method: 'GET',
            }),
        }),
        getProjectCommitByCommitId: builder.query({
            query: (data) => ({
                url: `/${data.projectId}/commits/${data.commitId}`,
                method: 'GET',
            }),
        }),
    }),
});

export const { useGetProjectsQuery, useGetProjectCommitsQuery, useGetProjectDevelopersQuery, useGetProjectCommitByCommitIdQuery } = projectApiSlice;
