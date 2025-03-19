document.addEventListener('DOMContentLoaded', function () {
    const signupForm = document.querySelector('section');
    signupForm.style.opacity = 0;
  
    setTimeout(() => {
      signupForm.style.transition = 'opacity 1s ease-in-out';
      signupForm.style.opacity = 1;
    }, 500);
  
    const signupButton = document.querySelector('button');
    signupButton.addEventListener('click', function () {
      const emailInput = document.querySelector('input[type="email"]');
      const passwordInput = document.querySelector('input[type="password"]');
      const confirmPasswordInput = document.querySelector('input[type="password"][name="confirm-password"]');

      function validateEmail(email) {
            return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
        }
      function validatePassword(password) {
            return password.length >= 6;
        }

      if (!validateEmail(emailInput) && !validatePassword(passwordInput) && !validatePassword(confirmPasswordInput))  {
        signupForm.classList.add('shake');
  
        setTimeout(() => {
          signupForm.classList.remove('shake');
        }, 1000);
      }
    });
  });
  