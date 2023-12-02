import * as book from "../services/books.service.js";
import * as user from "../services/users.service.js";
export const validUser = async (req, res, next) => {
  //validation middleware to handle 404 error on all routes where userID param is used
  try {
    const id = req.params.id;
    const result = await user.get(id);
    if (result == undefined) {
      const err = new Error("User ID not in db");
      err.status = 404;
      next(err);
    }
    next();
  } catch (err) {
    //Handle generic error
    next(err);
  }
};

export const validBook = async (req, res, next) => {
  //validation middleware to handle 404 error on all routes where bookID param is used
  try {
    const id = req.params.id;
    const result = await book.get(id);
    if (result == undefined) {
      const err = new Error("Book ID not in db");
      err.status = 404;
      next(err);
    }
    next();
  } catch (err) {
    //Handle generic errors
    next(err);
  }
};

export const validChange = async (req, res, next) => {
  try {
    //Get parameters
    const bookID = req.params.id;
    const userID = req.body.userId;

    //Get entities
    const borrower = await user.get(userID);
    const item = await book.get(bookID);

    //Only continue validation if entities exist
    if (borrower && item) {
      //Evaluatie connection between borrowerID in item & userID in request if item is borrowed
      if (item.borrower != null && item.borrower !== userID) {
        const err = new Error(
          "Validation error: Book already borrowed and not connected to user in request"
        );
        err.status = 405;
        next(err);
      } else {
        //if item not borrowed or borrowerID in item match userID in request proceed to --> next
        next();
      }
    } else {
      //if entities not present
      const err = new Error("Validation error: BookID or UserID not in DB");
      err.status = 405;
      next(err);
    }
  } catch (err) {
    //catch unexpected errors in general error handling and respond 400
    next(err);
  }
};
