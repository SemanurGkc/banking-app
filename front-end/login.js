const AUTH_API = "http://localhost:8080/api/auth";

document.getElementById("loginForm").addEventListener("submit", async (e) => {
    e.preventDefault();

    const username = document.getElementById("username").value.trim();
    const password = document.getElementById("password").value;
    const errorDiv = document.getElementById("error");
    const successDiv = document.getElementById("success");
    const loginBtn = document.getElementById("loginBtn");

    errorDiv.style.display = "none";
    successDiv.style.display = "none";

    loginBtn.disabled = true;
    loginBtn.textContent = "Logging in...";

    try {
        const res = await fetch(`${AUTH_API}/login`, {
            method: "POST",
            credentials: "include",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({ username, password })
        });

        if (!res.ok) {
            const error = await res.json();
            throw new Error(error.error || "Login failed");
        }

        const data = await res.json();

        localStorage.setItem("userId", data.userId);
        localStorage.setItem("username", data.username);

        successDiv.textContent = "Login successful! Redirecting...";
        successDiv.style.display = "block";

        setTimeout(() => {
            window.location.href = "dashboard.html";
        }, 800);

    } catch (err) {
        errorDiv.textContent = err.message || "Username or password is incorrect.";
        errorDiv.style.display = "block";
        loginBtn.disabled = false;
        loginBtn.textContent = "Login";
    }
});