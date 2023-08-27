function Card({ cardIcon, cardTitle, cardDescription }) {
  return (
    <div className="card">
      <div className="svg-container">
        <img src={cardIcon} />
      </div>

      <h2>{cardTitle}</h2>

      <p>{cardDescription}</p>
    </div>
  );
}
export default Card;
