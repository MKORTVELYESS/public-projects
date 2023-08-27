import TextContainer from "./TextContainer";
import CardsContainer from "./CardsContainer";
function TopComponent() {
  return (
    <section>
      <div className="main-container">
        <TextContainer />
        <CardsContainer />
      </div>
    </section>
  );
}
export default TopComponent;
