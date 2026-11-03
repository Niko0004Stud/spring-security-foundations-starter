package ek.osnb.resource.note;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

interface NoteRepository extends JpaRepository<Note, Long> {

    List<Note> findByOwner(String owner);
}
