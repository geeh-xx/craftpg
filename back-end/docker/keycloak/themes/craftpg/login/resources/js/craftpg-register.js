(function () {
  function hideFieldGroup(field) {
    if (!field) return;

    const form = document.getElementById('kc-register-form');
    let current = field.parentElement;

    while (current && current !== form) {
      const className = typeof current.className === 'string' ? current.className : '';
      if (
        className.includes('pf-v5-c-form__group') ||
        className.includes('form-group') ||
        className.includes('kcFormGroupClass')
      ) {
        current.style.display = 'none';
        return;
      }
      current = current.parentElement;
    }

    if (field.parentElement) {
      field.parentElement.style.display = 'none';
    }
  }

  function keepOnlyRequiredRegistrationFields() {
    const registerForm = document.getElementById('kc-register-form');
    if (!registerForm) return;

    const allowedNames = new Set(['firstName', 'email', 'password', 'password-confirm']);
    const fields = registerForm.querySelectorAll('input, select, textarea');

    fields.forEach((field) => {
      const name = field.getAttribute('name') || '';
      const type = (field.getAttribute('type') || '').toLowerCase();

      if (type === 'hidden' || type === 'submit' || type === 'button') return;

      if (name === 'lastName' || name === 'username') {
        hideFieldGroup(field);
        return;
      }

      if (!allowedNames.has(name)) {
        hideFieldGroup(field);
      }
    });

    const firstNameField = registerForm.querySelector('input[name="firstName"]');
    const emailField = registerForm.querySelector('input[name="email"]');
    const usernameField = registerForm.querySelector('input[name="username"]');
    const lastNameField = registerForm.querySelector('input[name="lastName"]');

    if (usernameField) {
      const syncUsername = function () {
        const emailValue = emailField && emailField.value ? emailField.value.trim() : '';
        usernameField.value = emailValue;
      };

      syncUsername();
      if (emailField) {
        emailField.addEventListener('input', syncUsername);
        emailField.addEventListener('change', syncUsername);
      }
      registerForm.addEventListener('submit', syncUsername);
    }

    if (lastNameField) {
      const syncLastName = function () {
        const firstNameValue = firstNameField && firstNameField.value ? firstNameField.value.trim() : '';
        lastNameField.value = firstNameValue || 'User';
      };

      syncLastName();
      if (firstNameField) {
        firstNameField.addEventListener('input', syncLastName);
        firstNameField.addEventListener('change', syncLastName);
      }
      registerForm.addEventListener('submit', syncLastName);
    }
  }

  if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', keepOnlyRequiredRegistrationFields);
  } else {
    keepOnlyRequiredRegistrationFields();
  }
})();
