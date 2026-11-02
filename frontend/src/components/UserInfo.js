import { logout } from "../api.js";

export function createUserInfo(me, onLogout) {
    const section = document.createElement("section");

    section.innerHTML = `
        <p>Logged in as <strong></strong></p>
        <button type="button">Log out</button>
    `;

    section.querySelector("strong").textContent = me.username;

    section.querySelector("button").addEventListener(
        "click",
        async () => {
            try {
                await logout();
                await onLogout();
            } catch (error) {
                console.error(error);
                alert("Could not log out.");
            }
        }
    );

    return section;
}
