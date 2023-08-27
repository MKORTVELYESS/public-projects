function PrimaryButton({ buttonText, buttonType = "button" }) {
  return <button type={buttonType}>{buttonText}</button>;
}
export default PrimaryButton;
