import { Router } from "express";
import { list, getByID, change } from "../controllers/books.controller.js";
import { validBook, validChange } from "../middlewares/validator.middleware.js";

const router = Router();

router.get("/", list);
router.get("/:id", validBook, getByID);
router.patch("/:id", validChange, change);

export default router;
