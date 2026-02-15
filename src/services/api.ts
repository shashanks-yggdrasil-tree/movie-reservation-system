// src/services/api.ts
import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query/react";

export const api = createApi({
  reducerPath: "api",
  baseQuery: fetchBaseQuery({
    baseUrl: "http://localhost:8080/",

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
