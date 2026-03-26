package com.example.miniproject_2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.miniproject_2.adapter.CartAdapter;
import com.example.miniproject_2.base.BaseActivity;
import com.example.miniproject_2.entity.Order;
import com.example.miniproject_2.entity.OrderDetail;

import java.util.List;

public class CartActivity extends BaseActivity {

    private RecyclerView rvCartItems;
    private TextView tvCartTotal;
    private Button btnCheckout, btnContinueShopping;
    private Order currentOrder;
    private List<OrderDetail> orderDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        rvCartItems = findViewById(R.id.rvCartItems);
        tvCartTotal = findViewById(R.id.tvCartTotal);
        btnCheckout = findViewById(R.id.btnCheckout);
        btnContinueShopping = findViewById(R.id.btnContinueShopping);

        rvCartItems.setLayoutManager(new LinearLayoutManager(this));

        loadCart();

        btnContinueShopping.setOnClickListener(v -> {
            Intent intent = new Intent(this, ProductListActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        btnCheckout.setOnClickListener(v -> {
            if (currentOrder != null && orderDetails != null && !orderDetails.isEmpty()) {
                // Cập nhật trạng thái thành Paid
                currentOrder.status = "Paid";
                db.orderDao().update(currentOrder);
                
                Toast.makeText(this, "Thanh toán thành công!", Toast.LENGTH_SHORT).show();
                
                Intent intent = new Intent(this, InvoiceActivity.class);
                intent.putExtra("ORDER_ID", currentOrder.id);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Giỏ hàng trống", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadCart() {
        int userId = prefsHelper.getUserId();
        currentOrder = db.orderDao().getPendingOrderByUserId(userId);
        
        if (currentOrder != null) {
            orderDetails = db.orderDetailDao().getByOrderId(currentOrder.id);
            CartAdapter adapter = new CartAdapter(orderDetails, db);
            rvCartItems.setAdapter(adapter);

            double total = 0;
            for (OrderDetail detail : orderDetails) {
                total += detail.unitPrice * detail.quantity;
            }
            currentOrder.totalAmount = total;
            db.orderDao().update(currentOrder);
            tvCartTotal.setText(String.format("$%.2f", total));
        } else {
            tvCartTotal.setText("$0.00");
            btnCheckout.setEnabled(false);
        }
    }
}
