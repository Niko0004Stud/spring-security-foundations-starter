import { login } from "../api.js";

export function createLogin(onLogin) {
    const section = document.createElement("section");

    section.innerHTML = `
        <h1>My Notes</h1>
        <h2>Log in</h2>

        <form>
            <input name="username"
                   placeholder="Username"
                   autocomplete="username"
                   required>

            <input name="password"
                   type="password"
                   placeholder="Password"
                   autocomplete="current-password"
                   required>

            <button type="submit">Log in</button>
        </form>

        <p class="message"></p>
    `;

    const form = section.querySelector("form");
    const message = section.querySelector(".message");

    form.addEventListener("submit", async (event) => {
        event.preventDefault();

        try {
            await login(new FormData(form));
            await onLogin();
        } catch (error) {
            message.textContent = "Could not log in.";
            console.error(error);
        }
    });

    return section;
}
