package com.zaid.journalApp.service;

import com.zaid.journalApp.entity.Journal;
import com.zaid.journalApp.repository.JournalRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JournalService {

    @Autowired
    private JournalRepository journalRepository;

    public void saveJournal(Journal journal) {
        journalRepository.save(journal);
    }

    public List<Journal> getAllJournals() {
        return journalRepository.findAll();
    }

    public Journal getJournal(ObjectId id) {
        Optional<Journal> journal = journalRepository.findById(id);
        return journal.orElse(null);
    }


    public boolean deleteJournal(ObjectId id) {
        if (journalRepository.existsById(id)) {
            journalRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Journal updateJournal(ObjectId id, Journal journal) {
        Optional<Journal> existingJournal = journalRepository.findById(id);
        if (existingJournal.isPresent()) {
            Journal entryToUpdate = existingJournal.get();
            entryToUpdate.setTitle(journal.getTitle());
            entryToUpdate.setContent(journal.getContent());
            entryToUpdate.setDate(journal.getDate());
            return journalRepository.save(entryToUpdate);
        }
        return null;
    }
}











































//package com.zaid.journalApp.service;
//
//import com.zaid.journalApp.entity.JournalEntry;
//import com.zaid.journalApp.repository.JournalRepository;
//import org.bson.types.ObjectId;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//import java.util.NoSuchElementException;
//
//@Component
//public class JournalEntryService {
//    @Autowired
//    private JournalRepository JournalRepository;
//    public void saveEntry(JournalEntry journalEntry){
//        JournalRepository.save(journalEntry);
//    }
//    public List<JournalEntry> getAllJournalEntries() {
//        return JournalRepository.findAll();
//    }
//    public JournalEntry getJournalEntry(ObjectId id) {
//        try {
//            return JournalRepository.findById(id).get();
//        } catch (NoSuchElementException e) {
//            // Handle the case where no JournalEntry with the given id exists
//            return null; // or throw a custom exception, etc.
//        }
//    }
//    public void deleteJournalEntry(ObjectId id) {
//        JournalRepository.deleteById(id);
//    }
//    public JournalEntry updateJournalEntry(ObjectId id, JournalEntry journalEntry) {
//        JournalEntry existingEntry = getJournalEntry(id);
//        if (existingEntry != null) {
//            existingEntry.setTitle(journalEntry.getTitle());
//            existingEntry.setContent(journalEntry.getContent());
//            return JournalRepository.save(existingEntry);
//        }
//        return null;
//    }
//}
