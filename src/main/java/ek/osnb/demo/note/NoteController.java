package ek.osnb.demo.note;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
class NoteController {

    private final NoteRepository notes;

    NoteController(NoteRepository notes) {
        this.notes = notes;
    }

    @GetMapping("/api/notes")
    List<Note> myNotes(Authentication authentication) {
        return notes.findByOwner(authentication.getName());
    }

    // A form post (text=...), just like the login form
    @PostMapping("/api/notes")
    @ResponseStatus(HttpStatus.CREATED)
    Note addNote(@RequestParam String text, Authentication authentication) {
        return notes.save(new Note(text, authentication.getName()));
    }

    // Only for admins, see SecurityConfig
    @GetMapping("/api/admin/notes")
    List<Note> allNotes() {
        return notes.findAll();
    }
}
