
export async function getMe() {
    const response = await fetch("/api/me");

    if (response.status === 401) {
        return null;
    }

    return await response.json();
}

export async function login(formData) {
    const res = await fetch("/api/login", {
        method: "POST",
        // Send username and password as form data
        body: new URLSearchParams(formData),
        headers: {
            // Include the CSRF token
            "X-XSRF-TOKEN": csrfToken()
        }
    });

    if (!res.ok) {
        throw new Error("Login failed");
    }
}

export async function logout() {
    const res = await fetch("/api/logout", {
        method: "POST",
        headers: {
            // Include the CSRF token
            "X-XSRF-TOKEN": csrfToken()
        }
    });

    if (!res.ok) {
        throw new Error("An error occurred while logging out");
    }
}

export async function getNotes(url = "/api/notes") {
    const response = await fetch(url);

    if (!response.ok) {
        throw new Error(`HTTP ${response.status}`);
    }

    return response.json();
}

export async function addNote(formData) {
    const response = await fetch("/api/notes", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "X-XSRF-TOKEN": csrfToken()
        },
        body: JSON.stringify({text: formData.get("text")})
    });

    if (!response.ok) {
        throw new Error(`HTTP ${response.status}`);
    }
}


function csrfToken() {
    const match = document.cookie.match(
        /(?:^|;\s*)XSRF-TOKEN=([^;]+)/
    );

    return match ? decodeURIComponent(match[1]) : "";
}
