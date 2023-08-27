import PrimaryButton from "./PrimaryButton";
const buttonProps = {
  buttonText: "Click to get a 5!",
};
function TextContainer() {
  return (
    <div className="column section-text-container">
      <h1>The Final Test</h1>
      <p>
        Lorem ipsum dolor sit amet consectetur adipisicing elit. Tempora
        voluptates, molestias reprehenderit sapiente, quisquam quas numquam
        saepe fugiat veritatis minus natus libero, sunt similique repellat
        ducimus perferendis rerum? Sunt reiciendis vel praesentium assumenda
        quam eum esse cumque quidem at, illo, facere fugit nostrum modi quae
        quaerat quod, ipsam eos repellendus?
      </p>
      <PrimaryButton {...buttonProps} />
    </div>
  );
}
export default TextContainer;
