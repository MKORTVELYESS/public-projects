import { Navbar, Nav, NavDropdown } from "react-bootstrap";
import "./MyNavbar.css";
function MyNavbar({}) {
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
              <NavDropdown.Item>Mark all as Undone</NavDropdown.Item>
              <NavDropdown.Item>Mark all as Done</NavDropdown.Item>
              <NavDropdown.Divider />
              <NavDropdown.Item href="#action-3">Clear list</NavDropdown.Item>
            </NavDropdown>
          </Nav>
        </Navbar.Collapse>
      </Navbar>
    </div>
  );
}

export default MyNavbar;
