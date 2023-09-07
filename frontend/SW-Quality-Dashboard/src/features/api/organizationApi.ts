import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query/react";
import { OrganizationResponse } from "./responseTypes";
import { RootState } from "./store";
import { API_URL } from "../../assets/url";

export const organizationApiSlice = createApi({
  reducerPath: "organizationApi",
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
  tagTypes: ["Organization"],
  endpoints: (builder) => ({
    // this should probably be protected
    createOrganization: builder.mutation({
      query: ({ name, location, typeOfOrg }) => ({
        url: "/",
        method: "POST",
        body: { name, location, typeOfOrg },
        headers: {
          "Content-Type": "application/json",
          // not like this
        },
      }),

      invalidatesTags: ["Organization"],
    }),
    getOrganizations: builder.query({
      query: () => ({
        url: "/",
        method: "GET",
      }),
      providesTags: ["Organization"],
    }),
    getOrganizationProjects: builder.query({
      query: (organizationId) => ({
        url: `/${organizationId}/projects`,
        method: "GET",
      }),
    }),
    getOrganizationAnalysis: builder.query({
      query: (organizationId) => ({
        url: `/${organizationId}/organization-analysis`,
        method: "GET",
      }),
    }),
  }),
});
export const {
  useGetOrganizationsQuery,
  useCreateOrganizationMutation,
  useGetOrganizationAnalysisQuery,
  useGetOrganizationProjectsQuery,
} = organizationApiSlice;
