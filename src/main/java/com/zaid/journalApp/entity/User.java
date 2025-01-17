package com.zaid.journalApp.entity;

import lombok.Data;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection="users")
public class User{
    @Id
    private ObjectId id;

    @NonNull
    @Indexed(unique=true)
    private String username;

    @NonNull
    @Indexed(unique = true)
    private String email; // New field added

    private boolean sentimentAnalysis; // New field added

    @NonNull
    private String password;

    // Maintain only IDs for journals for better decoupling
    private List<ObjectId>JournalIds=new ArrayList<>();

    private List<String> roles = new ArrayList<>();

}