package ek.osnb.resource.note;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// The resource server has no security yet, so it doesn't know who is calling.
// Only the BFF should call it, with a JWT that says who the user is.
@RestController
class NoteController {

    private final NoteRepository notes;

    NoteController(NoteRepository notes) {
        this.notes = notes;
    }

    @GetMapping("/api/notes")
    List<Note> myNotes() {
        // TODO Part 2: only return the notes of the user in the JWT
        return notes.findAll();
    }

    record CreateNoteRequest(String text) {}

    @PostMapping("/api/notes")
    @ResponseStatus(HttpStatus.CREATED)
    Note addNote(@RequestBody CreateNoteRequest request) {
        // TODO Part 2: the owner is the user in the JWT
        return notes.save(new Note(request.text(), "ANONYMOUS"));
    }

    // TODO Part 2: only admins may call this
    @GetMapping("/api/admin/notes")
    List<Note> allNotes() {
        return notes.findAll();
    }
}
