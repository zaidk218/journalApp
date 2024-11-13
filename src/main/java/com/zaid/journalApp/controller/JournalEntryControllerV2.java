package com.zaid.journalApp.controller;

import com.zaid.journalApp.entity.JournalEntry;
import com.zaid.journalApp.service.JournalEntryService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/journal")
public class JournalEntryControllerV2 {

    @Autowired
    private JournalEntryService journalEntryService;

    @GetMapping
    public ResponseEntity<List<JournalEntry>> getJournalEntries() {
        List<JournalEntry> entries = journalEntryService.getAllJournalEntries();
        if(entries!=null && !entries.isEmpty()){
            return  ResponseEntity.ok(entries) ;
        }
        return ResponseEntity.notFound().build();

    }

    @PostMapping
    public ResponseEntity<JournalEntry> createJournalEntry(@RequestBody JournalEntry journalEntry) {
        if (journalEntry == null || journalEntry.getTitle() == null || journalEntry.getContent() == null) {
            return ResponseEntity.badRequest().build(); // 400 Bad Request
        }
        journalEntry.setDate(new java.util.Date());
        journalEntryService.saveEntry(journalEntry);
        return ResponseEntity.status(HttpStatus.CREATED).body(journalEntry); // 201 Created
    }

    @GetMapping("/id/{myId}")
    public ResponseEntity<JournalEntry> getJournalEntry(@PathVariable ObjectId myId) {
        JournalEntry entry = journalEntryService.getJournalEntry(myId);
        return entry != null ? ResponseEntity.ok(entry) : ResponseEntity.notFound().build(); // 404 Not Found
    }

    @DeleteMapping("/id/{myId}")
    public ResponseEntity<Void> deleteJournalEntry(@PathVariable ObjectId myId) {
        boolean deleted = journalEntryService.deleteJournalEntry(myId);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build(); // 204 No Content or 404 Not Found
    }

    @PutMapping("/id/{myId}")
    public ResponseEntity<JournalEntry> updateJournalEntry(@PathVariable ObjectId myId, @RequestBody JournalEntry journalEntry) {
        if (journalEntry == null || journalEntry.getTitle().equals("") || journalEntry.getContent().equals("")) {
            return ResponseEntity.badRequest().build(); // 400 Bad Request
        }
        journalEntry.setDate(new java.util.Date());
        JournalEntry updatedEntry = journalEntryService.updateJournalEntry(myId, journalEntry);
        return updatedEntry != null ? ResponseEntity.ok(updatedEntry) : ResponseEntity.notFound().build(); // 404 Not Found
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
//}
