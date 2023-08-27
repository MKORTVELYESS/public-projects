import Card from "./Card.jsx";
import CARDS_PROPS from "./CARDS_PROPS.jsx";
function CardsContainer() {
  return (
    <div className="column cards-container">
      {CARDS_PROPS.map((cardProps, index) => {
        return <Card key={`card-${index}`} {...cardProps} />;
      })}
    </div>
  );
}
export default CardsContainer;
