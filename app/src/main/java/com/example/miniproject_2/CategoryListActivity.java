package com.example.miniproject_2;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.miniproject_2.adapter.CategoryAdapter;
import com.example.miniproject_2.base.BaseActivity;
import com.example.miniproject_2.entity.Category;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
//test
public class CategoryListActivity extends BaseActivity implements CategoryAdapter.OnCategoryActionListener {

    private RecyclerView rvCategories;
    private CategoryAdapter adapter;
    private FloatingActionButton fabAdd;
    private boolean isAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);

        isAdmin = "admin".equals(prefsHelper.getRole());

        rvCategories = findViewById(R.id.rvCategories);
        fabAdd = findViewById(R.id.fabAddCategory);
        
        rvCategories.setLayoutManager(new LinearLayoutManager(this));

        if (isAdmin) {
            fabAdd.setVisibility(View.VISIBLE);
            fabAdd.setOnClickListener(v -> showCategoryDialog(null));
        }

        findViewById(R.id.toolbarCategory).setOnClickListener(v -> finish());

        loadCategories();
    }

    private void loadCategories() {
        List<Category> categories = db.categoryDao().getAll();
        adapter = new CategoryAdapter(categories, isAdmin, this);
        rvCategories.setAdapter(adapter);
    }

    @Override
    public void onCategoryClick(Category category) {
        // Product list removed - categories no longer have a purpose
        Toast.makeText(this, "Danh mục không còn có sản phẩm", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEditClick(Category category) {
        showCategoryDialog(category);
    }

    @Override
    public void onDeleteClick(Category category) {
        new AlertDialog.Builder(this)
                .setTitle("Xóa danh mục")
                .setMessage("Bạn có chắc muốn xóa danh mục '" + category.name + "'? Tất cả sản phẩm thuộc danh mục này cũng sẽ bị xóa.")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    db.categoryDao().delete(category);
                    loadCategories();
                    Toast.makeText(this, "Đã xóa danh mục", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void showCategoryDialog(Category category) {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_category, null);
        EditText etName = view.findViewById(R.id.etDialogCategoryName);
        
        String title = "Thêm danh mục";
        if (category != null) {
            title = "Sửa danh mục";
            etName.setText(category.name);
        }

        new AlertDialog.Builder(this)
                .setTitle(title)
                .setView(view)
                .setPositiveButton("Lưu", (dialog, which) -> {
                    String name = etName.getText().toString().trim();
                    if (!name.isEmpty()) {
                        if (category == null) {
                            db.categoryDao().insert(new Category(name));
                        } else {
                            category.name = name;
                            db.categoryDao().update(category);
                        }
                        loadCategories();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}
