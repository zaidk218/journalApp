package com.zaid.journalApp.controller;

import com.zaid.journalApp.entity.Journal;
import com.zaid.journalApp.service.JournalService;
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

    @GetMapping
    public ResponseEntity<List<Journal>> getJournals() {
        List<Journal> journals = journalService.getAllJournals();
        if (journals != null && !journals.isEmpty()) {
            return ResponseEntity.ok(journals);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Journal> createJournal(@RequestBody Journal journal) {
        if (journal == null || journal.getTitle() == null || journal.getContent() == null) {
            return ResponseEntity.badRequest().build(); // 400 Bad Request
        }
        journal.setDate(new java.util.Date());
        journalService.saveJournal(journal);
        return ResponseEntity.status(HttpStatus.CREATED).body(journal); // 201 Created
    }

    @GetMapping("/id/{myId}")
    public ResponseEntity<Journal> getJournal(@PathVariable ObjectId myId) {
        Journal journal = journalService.getJournal(myId);
        return journal != null ? ResponseEntity.ok(journal) : ResponseEntity.notFound().build(); // 404 Not Found
    }

    @DeleteMapping("/id/{myId}")
    public ResponseEntity<Void> deleteJournal(@PathVariable ObjectId myId) {
        boolean deleted = journalService.deleteJournal(myId);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build(); // 204 No Content or 404 Not Found
    }

    @PutMapping("/id/{myId}")
    public ResponseEntity<Journal> updateJournal(@PathVariable ObjectId myId, @RequestBody Journal journal) {
        if (journal == null || journal.getTitle().isEmpty() || journal.getContent().isEmpty()) {
            return ResponseEntity.badRequest().build(); // 400 Bad Request
        }
        journal.setDate(new java.util.Date());
        Journal updatedJournal = journalService.updateJournal(myId, journal);
        return updatedJournal != null ? ResponseEntity.ok(updatedJournal) : ResponseEntity.notFound().build(); // 404 Not Found
    }
}


























//package com.zaid.journalApp.controller;
//
//import com.mongodb.lang.NonNull;
//import com.zaid.journalApp.entity.JournalEntry;
//import com.zaid.journalApp.service.JournalEntryService;
//import org.bson.types.ObjectId;
//import org.jetbrains.annotations.NotNull;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//
//import java.util.List;
//
//
//@RestController
//@RequestMapping("/journal")
//public class JournalEntryControllerV2 {
//
//    @Autowired
//    private JournalEntryService journalEntryService;
//    @GetMapping()
//    public List<JournalEntry> getJournalEntries() {
//        return journalEntryService.getAllJournalEntries();
//    }
//
//    @PostMapping
//    public JournalEntry createJournalEntry(@RequestBody @NonNull JournalEntry journalEntry) {
//        journalEntry.setDate(new java.util.Date());
//         journalEntryService.saveEntry(journalEntry);
//         return journalEntry;
//    }
//
//    @GetMapping("/id/{myId}")
//    public JournalEntry getJournalEntry(@PathVariable ObjectId myId) {
//        return journalEntryService.getJournalEntry(myId);
//    }
//    @DeleteMapping("/id/{myId}")
//    public boolean deleteJournalEntry(@PathVariable ObjectId myId) {
//         journalEntryService.deleteJournalEntry(myId);
//         return true;
//    }
//
//    @PutMapping("/id/{myId}")
//    public JournalEntry updateJournalEntry(@PathVariable ObjectId myId, @RequestBody @NotNull JournalEntry journalEntry) {
//        journalEntry.setDate(new java.util.Date());
//        return journalEntryService.updateJournalEntry(myId, journalEntry);
//    }
//} "