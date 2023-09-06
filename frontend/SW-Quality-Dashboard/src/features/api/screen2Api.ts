import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query/react";

export const screen2Api = createApi({
  reducerPath: "screen2Api",

  baseQuery: fetchBaseQuery({
    baseUrl: "http://localhost:8080/api",
    prepareHeaders: (headers, { getState }) => {
      const token = getState().auth.accessToken;
      if (token) {
        headers.set("Authorization", `Bearer ${token}`);
      }
      return headers;
    },
  }),
  endpoints: (builder) => ({
    getCodeSmells: builder.query({
      query: (organizationId: string) => ({
        url: `/organizations/${organizationId}/code-smells-distribution`,
      }),
    }),
    getTechDebtStats: builder.query({
      query: (organizationId: string) => ({
        url: `/organizations/${organizationId}/tech-debt-statistics`,
      }),
    }),
    getActivityStats: builder.query({
      query: (organizationId: string) => ({
        url: `/organizations/${organizationId}/activity`,
      }),
    }),
    getTotalTechDebt: builder.query({
      query: (organizationId: string) => ({
        url: `/organizations/${organizationId}/total-tech-debt`,
      }),
    }),
    getRandomBestPractice: builder.query({
      query: () => ({
        url: `/best-practices`,
      }),
    }),
  }),
});

export const {
  useGetActivityStatsQuery,
  useGetCodeSmellsQuery,
  useGetTechDebtStatsQuery,
  useGetRandomBestPracticeQuery,
  useGetTotalTechDebtQuery,
} = screen2Api;
