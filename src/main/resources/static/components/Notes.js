import { getNotes, addNote } from "../api.js";

export function createNotes(me) {
    const section = document.createElement("section");

    section.innerHTML = `
        <h2>My Notes</h2>

        <button id="admin-button" type="button" hidden>
            Show everyone's notes
        </button>

        <ul id="note-list"></ul>

        <form id="note-form">
            <input name="text"
                   placeholder="New note..."
                   required>
            <button type="submit">Add note</button>
        </form>

        <p class="message"></p>
    `;

    const heading = section.querySelector("h2");
    const noteList = section.querySelector("#note-list");
    const noteForm = section.querySelector("#note-form");
    const adminButton = section.querySelector("#admin-button");
    const message = section.querySelector(".message");

    // Hiding a button is not authorization.
    // Spring Security must protect the admin endpoint.
    if (me.roles.includes("ROLE_ADMIN")) {
        adminButton.hidden = false;
    }

    async function loadNotes(url = "/api/notes") {
        try {
            const notes = await getNotes(url);

            noteList.replaceChildren();

            for (const note of notes) {
                const item = document.createElement("li");
                item.textContent = `${note.text} (${note.owner})`;
                noteList.append(item);
            }

        } catch (error) {
            message.textContent = "Could not load notes.";
            console.error(error);
        }
    }

    noteForm.addEventListener("submit", async (event) => {
        event.preventDefault();

        try {
            await addNote(new FormData(noteForm));
            noteForm.reset();
            await loadNotes();
        } catch (error) {
            message.textContent = "Could not add note.";
            console.error(error);
        }
    });

    adminButton.addEventListener("click", () => {
        heading.textContent = "Everyone's Notes";
        loadNotes("/api/admin/notes");
    });

    loadNotes();

    return section;
}
