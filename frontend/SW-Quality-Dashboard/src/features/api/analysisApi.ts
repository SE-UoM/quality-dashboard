import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query/react";

export const analysisApiSlice = createApi({
  baseQuery: fetchBaseQuery({
    baseUrl: "http://localhost:8080/api/analysis",
    prepareHeaders: (headers, { getState }) => {
      const token = getState().auth.accessToken;
      if (token) {
        headers.set("Authorization", `Bearer ${token}`);
      }
      return headers;
    },
  }),
  tagTypes: ["Analysis"],
  endpoints: (builder) => ({
    // this should probably be protected
    startAnalysis: builder.mutation({
      query: (githubUrl) => ({
        url: "/start",
        params: {
          github_url: githubUrl,
        },
        method: "POST",
      }),
    }),
  }),
});

export const { useStartAnalysisMutation } = analysisApiSlice;
