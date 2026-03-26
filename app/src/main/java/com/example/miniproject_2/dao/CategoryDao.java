package com.example.miniproject_2.dao;

import androidx.room.Dao;
import androidx.room.Query;
import com.example.miniproject_2.entity.Category;
import java.util.List;

@Dao
public interface CategoryDao extends BaseDao<Category> {
    @Query("SELECT * FROM categories")
    List<Category> getAll();

    @Query("SELECT * FROM categories WHERE id = :id")
    Category getById(int id);
}
