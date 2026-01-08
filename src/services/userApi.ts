// src/services/userApi.ts
import { api } from "./api";

export const userApi = api.injectEndpoints({
  endpoints: (builder) => ({
    getUsers: builder.query<String, void>({
      query: () => ({
        url: `hello-world`,
        responseHandler: (response) => {
          return response.text();
        },
      }),
    }),
  }),
});

export const { useGetUsersQuery } = userApi;
