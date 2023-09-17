import "./App.css";
import MyNavbar from "./MyNavbar";
import ViteReact from "./ViteReact";
import MainContainer from "./MainContainer";
import { TodoItemsContext } from "./TodoItemsContext";
import { useContext, useState } from "react";
import { TodoItemsDispatchContext } from "./TodoItemsDispatchContext";
function App() {
  const [todoItemsState, setTodoItemsState] = useState(
    useContext(TodoItemsContext)
  );

  return (
    <>
      <TodoItemsDispatchContext.Provider value={setTodoItemsState}>
        <MyNavbar />
        <TodoItemsContext.Provider value={todoItemsState}>
          <MainContainer />
        </TodoItemsContext.Provider>
      </TodoItemsDispatchContext.Provider>
      <ViteReact />
    </>
  );
}

export default App;
