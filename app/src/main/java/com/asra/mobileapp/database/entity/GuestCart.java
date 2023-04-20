package com.asra.mobileapp.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value = {"slug"},
        unique = true)})
public class GuestCart {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @NonNull
    public String slug;

    public String rawJson;


}
