package com.example.miniproject_2;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.miniproject_2.adapter.ProductAdapter;
import com.example.miniproject_2.base.BaseActivity;
import com.example.miniproject_2.entity.Category;
import com.example.miniproject_2.entity.Product;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class ProductListActivity extends BaseActivity implements ProductAdapter.OnProductActionListener {

    private RecyclerView rvProducts;
    private ProductAdapter adapter;
    private Toolbar toolbar;
    private FloatingActionButton fabAdd;
    private boolean isAdmin;
    private int currentCategoryId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        isAdmin = "admin".equals(prefsHelper.getRole());
        currentCategoryId = getIntent().getIntExtra("CATEGORY_ID", -1);

        toolbar = findViewById(R.id.toolbar);
        rvProducts = findViewById(R.id.rvProducts);
        fabAdd = findViewById(R.id.fabAddProduct);

        rvProducts.setLayoutManager(new GridLayoutManager(this, 2));

        if (isAdmin) {
            fabAdd.setVisibility(View.VISIBLE);
            fabAdd.setOnClickListener(v -> showProductDialog(null));
        }

        toolbar.setNavigationOnClickListener(v -> finish());

        loadProducts();
    }

    private void loadProducts() {
        List<Product> products;
        if (currentCategoryId != -1) {
            products = db.productDao().getByCategoryId(currentCategoryId);
            Category category = db.categoryDao().getById(currentCategoryId);
            if (category != null) toolbar.setTitle(category.name);
        } else {
            products = db.productDao().getAll();
            toolbar.setTitle("Tất cả sản phẩm");
        }

        adapter = new ProductAdapter(products, isAdmin, this);
        rvProducts.setAdapter(adapter);
    }

    @Override
    public void onProductClick(Product product) {
        Intent intent = new Intent(this, ProductDetailActivity.class);
        intent.putExtra("PRODUCT_ID", product.id);
        startActivity(intent);
    }

    @Override
    public void onEditClick(Product product) {
        showProductDialog(product);
    }

    @Override
    public void onDeleteClick(Product product) {
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

    private void showProductDialog(Product product) {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_product, null);
        EditText etName = view.findViewById(R.id.etDialogProductName);
        EditText etPrice = view.findViewById(R.id.etDialogProductPrice);
        EditText etImageUrl = view.findViewById(R.id.etDialogProductImage);
        EditText etDesc = view.findViewById(R.id.etDialogProductDesc);

        if (product != null) {
            etName.setText(product.name);
            etPrice.setText(String.valueOf(product.price));
            etImageUrl.setText(product.imageUrl);
            etDesc.setText(product.description);
        }

        new AlertDialog.Builder(this)
                .setTitle(product == null ? "Thêm sản phẩm" : "Sửa sản phẩm")
                .setView(view)
                .setPositiveButton("Lưu", (dialog, which) -> {
                    String name = etName.getText().toString().trim();
                    String priceStr = etPrice.getText().toString().trim();
                    String imageUrl = etImageUrl.getText().toString().trim();
                    String desc = etDesc.getText().toString().trim();

                    if (!name.isEmpty() && !priceStr.isEmpty()) {
                        double price = Double.parseDouble(priceStr);
                        if (product == null) {
                            // Mặc định thêm vào category hiện tại hoặc category đầu tiên
                            int catId = (currentCategoryId != -1) ? currentCategoryId : 1; 
                            db.productDao().insert(new Product(catId, name, price, desc, imageUrl));
                        } else {
                            product.name = name;
                            product.price = price;
                            product.imageUrl = imageUrl;
                            product.description = desc;
                            db.productDao().update(product);
                        }
                        loadProducts();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}
