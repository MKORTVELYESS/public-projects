import * as db from "./db.service.js";

export const select = async (available, title) => {
  //Create query string for all cases
  let qStr;
  if (available == null && title == null) {
    //no query params case
    qStr = "SELECT * FROM books";
  } else if (available != null && title == null) {
    //only availability query param present
    qStr = `SELECT * FROM books WHERE borrower IS ${
      available ? "NULL" : "NOT NULL"
    }`;
  } else if (available == null && title != null) {
    //only title query param present
    qStr = `SELECT * FROM books WHERE title LIKE ${title}`;
  } else if (available != null && title != null) {
    //both query params present
    qStr = `SELECT * FROM books WHERE borrower IS ${
      available ? "NULL" : "NOT NULL"
    } AND title LIKE ${title}`;
  }

  const result = await db.query(qStr);

  return result.rows;
};

export const get = async (id) => {
  const result = await db.query("SELECT * FROM books WHERE id = $1", [id]);
  return result.rows[0];
};

export const ret = async (id) => {
  const result = await db.query(
    "UPDATE books SET return_date = CURRENT_TIMESTAMP, borrowing_date = NULL, borrower= NULL WHERE id = $1 RETURNING *",
    [id]
  );
  return result.rows[0];
};

export const borrow = async (userID, id) => {
  const result = await db.query(
    "UPDATE books SET return_date = NULL, borrowing_date = CURRENT_TIMESTAMP, borrower = $1 WHERE id = $2 RETURNING *",
    [userID, id]
  );
  return result.rows[0];
};

export const of = async (id) => {
  const result = await db.query("SELECT * FROM books WHERE borrower = $1", [
    id,
  ]);
  return result.rows;
};
