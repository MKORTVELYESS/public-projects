import { MDBCheckbox } from "mdb-react-ui-kit";
import React, { useEffect, useRef, useState } from "react";
import "./TodoItem.css";
const TodoItem = ({ text, isDone, onDelete }) => {
  const parentRef = useRef(null);
  const [buttonHeight, setButtonHeight] = useState("auto");
  const [isChecked, setIsChecked] = useState(isDone);

  function toggleChecked() {
    setIsChecked(!isChecked);
  }

  useEffect(() => {
    //Handles Delete button resizeing in case its parent container grows  due to Todod text being broken into multipl lines
    const parentElement = parentRef.current;
    if (parentElement) {
      const parentHeight = parentElement.clientHeight;
      setButtonHeight(parentHeight + "px");
    }
  }, []);

  return (
    <div
      ref={parentRef}
      className={`d-flex align-items-center ps-2 justify-content-between todo-item rounded my-2 ${
        isChecked ? "selected" : null
      }`}
    >
      <MDBCheckbox
        name="inlineCheck"
        id="inlineCheckbox1"
        value="option1"
        label=""
        onChange={toggleChecked}
        checked={isChecked}
        inline
      />
      <p className="my-auto">{text}</p>
      <div className={`d-flex button-container`}>
        <button
          onClick={onDelete}
          style={{ height: buttonHeight }}
          className="btn btn-danger"
        >
          Delete
        </button>
      </div>
    </div>
  );
};
export default TodoItem;
