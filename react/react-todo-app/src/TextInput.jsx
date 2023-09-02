import React from "react";
import { useState } from "react";
const InputPage = ({ onAdd }) => {
  const [inputText, setInputText] = useState("");
  const handleInputChange = (event) => {
    setInputText(event.target.value);
  };
  const handleButtonClick = () => {
    onAdd(inputText);
    setInputText("");
  };
  return (
    <>
      <div className="input-group mb-3">
        <input
          type="text"
          className="form-control"
          placeholder="e.g. Do the Dishes"
          aria-label="User's new todo to be added"
          aria-describedby="basic-addon2"
          onChange={handleInputChange}
          value={inputText}
        ></input>
        <div className="input-group-append">
          <button
            onClick={handleButtonClick}
            className="btn btn-outline-secondary"
            type="button"
          >
            Add
          </button>
        </div>
      </div>
    </>
  );
};

export default InputPage;
