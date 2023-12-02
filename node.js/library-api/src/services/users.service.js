import * as db from "./db.service.js";

export const add = async (user) => {
  const result = await db.query(
    "INSERT INTO users (first_name, last_name, email, phone) VALUES ($1, $2, $3, $4) RETURNING *",
    [user.firstName, user.lastName, user.email, user.phone]
  );
  const newUser = userDataDictionary(result);
  return newUser;
};

export const remove = async (id) => {
  const result = await db.query("DELETE FROM users WHERE id = $1 RETURNING *", [
    id,
  ]);
  const newUser = userDataDictionary(result);
  return newUser;
};

const userDataDictionary = (user) => {
  return {
    firstName: user.rows[0].first_name,
    lastName: user.rows[0].last_name,
    email: user.rows[0].email,
    phone: user.rows[0].phone,
    id: user.rows[0].id,
  };
};

export const get = async (id) => {
  const result = await db.query("SELECT * FROM users WHERE id = $1", [id]);
  return result.rows[0];
};
