package com.zaid.journalApp.controller;

import com.zaid.journalApp.entity.Journal;
import com.zaid.journalApp.service.JournalService;
import org.apache.coyote.Response;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users/{username}/journals")
public class JournalController{

    @Autowired
    private JournalService journalService;

    @GetMapping
    public ResponseEntity<List<Journal>> getJournalsByUser(@PathVariable String username){
        List<Journal>journals=journalService.getJournalsByUsername(username);
        return ResponseEntity.ok(journals);
    }

    @PostMapping
    public ResponseEntity<Journal> createJournal(@RequestBody Journal journal,@PathVariable String username){
        Journal createdJournal=journalService.createJournal(journal,username);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdJournal);
    }

    @GetMapping("/{journalId}")
    public ResponseEntity<Journal> getJournal(@PathVariable ObjectId journalId){
        Journal journal=journalService.getJournal(journalId);
        return journal !=null ? ResponseEntity.ok(journal) :ResponseEntity.notFound().build();
    }

    @PutMapping("/{journalId}")
    public ResponseEntity<Journal> updateJournal(@PathVariable ObjectId journalId,@RequestBody Journal journal,@PathVariable String username){
        Journal updatedJournal=journalService.updateJournal(journalId,journal,username);
        return updatedJournal!=null ? ResponseEntity.ok(updatedJournal) :ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{journalId}")
    public ResponseEntity<Void> deleteJournal(@PathVariable ObjectId journalId,@PathVariable String username){
        boolean isDeleted=journalService.deleteJournal(journalId,username);
        return isDeleted ? ResponseEntity.noContent().build() :ResponseEntity.notFound().build();
    }
}





































































