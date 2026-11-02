import { getAllNotes } from "../api.js";
import { NoteList } from "../components/NoteList.js";
import { handleAuthError } from "../guards.js";

export function AdminPage() {
    const element = document.createElement("section");

    element.innerHTML = `
        <h1>Everyone's notes</h1>
        <div class="notes"></div>
        <p class="message"></p>
    `;

    const notes = element.querySelector(".notes");
    const message = element.querySelector(".message");

    async function loadNotes() {
        try {
            notes.replaceChildren(NoteList(await getAllNotes()));
        } catch (error) {
            if (handleAuthError(error)) {
                return;
            }

            message.textContent = `Could not load notes: ${error.message}`;
            console.error(error);
        }
    }

    return { element, mount: loadNotes };
}
