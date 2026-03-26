package com.example.miniproject_2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.miniproject_2.adapter.CartAdapter;
import com.example.miniproject_2.base.BaseActivity;
import com.example.miniproject_2.entity.Order;
import com.example.miniproject_2.entity.OrderDetail;
import com.example.miniproject_2.entity.User;

import java.util.List;

public class InvoiceActivity extends BaseActivity {

    private TextView tvInvoiceId, tvInvoiceDate, tvInvoiceUser, tvInvoiceTotal;
    private RecyclerView rvInvoiceDetails;
    private Button btnBackToHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);

        tvInvoiceId = findViewById(R.id.tvInvoiceId);
        tvInvoiceDate = findViewById(R.id.tvInvoiceDate);
        tvInvoiceUser = findViewById(R.id.tvInvoiceUser);
        tvInvoiceTotal = findViewById(R.id.tvInvoiceTotal);
        rvInvoiceDetails = findViewById(R.id.rvInvoiceDetails);
        btnBackToHome = findViewById(R.id.btnBackToHome);

        rvInvoiceDetails.setLayoutManager(new LinearLayoutManager(this));

        int orderId = getIntent().getIntExtra("ORDER_ID", -1);
        Order order = db.orderDao().getById(orderId);

        if (order != null) {
            tvInvoiceId.setText("Mã hóa đơn: #" + order.id);
            tvInvoiceDate.setText("Ngày: " + order.orderDate);

            User user = db.userDao().login(prefsHelper.getUsername(), ""); // Simple way to get full info if needed, but we have username in prefs
            tvInvoiceUser.setText("Khách hàng: " + prefsHelper.getUsername());

            List<OrderDetail> details = db.orderDetailDao().getByOrderId(order.id);
            CartAdapter adapter = new CartAdapter(details, db);
            rvInvoiceDetails.setAdapter(adapter);

            tvInvoiceTotal.setText(String.format("$%.2f", order.totalAmount));
        }

        btnBackToHome.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });
    }
}
