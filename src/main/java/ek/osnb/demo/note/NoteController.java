package ek.osnb.demo.note;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
class NoteController {

    private final NoteRepository notes;

    NoteController(NoteRepository notes) {
        this.notes = notes;
    }

    // TODO Exercise 3: Return only the notes owned by the logged-in user
    @GetMapping("/api/notes")
    List<Note> myNotes(Authentication auth) {
        return notes.findByOwner(auth.getName());
    }

    // TODO Exercise 3: Save the note with the logged-in user as its owner
    // A form post (text=...), just like the login form
    @PostMapping("/api/notes")
    @ResponseStatus(HttpStatus.CREATED)
    Note addNote(@RequestParam String text, Authentication auth) {
        //throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
        Note newNote = new Note(text, auth.getName());
        return notes.save(newNote);
    }

    @GetMapping("/api/admin/notes")
    List<Note> allNotes() {
        return notes.findAll();
    }
}
