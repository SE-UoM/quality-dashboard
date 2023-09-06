import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query/react";

export const screen3Api = createApi({
  reducerPath: "screen3Api",
  baseQuery: fetchBaseQuery({
    baseUrl: "http://localhost:8080/api/organizations",
    prepareHeaders: (headers, { getState }) => {
      const token = getState().auth.accessToken;
      if (token) {
        headers.set("Authorization", `Bearer ${token}`);
      }
      return headers;
    },
  }),
  endpoints: (builder) => ({
    getTopProjects: builder.query({
      query: (organizationId: string) => ({
        url: `/${organizationId}/top-projects`,
      }),
    }),
    getProjectsInfo: builder.query({
      query: (organizationId: string) => ({
        url: `/${organizationId}/projects-info`,
      }),
    }),
    getTopDevelopers: builder.query({
      query: (organizationId: string) => ({
        url: `/${organizationId}/top-developers`,
      }),
    }),
    getTopLanguages: builder.query({
      query: (organizationId: string) => ({
        url: `/${organizationId}/language-distribution`,
      }),
    }),
  }),
});

export const {
  useGetProjectsInfoQuery,
  useGetTopDevelopersQuery,
  useGetTopLanguagesQuery,
  useGetTopProjectsQuery,
} = screen3Api;
