package com.example.miniproject_2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.miniproject_2.base.BaseActivity;
import com.example.miniproject_2.entity.User;

public class RegisterActivity extends BaseActivity {

    private EditText etRegUsername, etRegFullName, etRegPassword, etRegConfirmPassword;
    private Button btnRegister;
    private TextView tvGoToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etRegUsername = findViewById(R.id.etRegUsername);
        etRegFullName = findViewById(R.id.etRegFullName);
        etRegPassword = findViewById(R.id.etRegPassword);
        etRegConfirmPassword = findViewById(R.id.etRegConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        tvGoToLogin = findViewById(R.id.tvGoToLogin);

        btnRegister.setOnClickListener(v -> handleRegister());

        tvGoToLogin.setOnClickListener(v -> finish());
    }

    private void handleRegister() {
        String username = etRegUsername.getText().toString().trim();
        String fullName = etRegFullName.getText().toString().trim();
        String password = etRegPassword.getText().toString().trim();
        String confirm = etRegConfirmPassword.getText().toString().trim();

        if (username.isEmpty() || fullName.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirm)) {
            Toast.makeText(this, "Mật khẩu không khớp", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if username already exists
        User existingUser = db.userDao().findByUsername(username);
        if (existingUser != null) {
            Toast.makeText(this, "Tên đăng nhập đã tồn tại", Toast.LENGTH_SHORT).show();
            return;
        }

        // Insert new user
        User newUser = new User(username, password, fullName, "user");
        db.userDao().insert(newUser);

        Toast.makeText(this, "Đăng ký thành công! Vui lòng Đăng nhập", Toast.LENGTH_LONG).show();
        finish();
    }
}
