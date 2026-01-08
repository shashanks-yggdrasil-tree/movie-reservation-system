// src/services/api.ts
import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query/react";

export const api = createApi({
  reducerPath: "api",
  baseQuery: fetchBaseQuery({
    baseUrl: "http://192.168.10.3:8080/api/v1/",

    // prepareHeaders: (headers) => {
    //   // Add auth headers if needed
    //   const token = localStorage.getItem('token')
    //   if (token) {
    //     headers.set('Authorization', `Bearer ${token}`)
    //   }
    //   return headers
    // }
  }),
  endpoints: () => ({}), // Start with empty endpoints
});
