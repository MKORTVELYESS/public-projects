import TextInput from "./TextInput.jsx";
import TodoContainer from "./TodoContainer.jsx";
import { useState } from "react";
import { useContext } from "react";
import { TodoItemsContext } from "./TodoItemsContext.jsx";
import { TodoItemsDispatchContext } from "./TodoItemsDispatchContext.jsx";

const MainContainer = () => {
  const arrTodos = useContext(TodoItemsContext);
  const setArrTodos = useContext(TodoItemsDispatchContext);

  const [maxID, setMaxID] = useState(getMaxID());

  const [errorMsg, setErrorMsg] = useState(null);

  function getMaxID() {
    return arrTodos.reduce((acc, curr) => {
      return curr.id > acc ? curr.id : acc;
    }, 0);
  }

  function deleteTodo(id) {
    const newTodos = arrTodos.filter((objTodo) => objTodo.id !== id);
    setArrTodos(newTodos);
    setErrorMsg("");
  }
  function createNewTodo(textNewTodo) {
    textNewTodo = textNewTodo.trim();
    if (textNewTodo !== "") {
      const objNewTodo = {
        text: textNewTodo,
        isDone: false,
        id: maxID + 1,
      };
      const newExtendedTodos = [...arrTodos, objNewTodo];
      setArrTodos(newExtendedTodos);
      setMaxID(maxID + 1);
      setErrorMsg("");
    } else {
      setErrorMsg("The input is empty");
    }
  }

  function toggleItemState(id) {
    const newTodos = arrTodos.map((item) => {
      if (item.id == id) {
        return { ...item, isDone: !item.isDone };
      } else {
        return item;
      }
    });
    setArrTodos(newTodos);
  }
  return (
    <div className="container mt-3 mt-md-5 pt-md-5">
      <h1>Add a new Todo:</h1>
      <TextInput onAdd={createNewTodo} />
      <p className="text-danger">{errorMsg}</p>
      <TodoContainer onDelete={deleteTodo} onToggleTodo={toggleItemState} />
    </div>
  );
};
export default MainContainer;
