export function NoteList(notes) {
    const list = document.createElement("ul");
    list.id = "note-list";

    for (const note of notes) {
        const item = document.createElement("li");
        item.textContent = `${note.text} (${note.owner})`;
        list.append(item);
    }

    return list;
}
