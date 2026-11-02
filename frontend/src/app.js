import { createLogin } from "./components/Login.js";
import { createNotes } from "./components/Notes.js";
import { createUserInfo } from "./components/UserInfo.js";
import { getMe } from "./api.js";

window.addEventListener("DOMContentLoaded", render);


async function render() {
    const app = document.querySelector("#app");

    app.replaceChildren();

    try {
        const me = await getMe();

        if (!me) {
            app.append(createLogin(render));
            return;
        }

        const heading = document.createElement("h1");
        heading.textContent = "My Notes";

        app.append(
            heading,
            createUserInfo(me, render),
            createNotes(me)
        );

    } catch (error) {
        app.textContent = "Could not load application.";
        console.error(error);
    }
}
