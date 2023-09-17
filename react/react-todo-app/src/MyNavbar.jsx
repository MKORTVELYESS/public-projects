import { Navbar, Nav, NavDropdown } from "react-bootstrap";
import "./MyNavbar.css";
import { TodoItemsContext } from "./TodoItemsContext";
import { TodoItemsDispatchContext } from "./TodoItemsDispatchContext";
import { useContext } from "react";
function MyNavbar({}) {
  const arrTodos = useContext(TodoItemsContext);
  const setArrTodos = useContext(TodoItemsDispatchContext);

  const handleClick = (action) => {
    let arrNewTodos;
    switch (action) {
      case "AllUndone":
        arrNewTodos = arrTodos.map((todo) => {
          return { ...todo, isDone: false };
        });
        break;
      case "AllDone":
        arrNewTodos = arrTodos.map((todo) => {
          return { ...todo, isDone: true };
        });
        break;
      case "Clear":
        arrNewTodos = [];
        break;
      default:
        console.log(`No action configured`);
    }
    console.log(arrNewTodos);
    setArrTodos(arrNewTodos);
  };
  return (
    <div className="navigation">
      <Navbar bg="light" expand="md">
        <Navbar.Brand href="#home">React Todo App</Navbar.Brand>
        <Navbar.Toggle aria-controls="basic-navbar-nav" />
        <Navbar.Collapse id="basic-navbar-nav">
          <Nav className="mr-auto">
            <Nav.Link href="#home">Home</Nav.Link>
            <Nav.Link href="#about">About</Nav.Link>
            <NavDropdown
              style={{ marginLeft: "auto" }}
              title="Actions"
              id="basic-nav-dropdown"
            >
              <NavDropdown.Item onClick={() => handleClick("AllUndone")}>
                Mark all as Undone
              </NavDropdown.Item>
              <NavDropdown.Item onClick={() => handleClick("AllDone")}>
                Mark all as Done
              </NavDropdown.Item>
              <NavDropdown.Divider />
              <NavDropdown.Item
                onClick={() => handleClick("Clear")}
                href="#action-3"
              >
                Clear list
              </NavDropdown.Item>
            </NavDropdown>
          </Nav>
        </Navbar.Collapse>
      </Navbar>
    </div>
  );
}

export default MyNavbar;
