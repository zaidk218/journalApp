package com.zaid.journalApp.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/journal")
public class UserController {


}











































































//package com.zaid.journalApp.controller;
//
//import com.mongodb.lang.NonNull;
//import com.zaid.journalApp.entity.JournalEntry;
//import com.zaid.journalApp.service.JournalEntryService;
//import org.bson.types.ObjectId;
//import org.jetbrains.annotations.NotNull;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//
//import java.util.List;
//
//
//@RestController
//@RequestMapping("/journal")
//public class JournalEntryControllerV2 {
//
//    @Autowired
//    private JournalEntryService journalEntryService;
//    @GetMapping()
//    public List<JournalEntry> getJournalEntries() {
//        return journalEntryService.getAllJournalEntries();
//    }
//
//    @PostMapping
//    public JournalEntry createJournalEntry(@RequestBody @NonNull JournalEntry journalEntry) {
//        journalEntry.setDate(new java.util.Date());
//         journalEntryService.saveEntry(journalEntry);
//         return journalEntry;
//    }
//
//    @GetMapping("/id/{myId}")
//    public JournalEntry getJournalEntry(@PathVariable ObjectId myId) {
//        return journalEntryService.getJournalEntry(myId);
//    }
//    @DeleteMapping("/id/{myId}")
//    public boolean deleteJournalEntry(@PathVariable ObjectId myId) {
//         journalEntryService.deleteJournalEntry(myId);
//         return true;
//    }
//
//    @PutMapping("/id/{myId}")
//    public JournalEntry updateJournalEntry(@PathVariable ObjectId myId, @RequestBody @NotNull JournalEntry journalEntry) {
//        journalEntry.setDate(new java.util.Date());
//        return journalEntryService.updateJournalEntry(myId, journalEntry);
//    }
//}
