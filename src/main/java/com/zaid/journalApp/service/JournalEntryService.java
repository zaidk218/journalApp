package com.zaid.journalApp.service;

import com.zaid.journalApp.entity.JournalEntry;
import com.zaid.journalApp.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JournalEntryService {

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    public void saveEntry(JournalEntry journalEntry) {
        journalEntryRepository.save(journalEntry);
    }

    public List<JournalEntry> getAllJournalEntries() {
        return journalEntryRepository.findAll();
    }

    public JournalEntry getJournalEntry(ObjectId id) {
        Optional<JournalEntry> entry = journalEntryRepository.findById(id);
        return entry.orElse(null);
    }


    public boolean deleteJournalEntry(ObjectId id) {
        if (journalEntryRepository.existsById(id)) {
            journalEntryRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public JournalEntry updateJournalEntry(ObjectId id, JournalEntry journalEntry) {
        Optional<JournalEntry> existingEntry = journalEntryRepository.findById(id);
        if (existingEntry.isPresent()) {
            JournalEntry entryToUpdate = existingEntry.get();
            entryToUpdate.setTitle(journalEntry.getTitle());
            entryToUpdate.setContent(journalEntry.getContent());
            entryToUpdate.setDate(journalEntry.getDate());
            return journalEntryRepository.save(entryToUpdate);
        }
        return null;
    }
}











































//package com.zaid.journalApp.service;
//
//import com.zaid.journalApp.entity.JournalEntry;
//import com.zaid.journalApp.repository.JournalEntryRepository;
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
//    private JournalEntryRepository journalEntryRepository;
//    public void saveEntry(JournalEntry journalEntry){
//        journalEntryRepository.save(journalEntry);
//    }
//    public List<JournalEntry> getAllJournalEntries() {
//        return journalEntryRepository.findAll();
//    }
//    public JournalEntry getJournalEntry(ObjectId id) {
//        try {
//            return journalEntryRepository.findById(id).get();
//        } catch (NoSuchElementException e) {
//            // Handle the case where no JournalEntry with the given id exists
//            return null; // or throw a custom exception, etc.
//        }
//    }
//    public void deleteJournalEntry(ObjectId id) {
//        journalEntryRepository.deleteById(id);
//    }
//    public JournalEntry updateJournalEntry(ObjectId id, JournalEntry journalEntry) {
//        JournalEntry existingEntry = getJournalEntry(id);
//        if (existingEntry != null) {
//            existingEntry.setTitle(journalEntry.getTitle());
//            existingEntry.setContent(journalEntry.getContent());
//            return journalEntryRepository.save(existingEntry);
//        }
//        return null;
//    }
//}
