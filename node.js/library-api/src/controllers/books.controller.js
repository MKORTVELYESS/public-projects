import * as books from "../services/books.service.js";

export const list = async (req, res, next) => {
  try {
    //Get query params
    let available;
    if (req.query.available) {
      available = req.query.available === "true";
    }
    const title = req.query.title;

    //Call select with query params - params undefined if not provided
    const result = await books.select(available, title);
    res.send(result);
  } catch (err) {
    next(err);
  }
};

export const getByID = async (req, res, next) => {
  try {
    const result = await books.get(req.params.id);
    res.send(result);
  } catch (err) {
    next(err);
  }
};

export const change = async (req, res, next) => {
  try {
    //Get start state of book
    const startState = await books.get(req.params.id);
    const currentBorrower = startState.borrower;

    //Determine action --> return or borrow
    if (currentBorrower) {
      //If book has borrower --> return
      await books.ret(req.params.id);
    } else {
      //Else --> borrow
      await books.borrow(req.body.userId, req.params.id);
    }

    //Get and send end state of item
    const endState = await books.get(req.params.id);
    res.send(endState);
  } catch (err) {
    next(err);
  }
};

export const getUserBooks = async (req, res, next) => {
  try {
    const userID = req.params.id;
    const result = await books.of(userID);
    res.send(result);
  } catch (err) {
    next(err);
  }
};
