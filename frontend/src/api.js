export class ApiError extends Error {
    constructor(status) {
        super(`HTTP ${status}`);
        this.status = status;
    }
}

// Spring writes the token to the XSRF-TOKEN cookie. A page on another site can
// make the browser send that cookie, but it can't read it and copy it to a header.
function csrfToken() {
    const match = document.cookie.match(/(?:^|;\s*)XSRF-TOKEN=([^;]+)/);

    return match ? decodeURIComponent(match[1]) : "";
}

async function request(path, { method = "GET", headers = {}, body } = {}) {
    const allHeaders = method === "GET"
        ? headers
        : { ...headers, "X-XSRF-TOKEN": csrfToken() };

    const response = await fetch(path, { method, headers: allHeaders, body });

    if (!response.ok) {
        throw new ApiError(response.status);
    }

    return response.status === 204 ? null : response.json();
}

// A 401 here is a normal answer: nobody is logged in
export async function getMe() {
    try {
        return await request("/api/me");
    } catch (error) {
        if (error.status === 401) {
            return null;
        }
        throw error;
    }
}

// Spring's formLogin reads form fields, so this is not JSON
export function login(formData) {
    return request("/api/login", {
        method: "POST",
        body: new URLSearchParams(formData)
    });
}

export function logout() {
    return request("/api/logout", { method: "POST" });
}

export function getNotes() {
    return request("/api/notes");
}

export function getAllNotes() {
    return request("/api/admin/notes");
}

// The controller reads @RequestBody, so this is JSON
export function addNote(formData) {
    return request("/api/notes", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ text: formData.get("text") })
    });
}
