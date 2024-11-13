package com.zaid.journalApp.entity;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection="journalEntries")
@Data
public class Journal {
    @Id
    private ObjectId id;
    private String title;
    private String content;
    private Date date;
}
