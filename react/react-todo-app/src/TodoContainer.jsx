import TodoItem from "./TodoItem";
import { TodoItemsContext } from "./TodoItemsContext";
import { useContext } from "react";
const TodoContainer = ({ onDelete, onToggleTodo }) => {
  const arrTodos = useContext(TodoItemsContext);

  return (
    <div className="mb-5">
      {arrTodos.map((todo) => {
        return (
          <TodoItem
            text={todo.text}
            isDone={todo.isDone}
            onDelete={() => {
              onDelete(todo.id);
            }}
            onToggle={() => {
              onToggleTodo(todo.id);
            }}
            key={`Todo-${todo.id}`}
          />
        );
      })}

      <hr />
    </div>
  );
};
export default TodoContainer;
