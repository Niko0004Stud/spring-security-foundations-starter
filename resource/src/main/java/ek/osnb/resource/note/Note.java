package ek.osnb.resource.note;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;

    // The username of the user who wrote the note
    private String owner;

    protected Note() {
    }

    public Note(String text, String owner) {
        this.text = text;
        this.owner = owner;
    }

    public Long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public String getOwner() {
        return owner;
    }
}
