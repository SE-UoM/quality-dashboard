import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query/react";
import { API_URL } from "../../assets/url";

export const screen5Api = createApi({
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
    getCodeCoverage: builder.query({
      query: (organizationId: string) => ({
        url: `/${organizationId}/most-active-developer`,
      }),
    }),
    getCommitTypes: builder.query({
      query: (organizationId: string) => ({
        url: `/${organizationId}/most-active-developer`,
      }),
    }),
    getHotspots: builder.query({
      query: (organizationId: string) => ({
        url: `/${organizationId}/most-active-developer`,
      }),
    }),
    getDependencies: builder.query({
      query: (organizationId: string) => ({
        url: `/${organizationId}/most-active-developer`,
      }),
    }),
  }),
});

export const {
  useGetCodeCoverageQuery,
  useGetDependenciesQuery,
  useGetCommitTypesQuery,
  useGetHotspotsQuery,
} = screen5Api;
