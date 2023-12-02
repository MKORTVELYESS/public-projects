import * as users from "../services/users.service.js";
import * as books from "../services/books.service.js";

export const createNewUser = async (req, res, next) => {
  try {
    const result = await users.add(req.body);
    res.status(201).send(result);
  } catch (err) {
    next(err);
  }
};

export const deleteUser = async (req, res, next) => {
  try {
    //Get books outstanding with borrower
    const borrowedBooks = await books.of(req.params.id);

    //If borrower has no books outstanding
    if (borrowedBooks.length === 0) {
      const result = await users.remove(req.params.id);
      res.send(result);
    } else {
      //Inform borrower of outstanding books
      const err = new Error(
        "User has outstanding books and can not be deleted"
      );
      throw err;
    }
  } catch (err) {
    next(err);
  }
};
