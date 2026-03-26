package com.example.miniproject_2.dao;

import androidx.room.Dao;
import androidx.room.Query;
import com.example.miniproject_2.entity.Order;
import java.util.List;

@Dao
public interface OrderDao extends BaseDao<Order> {
    @Query("SELECT * FROM orders WHERE userId = :userId AND status = 'Pending' LIMIT 1")
    Order getPendingOrderByUserId(int userId);

    @Query("SELECT * FROM orders WHERE userId = :userId")
    List<Order> getOrdersByUserId(int userId);

    @Query("SELECT * FROM orders WHERE id = :orderId")
    Order getById(int orderId);
}
