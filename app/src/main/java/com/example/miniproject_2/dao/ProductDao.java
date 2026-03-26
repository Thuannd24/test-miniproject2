package com.example.miniproject_2.dao;

import androidx.room.Dao;
import androidx.room.Query;
import com.example.miniproject_2.entity.Product;
import java.util.List;

@Dao
public interface ProductDao extends BaseDao<Product> {
    @Query("SELECT * FROM products")
    List<Product> getAll();

    @Query("SELECT * FROM products WHERE id = :id")
    Product getById(int id);

    @Query("SELECT * FROM products WHERE categoryId = :categoryId")
    List<Product> getByCategoryId(int categoryId);
}
