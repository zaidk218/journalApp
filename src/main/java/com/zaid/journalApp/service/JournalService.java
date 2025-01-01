package com.zaid.journalApp.service;

import com.zaid.journalApp.entity.Journal;
import com.zaid.journalApp.entity.User;
import com.zaid.journalApp.repository.JournalRepository;
import com.zaid.journalApp.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class JournalService
{

    @Autowired
    private JournalRepository journalRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Transactional(readOnly = true)
    public List<Journal> getJournalsByUsername(String username) {
        log.debug("Fetching journals for user: {}", username);
        User user = userService.getUser(username);
        if (user == null) {
            log.error("User not found while fetching journals: {}", username);
            throw new IllegalArgumentException("User not found: " + username);
        }
        List<Journal> journals = journalRepository.findAllById(user.getJournalIds());
        log.debug("Retrieved {} journals for user: {}", journals.size(), username);
        return journals;
    }

    @Transactional
    public Journal createJournal(Journal journal, String username) {
        log.info("Creating new journal for user: {}", username);
        try {
            validateJournalInput(journal);
            User user = userService.getUser(username);
            if (user == null) {
                log.error("User not found while creating journal: {}", username);
                throw new IllegalArgumentException("User not found: " + username);
            }

            journal.setDate(new Date());
            Journal savedJournal = journalRepository.save(journal);
            log.debug("Saved journal with ID: {} for user: {}", savedJournal.getId(), username);

            user.getJournalIds().add(savedJournal.getId());
            userRepository.save(user);
            log.info("Successfully created journal for user: {}", username);
            return savedJournal;
        } catch (Exception e) {
            log.error("Error creating journal for user: {}", username, e);
            throw e;
        }
    }

    private void validateJournalInput(Journal journal) {
        if (journal == null ||
                journal.getTitle() == null || journal.getTitle().trim().isEmpty() ||
                journal.getContent() == null || journal.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("Title and content are required and cannot be empty.");
        }
    }


    @Transactional(readOnly = true)
    public Journal getJournal(ObjectId journalId, String username) {
        log.debug("Fetching journal {} for user: {}", journalId, username);
        User user = userService.getUser(username);
        if (user == null || !user.getJournalIds().contains(journalId)) {
            log.error("Unauthorized access or journal not found. JournalId: {}, Username: {}", journalId, username);
            throw new IllegalArgumentException("Journal not found or unauthorized access");
        }
        return journalRepository.findById(journalId).orElse(null);
    }

    @Transactional
    public boolean deleteJournal(ObjectId journalId, String username) {
        log.info("Attempting to delete journal {} for user: {}", journalId, username);
        User user = userService.getUser(username);

        if (user != null &&
                journalRepository.existsById(journalId) &&
                user.getJournalIds().contains(journalId)) {

            log.debug("Journal {} exists and is associated with user: {}", journalId, username);

            user.getJournalIds().remove(journalId);
            journalRepository.deleteById(journalId);
            log.debug("Journal {} deleted successfully for user: {}", journalId, username);

            // Save user without re-encoding password
            userRepository.save(user);
            log.info("User data updated successfully after deleting journal: {}", journalId);
            return true;
        }

        log.warn("Journal deletion failed. Either journal {} does not exist, or user {} is not authorized.", journalId, username);
        return false;
    }


    @Transactional
    public Journal updateJournal(ObjectId journalId, Journal journal, String username) {
        log.info("Updating journal {} for user: {}", journalId, username);
        User user = userService.getUser(username);
        validateJournalInput(journal);

        if (!user.getJournalIds().contains(journalId)) {
            log.error("Unauthorized access attempt to update journal. JournalId: {}, Username: {}", journalId, username);
            throw new IllegalArgumentException("Unauthorized journal access");
        }

        return journalRepository.findById(journalId)
                .map(existingJournal -> {
                    existingJournal.setTitle(journal.getTitle());
                    existingJournal.setContent(journal.getContent());
                    Journal updatedJournal = journalRepository.save(existingJournal);
                    log.info("Successfully updated journal {} for user: {}", journalId, username);
                    return updatedJournal;
                })
                .orElseThrow(() -> {
                    log.error("Journal not found for update. JournalId: {}", journalId);
                    return new IllegalArgumentException("Journal not found");
                });
    }
}