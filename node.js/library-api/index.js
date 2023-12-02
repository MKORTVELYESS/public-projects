import express from "express";
import "dotenv/config";
import booksRouter from "./src/routes/books.router.js";
import usersRouter from "./src/routes/users.router.js";
const port = process.env.SERVER_PORT;

const app = express();

app.use(express.json());

app.use("/", (req, res, next) => {
  console.log("HTTP: ", req.method, "url: ", req.url);
  next();
});

app.use("/books", booksRouter);
app.use("/users", usersRouter);

app.use((err, req, res, next) => {
  if (err.status == undefined) {
    err.status = 400;
  }
  res.status(err.status).json({
    error: "Example: Internal server error.",
    message: err.message,
  });
});
app.listen(port, () => console.log("Listening on port ", port));
