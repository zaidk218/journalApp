package com.zaid.journalApp.service;

import com.zaid.journalApp.entity.User;
import com.zaid.journalApp.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void saveEntry(User user) {
        userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUser(ObjectId id) {
        Optional<User> entry = userRepository.findById(id);
        return entry.orElse(null);
    }


    public boolean deleteUser(ObjectId id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public User updateUser(ObjectId id, User user) {
        Optional<User> existingEntry = userRepository.findById(id);
        if (existingEntry.isPresent()) {
            User entryToUpdate = existingEntry.get();
            return userRepository.save(entryToUpdate);
        }
        return null;
    }
}











































//package com.zaid.journalApp.service;
//
//import com.zaid.journalApp.entity.JournalEntry;
//import com.zaid.journalApp.repository.UserRepository;
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
//    private UserRepository UserRepository;
//    public void saveEntry(JournalEntry journalEntry){
//        UserRepository.save(journalEntry);
//    }
//    public List<JournalEntry> getAllJournalEntries() {
//        return UserRepository.findAll();
//    }
//    public JournalEntry getJournalEntry(ObjectId id) {
//        try {
//            return UserRepository.findById(id).get();
//        } catch (NoSuchElementException e) {
//            // Handle the case where no JournalEntry with the given id exists
//            return null; // or throw a custom exception, etc.
//        }
//    }
//    public void deleteJournalEntry(ObjectId id) {
//        UserRepository.deleteById(id);
//    }
//    public JournalEntry updateJournalEntry(ObjectId id, JournalEntry journalEntry) {
//        JournalEntry existingEntry = getJournalEntry(id);
//        if (existingEntry != null) {
//            existingEntry.setTitle(journalEntry.getTitle());
//            existingEntry.setContent(journalEntry.getContent());
//            return UserRepository.save(existingEntry);
//        }
//        return null;
//    }
//}
