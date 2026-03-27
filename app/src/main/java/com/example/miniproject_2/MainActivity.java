package com.example.miniproject_2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.miniproject_2.base.BaseActivity;

public class MainActivity extends BaseActivity {

    private TextView tvUserWelcome;
    private View btnNavCategories, btnNavLogin;
    private boolean isAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Seed data
        db.seedInitialData();

        isAdmin = "admin".equals(prefsHelper.getRole());

        tvUserWelcome = findViewById(R.id.tvUserWelcome);
        btnNavCategories = findViewById(R.id.btnNavCategories);
        btnNavLogin = findViewById(R.id.btnNavLogin);

        btnNavCategories.setOnClickListener(v -> {
            startActivity(new Intent(this, CategoryListActivity.class));
        });

        btnNavLogin.setOnClickListener(v -> {
            if (prefsHelper.isLoggedIn()) {
                // Logout flow
                prefsHelper.clearSession();
                isAdmin = false;
                updateUI();
            } else {
                startActivity(new Intent(this, LoginActivity.class));
            }
        });

        updateUI();
    }
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
