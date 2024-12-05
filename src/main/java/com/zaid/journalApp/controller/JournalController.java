package com.zaid.journalApp.controller;

import com.zaid.journalApp.entity.Journal;
import com.zaid.journalApp.service.JournalService;
import com.zaid.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/journal")
public class JournalController {

    @Autowired
    private JournalService journalService;

    @Autowired
    private UserService userService;

    @GetMapping("{username}")
    public ResponseEntity<List<Journal>> getJournalsByUser(@PathVariable String username) {
        List<Journal> journals = journalService.getJournalsByUsername(username);
        return (journals != null && !journals.isEmpty())
                ? ResponseEntity.ok(journals)
                : ResponseEntity.notFound().build();
    }

    @PostMapping("{username}")
    public ResponseEntity<Journal> createJournal(@RequestBody Journal journal, @PathVariable String username) {
        Journal createdJournal = journalService.createJournal(journal, username);
        return (createdJournal != null)
                ? ResponseEntity.status(HttpStatus.CREATED).body(createdJournal)
                : ResponseEntity.badRequest().build();
    }

    @GetMapping("/id/{myId}")
    public ResponseEntity<Journal> getJournal(@PathVariable ObjectId myId) {
        Journal journal = journalService.getJournal(myId);
        return journal != null ? ResponseEntity.ok(journal) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/id/{username}/{myId}")
    public ResponseEntity<Void> deleteJournal(@PathVariable String username, @PathVariable ObjectId myId) {
        boolean deleted = journalService.deleteJournal(myId, username);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @PutMapping("/id/{username}/{myId}")
    public ResponseEntity<Journal> updateJournal(@PathVariable String username, @PathVariable ObjectId myId, @RequestBody Journal journal) {
        Journal updatedJournal = journalService.updateJournal(myId, journal, username);
        return updatedJournal != null ? ResponseEntity.ok(updatedJournal) : ResponseEntity.notFound().build();
    }
}
