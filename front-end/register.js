const AUTH_API = "http://localhost:8080/api/auth";

document.getElementById('registerForm').addEventListener('submit', async function(e) {
    e.preventDefault();

    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;
    const confirmPassword = document.getElementById('confirmPassword').value;
    const errorDiv = document.getElementById('error');
    const successDiv = document.getElementById('success');
    const registerBtn = document.getElementById('registerBtn');

    // Hide previous messages
    errorDiv.style.display = 'none';
    successDiv.style.display = 'none';

    // Validate passwords match
    if (password !== confirmPassword) {
        errorDiv.textContent = 'Passwords do not match!';
        errorDiv.style.display = 'block';
        return;
    }

    // Disable button
    registerBtn.disabled = true;
    registerBtn.textContent = 'Signing up...';

    try {
        const response = await fetch(AUTH_API + '/register', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                username: username,
                password: password
            })
        });

        const data = await response.json();

        if (response.ok || response.status === 201) {
            // Registration successful
            successDiv.textContent = 'Registration successful! Redirecting to the login page...';
            successDiv.style.display = 'block';

            // Redirect to login after 2 seconds
            setTimeout(() => {
                window.location.href = 'login.html';
            }, 2000);
        } else {
            // Registration failed
            errorDiv.textContent = data.error || 'Registration failed. Username might already be taken.';
            errorDiv.style.display = 'block';
            registerBtn.disabled = false;
            registerBtn.textContent = 'Register';
        }
    } catch (error) {
        console.error('Register error:', error);
        errorDiv.textContent = 'Connection error. Please try again later.';
        errorDiv.style.display = 'block';
        registerBtn.disabled = false;
        registerBtn.textContent = 'Register';
    }
});