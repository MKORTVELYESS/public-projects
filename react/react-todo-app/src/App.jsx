import "./App.css";
import MyNavbar from "./MyNavbar";
import ViteReact from "./ViteReact";
import MainContainer from "./MainContainer";
import { TodoItemsContext } from "./TodoItemsContext";
import { useState } from "react";
import { TodoItemsDispatchContext } from "./TodoItemsDispatchContext";
function App() {
  const [todoItemsState, setTodoItemsState] = useState([
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

  return (
    <>
      <TodoItemsDispatchContext.Provider value={setTodoItemsState}>
        <TodoItemsContext.Provider value={todoItemsState}>
          <MyNavbar />
          <MainContainer />
        </TodoItemsContext.Provider>
      </TodoItemsDispatchContext.Provider>
      <ViteReact />
    </>
  );
}

export default App;
