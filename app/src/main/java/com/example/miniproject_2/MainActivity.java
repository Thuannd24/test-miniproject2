package com.example.miniproject_2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.miniproject_2.adapter.ProductAdapter;
import com.example.miniproject_2.base.BaseActivity;
import com.example.miniproject_2.entity.Product;

import java.util.List;

public class MainActivity extends BaseActivity implements ProductAdapter.OnProductActionListener {

    private TextView tvUserWelcome;
    private ImageView ivCart;
    private View btnNavCategories, btnNavLogin;
    private RecyclerView rvHomeProducts;
    private ProductAdapter adapter;
    private boolean isAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Seed data
        db.seedInitialData();

        isAdmin = "admin".equals(prefsHelper.getRole());

        tvUserWelcome = findViewById(R.id.tvUserWelcome);
        ivCart = findViewById(R.id.ivCart);
        btnNavCategories = findViewById(R.id.btnNavCategories);
        btnNavLogin = findViewById(R.id.btnNavLogin);
        rvHomeProducts = findViewById(R.id.rvHomeProducts);

        rvHomeProducts.setLayoutManager(new GridLayoutManager(this, 2));
        loadProducts();

        btnNavCategories.setOnClickListener(v -> {
            startActivity(new Intent(this, CategoryListActivity.class));
        });

        btnNavLogin.setOnClickListener(v -> {
            if (prefsHelper.isLoggedIn()) {
                // Logout flow
                prefsHelper.clearSession();
                isAdmin = false;
                updateUI();
                loadProducts();
            } else {
                startActivity(new Intent(this, LoginActivity.class));
            }
        });

        ivCart.setOnClickListener(v -> {
            if (prefsHelper.isLoggedIn()) {
                startActivity(new Intent(this, CartActivity.class));
            } else {
                startActivity(new Intent(this, LoginActivity.class));
            }
        });

        updateUI();
    }

    private void loadProducts() {
        List<Product> products = db.productDao().getAll();
        // Cập nhật ProductAdapter với 3 đối số: danh sách, quyền admin, và listener (this)
        adapter = new ProductAdapter(products, isAdmin, this);
        rvHomeProducts.setAdapter(adapter);
    }

    @Override
    public void onProductClick(Product product) {
        Intent intent = new Intent(this, ProductDetailActivity.class);
        intent.putExtra("PRODUCT_ID", product.id);
        startActivity(intent);
    }

    @Override
    public void onEditClick(Product product) {
        // Có thể tái sử dụng dialog từ ProductListActivity hoặc chuyển hướng sang đó
        // Ở đây để đơn giản ta có thể chuyển sang ProductListActivity để thực hiện sửa
        Intent intent = new Intent(this, ProductListActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(Product product) {
        if (isAdmin) {
            new AlertDialog.Builder(this)
                    .setTitle("Xóa sản phẩm")
                    .setMessage("Bạn có chắc muốn xóa '" + product.name + "'?")
                    .setPositiveButton("Xóa", (dialog, which) -> {
                        db.productDao().delete(product);
                        loadProducts();
                        Toast.makeText(this, "Đã xóa sản phẩm", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isAdmin = "admin".equals(prefsHelper.getRole());
        updateUI();
        loadProducts();
    }

    private void updateUI() {
        if (prefsHelper.isLoggedIn()) {
            tvUserWelcome.setText("Chào, " + prefsHelper.getUsername());
        } else {
            tvUserWelcome.setText("Đăng nhập");
        }
    }
}
