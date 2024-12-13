package com.zaid.journalApp.service;

import com.zaid.journalApp.entity.Journal;
import com.zaid.journalApp.entity.User;
import com.zaid.journalApp.repository.JournalRepository;
import com.zaid.journalApp.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
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
        User user = userService.getUser(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found: " + username);
        }
        return journalRepository.findAllById(user.getJournalIds());
    }

    @Transactional
    public Journal createJournal(Journal journal, String username) {
        // Validate journal input
        validateJournalInput(journal);

        // Retrieve user
        User user = userService.getUser(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found: " + username);
        }

        // Set journal date and save
        journal.setDate(new Date());
        Journal savedJournal = journalRepository.save(journal);

        // Update user's journal list without modifying password
        user.getJournalIds().add(savedJournal.getId());
        // Use save method that doesn't trigger password re-encoding
        userRepository.save(user);

        return savedJournal;
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
        User user = userService.getUser(username);
        if (user == null || !user.getJournalIds().contains(journalId)) {
            throw new IllegalArgumentException("Journal not found or unauthorized access");
        }
        return journalRepository.findById(journalId).orElse(null);
    }

    @Transactional
    public boolean deleteJournal(ObjectId journalId, String username) {
        User user = userService.getUser(username);
        if (user != null &&
                journalRepository.existsById(journalId) &&
                user.getJournalIds().contains(journalId)) {

            user.getJournalIds().remove(journalId);
            journalRepository.deleteById(journalId);

            // Save user without re-encoding password
            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Transactional
    public Journal updateJournal(ObjectId journalId, Journal journal, String username) {
        User user = userService.getUser(username);

        // Validate input
        validateJournalInput(journal);

        // Check journal ownership
        if (!user.getJournalIds().contains(journalId)) {
            throw new IllegalArgumentException("Unauthorized journal access");
        }

        return journalRepository.findById(journalId)
                .map(existingJournal -> {
                    existingJournal.setTitle(journal.getTitle());
                    existingJournal.setContent(journal.getContent());
                    return journalRepository.save(existingJournal);
                })
                .orElseThrow(() -> new IllegalArgumentException("Journal not found"));
    }
}