// HARDCODED CREDENTIALS
const ADMIN_USERNAME = 'admin';
const ADMIN_PASSWORD = 'bilensven2024';

// Login form handler
document.getElementById('login-form').addEventListener('submit', function(e) {
    e.preventDefault();

    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;
    const errorAlert = document.getElementById('error-alert');

    // Check credentials
    if (username === ADMIN_USERNAME && password === ADMIN_PASSWORD) {
        // Success - save to sessionStorage
        sessionStorage.setItem('adminLoggedIn', 'true');
        sessionStorage.setItem('adminUsername', username);

        // Redirect to admin panel
        window.location.href = 'admin.html';
    } else {
        // Error - show message
        errorAlert.textContent = 'Forkert brugernavn eller adgangskode';
        errorAlert.style.display = 'block';

        // Clear password field
        document.getElementById('password').value = '';

        // Hide error after 3 seconds
        setTimeout(() => {
            errorAlert.style.display = 'none';
        }, 3000);
    }
});