package com.example.miniproject_2.dao;

import androidx.room.Dao;
import androidx.room.Query;
import com.example.miniproject_2.entity.OrderDetail;
import java.util.List;

@Dao
public interface OrderDetailDao extends BaseDao<OrderDetail> {
    @Query("SELECT * FROM order_details WHERE orderId = :orderId")
    List<OrderDetail> getByOrderId(int orderId);

    @Query("SELECT * FROM order_details WHERE orderId = :orderId AND productId = :productId LIMIT 1")
    OrderDetail getByOrderAndProduct(int orderId, int productId);
}
