import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query/react";
import { API_URL } from "../../assets/url";

export const screen3Api = createApi({
  reducerPath: "screen3Api",
  baseQuery: fetchBaseQuery({
    baseUrl: `${API_URL}/api/organizations`,
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
    getTopContributors: builder.query({
      query: (organizationId: string) => ({
        url: `/${organizationId}/top-contributors`,
      }),
    }),
    getTopLanguages: builder.query({
      query: (organizationId: string) => ({
        url: `/${organizationId}/language-distribution`,
      }),
    }),
    getTopDevelopers: builder.query({
      query: (organizationId: string) => ({
        url: `/${organizationId}/top-contributors`,
      }),
    }),
  }),
});

export const {
  useGetProjectsInfoQuery,
  useGetTopContributorsQuery,
  useGetTopLanguagesQuery,
  useGetTopProjectsQuery,
  useGetTopDevelopersQuery,
} = screen3Api;
