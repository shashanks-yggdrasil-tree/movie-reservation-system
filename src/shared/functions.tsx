import { faker } from "@faker-js/faker";
import type { Theater } from "./interface";

export const generateRandomTheater = (): Theater => {
  return {
    theaterName: faker.company.name() + " Cinemas",
    addressLine1: faker.location.streetAddress(),
    city: faker.location.city(),
    state: faker.location.state(),
  };
};
