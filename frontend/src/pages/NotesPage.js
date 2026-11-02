import { addNote, getNotes } from "../api.js";
import { NoteList } from "../components/NoteList.js";
import { handleAuthError } from "../guards.js";

export function NotesPage() {
    const element = document.createElement("section");

    element.innerHTML = `
        <h1>My notes</h1>

        <div class="notes"></div>

        <form>
            <input name="text"
                   placeholder="New note..."
                   required>
            <button type="submit">Add note</button>
        </form>

        <p class="message"></p>
    `;

    const notes = element.querySelector(".notes");
    const form = element.querySelector("form");
    const message = element.querySelector(".message");

    function showError(text, error) {
        if (handleAuthError(error)) {
            return;
        }

        message.textContent = `${text}: ${error.message}`;
        console.error(error);
    }

    async function loadNotes() {
        try {
            notes.replaceChildren(NoteList(await getNotes()));
        } catch (error) {
            showError("Could not load notes", error);
        }
    }

    form.addEventListener("submit", async (event) => {
        event.preventDefault();
        message.textContent = "";

        try {
            await addNote(new FormData(form));
            form.reset();
            await loadNotes();
        } catch (error) {
            showError("Could not add note", error);
        }
    });

    return { element, mount: loadNotes };
}
