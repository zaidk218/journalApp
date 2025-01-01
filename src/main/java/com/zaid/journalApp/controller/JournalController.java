package com.zaid.journalApp.controller;

import com.zaid.journalApp.entity.Journal;
import com.zaid.journalApp.service.JournalService;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class JournalController{

    @Autowired
    private JournalService journalService;

    @GetMapping
    public ResponseEntity<List<Journal>> getJournalsByUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        log.info("Request to fetch journals for user: {}", username);

        try {
            List<Journal> journals = journalService.getJournalsByUsername(username);
            log.debug("Retrieved {} journals for user: {}", journals.size(), username);
            return ResponseEntity.ok(journals);
        } catch (IllegalArgumentException e) {
            log.error("Error fetching journals for user: {}", username, e);
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
        log.info("Request to create journal for user: {}", username);

        try {
            Journal createdJournal = journalService.createJournal(journal, username);
            log.info("Successfully created journal with ID: {} for user: {}",
                    createdJournal.getId(), username);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdJournal);
        } catch (IllegalArgumentException e) {
            log.error("Error creating journal for user: {}", username, e);
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/{journalId}")
    public ResponseEntity<Journal> getJournal(@PathVariable ObjectId journalId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        log.info("Request to fetch journal: {} by user: {}", journalId, username);

        try {
            Journal journal = journalService.getJournal(journalId, username);
            log.info("Successfully retrieved journal: {} for user: {}", journalId, username);
            return ResponseEntity.ok(journal);
        } catch (IllegalArgumentException e) {
            log.error("Error fetching journal: {} for user: {}", journalId, username, e);
            return ResponseEntity.notFound().build();
        }
    }



    @PutMapping("/{journalId}")
    public ResponseEntity<Journal> updateJournal(
            @PathVariable ObjectId journalId,
            @RequestBody Journal journal) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        log.info("Request to update journal: {} by user: {}", journalId, username);

        try {
            Journal updatedJournal = journalService.updateJournal(journalId, journal, username);
            log.info("Successfully updated journal: {} for user: {}", journalId, username);
            return ResponseEntity.ok(updatedJournal);
        } catch (IllegalArgumentException e) {
            log.error("Error updating journal: {} for user: {}", journalId, username, e);
            return ResponseEntity.badRequest().build();
        }
    }


    @DeleteMapping("/{journalId}")
    public ResponseEntity<Void> deleteJournal(@PathVariable ObjectId journalId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        log.info("Request to delete journal: {} by user: {}", journalId, username);

        boolean isDeleted = journalService.deleteJournal(journalId, username);
        if (isDeleted) {
            log.info("Successfully deleted journal: {} for user: {}", journalId, username);
            return ResponseEntity.noContent().build();
        } else {
            log.warn("Journal not found or unauthorized access: {} for user: {}", journalId, username);
            return ResponseEntity.notFound().build();
        }
    }
}





































































