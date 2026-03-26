package com.example.miniproject_2.entity;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "categories")
public class Category {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String name;

    public Category() {}

    @Ignore
    public Category(String name) {
        this.name = name;
    }
}
