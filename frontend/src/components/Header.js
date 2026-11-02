import { logout } from "../api.js";
import { getCurrentUser, refreshUser, subscribeToUser } from "../auth.js";
import { router } from "../router.js";

export function initHeader() {
    const header = document.querySelector("header");

    function renderHeader(user) {
        // Hiding a link is not authorization: Spring still protects /api/admin/**
        const isAdmin = user?.roles.includes("ROLE_ADMIN");

        header.innerHTML = `
            <strong>My Notes</strong>
            <nav>
                <a href="/" data-navigo>Home</a>
                ${user ? `<a href="/notes" data-navigo>Notes</a>` : ""}
                ${isAdmin ? `<a href="/admin" data-navigo>Admin</a>` : ""}
                ${user ? "" : `<a href="/login" data-navigo>Log in</a>`}
            </nav>
        `;

        if (user) {
            header.append(createUserInfo(user));
        }

        // Navigo only sets up new links after a route handler, and this isn't one
        router.updatePageLinks();
    }

    renderHeader(getCurrentUser());
    subscribeToUser(renderHeader);
}

function createUserInfo(user) {
    const div = document.createElement("div");
    div.classList.add("header-user");

    div.innerHTML = `
        <span></span>
        <button type="button">Log out</button>
    `;

    div.querySelector("span").textContent = `${user.username} (${user.roles.join(", ")})`;

    div.querySelector("button").addEventListener("click", async () => {
        try {
            await logout();
            // Spring deleted the session and the CSRF token, and this gets a new token
            await refreshUser();
            router.navigate("/login");
        } catch (error) {
            console.error("Logout failed:", error);
        }
    });

    return div;
}
