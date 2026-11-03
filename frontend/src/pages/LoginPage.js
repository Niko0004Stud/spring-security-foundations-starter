import { login } from "../api.js";
import { refreshUser } from "../auth.js";
import { router } from "../router.js";

export function LoginPage() {
    const element = document.createElement("section");

    element.innerHTML = `
        <h1>Log in</h1>

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

        <!-- TODO Part 5: add a "Log in with GitHub" link here -->

        <p class="message"></p>
    `;

    const form = element.querySelector("form");
    const message = element.querySelector(".message");

    form.addEventListener("submit", async (event) => {
        event.preventDefault();
        message.textContent = "";

        try {
            await login(new FormData(form));
            await refreshUser();
            router.navigate("/notes");
        } catch (error) {
            message.textContent = error.status === 401
                ? "Wrong username or password."
                : "Could not log in.";
            console.error(error);
        }
    });

    return {
        element,
        mount() {
            form.elements.username.focus();
        }
    };
}
