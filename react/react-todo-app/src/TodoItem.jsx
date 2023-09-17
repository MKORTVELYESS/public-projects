import { MDBCheckbox } from "mdb-react-ui-kit";
import React, { useEffect, useRef, useState } from "react";
import "./TodoItem.css";
const TodoItem = ({ text, isDone, onDelete, onToggle }) => {
  const pRef = useRef(null);
  const [buttonHeight, setButtonHeight] = useState("auto");
  const [windowWidth, setWindowWidth] = useState(window.innerWidth);

  useEffect(() => {
    // Function to update window width in state
    const handleWindowResize = () => {
      setWindowWidth(window.innerWidth);
    };

    // Add event listener for window resize
    window.addEventListener("resize", handleWindowResize);

    // Clean up the event listener when the component unmounts
    return () => {
      window.removeEventListener("resize", handleWindowResize);
    };
  }, []);

  useEffect(() => {
    //Handles Delete button resizeing in case its parent container grows  due to Todod text being broken into multipl lines
    const pElement = pRef.current;
    if (pElement) {
      const pHeight = pElement.offsetHeight;
      const newButtonHeight = Math.max(pHeight, 40);
      setButtonHeight(newButtonHeight + "px");
    }
  }, [windowWidth]);

  return (
    <div
      className={`d-flex align-items-center ps-2 justify-content-between todo-item rounded my-2 ${
        isDone ? "selected" : null
      }`}
    >
      <MDBCheckbox
        name="inlineCheck"
        id="inlineCheckbox1"
        value="option1"
        label=""
        onChange={onToggle}
        checked={isDone}
        inline
      />
      <p ref={pRef} className="my-auto">
        {text}
      </p>
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
