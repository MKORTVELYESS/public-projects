import { Router } from "express";
import { createNewUser, deleteUser } from "../controllers/users.controller.js";
import { getUserBooks } from "../controllers/books.controller.js";
import { validUser } from "../middlewares/validator.middleware.js";
const router = Router();

router.post("/", createNewUser);
router.get("/:id/books", validUser, getUserBooks);
router.delete("/:id", validUser, deleteUser);

export default router;
