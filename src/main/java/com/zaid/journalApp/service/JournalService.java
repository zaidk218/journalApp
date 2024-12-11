package com.zaid.journalApp.service;

import com.zaid.journalApp.entity.Journal;
import com.zaid.journalApp.entity.User;
import com.zaid.journalApp.repository.JournalRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class JournalService {

    @Autowired
    private JournalRepository journalRepository;

    @Autowired
    private UserService userService;

    @Transactional(readOnly = true)
    public List<Journal> getJournalsByUsername(String username) {
        User user = userService.getUser(username);
        if(user==null){
            throw new IllegalArgumentException("User not found: " + username);
        }
        return journalRepository.findAllById(user.getJournalIds());

    }

    @Transactional
    public Journal createJournal(Journal journal, String username) {
        if (journal == null || journal.getTitle().isEmpty() || journal.getContent().isEmpty()) {
            throw new IllegalArgumentException("Title and content are required.");
        }

        User user = userService.getUser(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found: " + username);
        }

        journal.setDate(new Date());  // Automatically set the current date
        Journal savedJournal = journalRepository.save(journal);
        user.getJournalIds().add(savedJournal.getId());
        userService.saveEntry(user);
        return savedJournal;
    }


    @Transactional(readOnly = true) // Read-only for fetching a single journal
    public Journal getJournal(ObjectId journalId){
        return journalRepository.findById(journalId).orElse(null);
    }

    @Transactional
    public boolean deleteJournal(ObjectId journalId,String username){
        User user =userService.getUser(username);
        if(user!=null && journalRepository.existsById(journalId)){
            user.getJournalIds().remove(journalId);
            journalRepository.deleteById(journalId);
            userService.saveEntry(user);
            return true;
        }
        return false;
    }

    @Transactional
    public Journal updateJournal(ObjectId journalId,Journal journal,String username){
        User user =userService.getUser(username);
        if(journal==null|| journal.getTitle().isEmpty()||journal.getContent().isEmpty()||!user.getJournalIds().contains(journalId)){
            throw new IllegalArgumentException("Invalid Journal data");
        }
        return journalRepository.findById(journalId)
                .map(existingJournal->{
                    existingJournal.setTitle(journal.getTitle());
                    existingJournal.setContent(journal.getContent());
                    return journalRepository.save(existingJournal);
                })
                .orElse(null);

        }
    }
























































