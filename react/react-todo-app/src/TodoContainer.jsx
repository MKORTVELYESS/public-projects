import TodoItem from "./TodoItem";
const TodoContainer = ({ todos, onDelete }) => {
  return (
    <div className="mb-5">
      {todos.map((todo) => {
        return (
          <TodoItem
            text={todo.text}
            isDone={todo.isDone}
            onDelete={() => {
              onDelete(todo.id);
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
