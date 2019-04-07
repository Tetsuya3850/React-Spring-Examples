import { schema } from "normalizr";

const user = new schema.Entity("users");

export const article = new schema.Entity("articles", {
  author: user
});
