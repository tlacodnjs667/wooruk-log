document.addEventListener("DOMContentLoaded", () => {
  const usernameInput = document.querySelector("input#usernameInput");
  const passwordInput = document.querySelector("input#passwordInput");
  const passwordCheckInput = document.querySelector("input#passwordCheckInput");
  const emailInput = document.querySelector("input#emailInput");
  const submitBtn = document.querySelector("input#complete-btn");

  let valid = false;

  submitBtn.disabled = true;
  usernameInput.addEventListener("keyup", validate);
  passwordInput.addEventListener("keyup", validate);
  passwordCheckInput.addEventListener("keyup", validate);
  emailInput.addEventListener("keyup", validate);

  function validate() {
    submitBtn.disabled = !((usernameInput.value.length > 0) && (
        passwordInput.value.length > 0 && passwordCheckInput.value.length > 0
        && passwordCheckInput.value == passwordInput.value
    ) && (emailInput.value.length > 0));
  }
})