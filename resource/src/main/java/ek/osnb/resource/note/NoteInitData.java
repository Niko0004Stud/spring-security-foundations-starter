package ek.osnb.resource.note;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
class NoteInitData implements CommandLineRunner {

    private final NoteRepository notes;

    NoteInitData(NoteRepository notes) {
        this.notes = notes;
    }

    @Override
    public void run(String... args) {
        notes.save(new Note("Buy coffee", "user1"));
        notes.save(new Note("Prepare the exam project", "user2"));
        notes.save(new Note("Secret birthday present", "user1"));
    }
}
