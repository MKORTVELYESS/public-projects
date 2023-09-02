import TextInput from "./TextInput.jsx";
import TodoContainer from "./TodoContainer.jsx";
import { useState } from "react";

const MainContainer = () => {
  const [arrTodos, setArrTodos] = useState([
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

  const [maxID, setMaxID] = useState(getMaxID());

  const [errorMsg, setErrorMsg] = useState(null);

  // function setAllDone() {
  //   const newTodods = arrTodos.map(() => {
  //     return {
  //       id: arrTodos.id,
  //       text: arrTodos.text,
  //       isDone: ture,
  //     };
  //   });
  //   setArrTodos(newTodods);
  // }

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

  return (
    <div className="container mt-3 mt-md-5 pt-md-5">
      <h1>Add a new Todo:</h1>
      <TextInput onAdd={createNewTodo} />
      <p className="text-danger">{errorMsg}</p>
      <TodoContainer onDelete={deleteTodo} todos={arrTodos} />
    </div>
  );
};
export default MainContainer;
