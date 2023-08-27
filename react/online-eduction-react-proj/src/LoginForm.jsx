import PrimaryButton from "./PrimaryButton.jsx";
import EmailInput from "./EmailInput.jsx";
import CurrentPassword from "./CurrentPassword.jsx";
import SingUp from "./SignUp.jsx";
const buttonProps = {
  buttonText: "Sign in",
  buttonType: "submit",
};
function LoginForm() {
  return (
    <form action="">
      <EmailInput />
      <CurrentPassword />
      <div className="login-cta-container">
        <SingUp />
        <PrimaryButton {...buttonProps} />
      </div>
    </form>
  );
}
export default LoginForm;
