package com.zaid.journalApp.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import java.util.Date;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection="journalEntries")
public class Journal{
    @Id
    private ObjectId id;
    private String title;
    private String content;
    private Date date;
}