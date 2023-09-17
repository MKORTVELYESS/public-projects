import { createContext } from "react";
export const TodoItemsContext = createContext([
  {
    id: 1,
    text: "Clean the Kitchen",
    isDone: false,
  },
  { id: 2, text: "Wash the Dishes", isDone: false },
  { id: 3, text: "Feed the dog", isDone: true },
  { id: 4, text: "Study for the exam", isDone: false },
  { id: 5, text: "Buy plane tickets", isDone: false },
]);
