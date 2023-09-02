import "./App.css";
import MyNavbar from "./MyNavbar";
import ViteReact from "./ViteReact";
import MainContainer from "./MainContainer";
// const [headerTriggeredState, setHeaderTriggeredState] = useState("");

// const getHeaderAction = (action) => {
//   //Gets the action in the navbar into the state of the App
//   setHeaderTriggeredState(action);
// };

function App() {
  return (
    <>
      <MyNavbar /*onHeaderAction={getHeaderAction}*/ />
      <MainContainer />
      <ViteReact />
    </>
  );
}

export default App;
