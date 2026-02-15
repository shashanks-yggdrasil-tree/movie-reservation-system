import { endpoints } from "../shared/constants";
import type { Theater } from "../shared/interface";
import { api } from "./api";

export const userApi = api.injectEndpoints({
  endpoints: (builder) => ({
    getAllTheaters: builder.query<Theater[], void>({
      query: () => endpoints.theaters,
    }),
    createTheater: builder.mutation<Theater, Theater>({
      query: (body) => ({
        url: `${endpoints.theaters}`,
        method: "POST",
        body: body,
      }),
    }),
  }),
});

export const { useGetAllTheatersQuery, useCreateTheaterMutation } = userApi;
