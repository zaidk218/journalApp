package com.zaid.journalApp.service;

import com.zaid.journalApp.entity.Journal;
import com.zaid.journalApp.entity.User;
import com.zaid.journalApp.repository.JournalRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class JournalService {

    @Autowired
    private JournalRepository journalRepository;

    @Autowired
    private UserService userService;

    public List<Journal> getJournalsByUsername(String username) {
        User user = userService.getUser(username);
        return (user != null) ? user.getJournals() : null;
    }

    public Journal createJournal(Journal journal, String username) {
        if (journal == null || journal.getTitle().isEmpty() || journal.getContent().isEmpty()) {
            return null; // Validation failed
        }

        User user = userService.getUser(username);
        if (user == null) {
            return null; // User does not exist
        }

        journal.setDate(new Date());
        Journal savedJournal = journalRepository.save(journal);

        // Add journal to the user's list
        user.getJournals().add(savedJournal);
        userService.saveEntry(user);
        return savedJournal;
    }

    public Journal getJournal(ObjectId id) {
        return journalRepository.findById(id).orElse(null);
    }

    public boolean deleteJournal(ObjectId id, String username) {
        User user = userService.getUser(username);
        if (user != null && journalRepository.existsById(id)) {
            user.getJournals().removeIf(journal -> journal.getId().equals(id));
            journalRepository.deleteById(id);
            userService.saveEntry(user);
            return true;
        }
        return false;
    }

    public Journal updateJournal(ObjectId id, Journal journal, String username) {
        User user = userService.getUser(username);
        if (user == null || journal == null || journal.getTitle().isEmpty() || journal.getContent().isEmpty()) {
            return null; // Validation failed
        }

        Optional<Journal> existingJournal = journalRepository.findById(id);
        if (existingJournal.isPresent()) {
            Journal entryToUpdate = existingJournal.get();
            entryToUpdate.setTitle(journal.getTitle());
            entryToUpdate.setContent(journal.getContent());
            entryToUpdate.setDate(new Date());
            return journalRepository.save(entryToUpdate);
        }
        return null; // Journal not found
    }
}
