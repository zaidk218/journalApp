package com.zaid.journalApp.controller;

import com.zaid.journalApp.entity.Journal;
import com.zaid.journalApp.service.JournalService;
import org.apache.coyote.Response;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/journals")
public class JournalController{

    @Autowired
    private JournalService journalService;

    @GetMapping
    public ResponseEntity<List<Journal>> getJournalsByUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        try {
            List<Journal> journals = journalService.getJournalsByUsername(username);
            return ResponseEntity.ok(journals);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/journals/test-auth")
    public ResponseEntity<String> testAuthentication() {
        // Detailed authentication context logging
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return ResponseEntity.ok(
                "Authenticated as: " + authentication.getName() +
                        ", Authorities: " + authentication.getAuthorities() +
                        ", Details: " + authentication.getDetails() +
                        ", Credentials: " + authentication.getCredentials() +
                        ", Principal: " + authentication.getPrincipal() +
                        ", Authenticated: " + authentication.isAuthenticated() +
                        ", Authenticated Object: " + authentication
        );
    }



    @PostMapping
    public ResponseEntity<Journal> createJournal(@RequestBody Journal journal) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        try {
            Journal createdJournal = journalService.createJournal(journal, username);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdJournal);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/{journalId}")
    public ResponseEntity<Journal> getJournal(@PathVariable ObjectId journalId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        try {
            Journal journal = journalService.getJournal(journalId, username);
            return ResponseEntity.ok(journal);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }



    @PutMapping("/{journalId}")
    public ResponseEntity<Journal> updateJournal(
            @PathVariable ObjectId journalId,
            @RequestBody Journal journal) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        try {
            Journal updatedJournal = journalService.updateJournal(journalId, journal, username);
            return ResponseEntity.ok(updatedJournal);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{journalId}")
    public ResponseEntity<Void> deleteJournal(@PathVariable ObjectId journalId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        boolean isDeleted=journalService.deleteJournal(journalId,username);
        return isDeleted
                ? ResponseEntity.noContent().build()
                :ResponseEntity.notFound().build();
    }
}





































































