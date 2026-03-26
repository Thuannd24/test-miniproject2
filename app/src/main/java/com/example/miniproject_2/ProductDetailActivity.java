package com.example.miniproject_2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.miniproject_2.base.BaseActivity;
import com.example.miniproject_2.entity.Order;
import com.example.miniproject_2.entity.OrderDetail;
import com.example.miniproject_2.entity.Product;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ProductDetailActivity extends BaseActivity {

    private TextView tvName, tvPrice, tvDescription;
    private Button btnAddToCart;
    private Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        tvName = findViewById(R.id.tvDetailName);
        tvPrice = findViewById(R.id.tvDetailPrice);
        tvDescription = findViewById(R.id.tvDetailDescription);
        btnAddToCart = findViewById(R.id.btnAddToCart);

        int productId = getIntent().getIntExtra("PRODUCT_ID", -1);
        product = db.productDao().getById(productId);

        if (product != null) {
            tvName.setText(product.name);
            tvPrice.setText(String.format("$%.2f", product.price));
            tvDescription.setText(product.description);
        }

        btnAddToCart.setOnClickListener(v -> {
            if (!prefsHelper.isLoggedIn()) {
                Toast.makeText(this, "Vui lòng đăng nhập để mua hàng", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, LoginActivity.class));
                return;
            }
            addToCart();
        });
    }

    private void addToCart() {
        int userId = prefsHelper.getUserId();
        // 1. Tìm hoặc tạo Order ở trạng thái 'Pending'
        Order pendingOrder = db.orderDao().getPendingOrderByUserId(userId);
        if (pendingOrder == null) {
            String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
            pendingOrder = new Order(userId, currentDate, "Pending", 0.0);
            long orderId = db.orderDao().insert(pendingOrder);
            pendingOrder.id = (int) orderId;
        }

        // 2. Thêm vào OrderDetail
        OrderDetail existingDetail = db.orderDetailDao().getByOrderAndProduct(pendingOrder.id, product.id);
        if (existingDetail != null) {
            existingDetail.quantity += 1;
            db.orderDetailDao().update(existingDetail);
        } else {
            OrderDetail newDetail = new OrderDetail(pendingOrder.id, product.id, 1, product.price);
            db.orderDetailDao().insert(newDetail);
        }

        Toast.makeText(this, "Đã thêm " + product.name + " vào giỏ hàng", Toast.LENGTH_SHORT).show();
        
        // Theo luồng trong ảnh: Sau khi thêm có thể tiếp tục chọn hoặc thanh toán
        // Ở đây ta có thể mở màn hình Giỏ hàng (CartActivity)
        startActivity(new Intent(this, CartActivity.class));
    }
}
