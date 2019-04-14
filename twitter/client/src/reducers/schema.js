import { schema } from "normalizr";

export const user = new schema.Entity("users");

export const tweet = new schema.Entity("tweets", {
  applicationUser: user
});
