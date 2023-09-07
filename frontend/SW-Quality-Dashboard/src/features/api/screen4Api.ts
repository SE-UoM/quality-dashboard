import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query/react";
import { API_URL } from "../../assets/url";

export const screen4Api = createApi({
  reducerPath: "screen4Api",
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
    getMostActiveDevelopers: builder.query({
      query: (organizationId: string) => ({
        url: `/${organizationId}/most-active-developer`,
      }),
    }),
    getMostActiveProject: builder.query({
      query: (organizationId: string) => ({
        url: `/${organizationId}/most-active-project`,
      }),
    }),
    getAllCommit: builder.query({
      query: (organizationId: string) => ({
        url: `/${organizationId}/all-commits`,
      }),
    }),
    getMostStarredProject: builder.query({
      query: (organizationId: string) => ({
        url: `/${organizationId}/most-starred-project`,
      }),
    }),
    getMostForkedProject: builder.query({
      query: (organizationId: string) => ({
        url: `/${organizationId}/most-forked-project`,
      }),
    }),
    getDevelopersInfo: builder.query({
      query: (organizationId: string) => ({
        url: `/${organizationId}/developers-info`,
      }),
    }),
  }),
});

export const {
  useGetAllCommitQuery,
  useGetDevelopersInfoQuery,
  useGetMostForkedProjectQuery,
  useGetMostActiveDevelopersQuery,
  useGetMostActiveProjectQuery,
  useGetMostStarredProjectQuery,
} = screen4Api;
