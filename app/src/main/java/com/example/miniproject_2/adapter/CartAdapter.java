package com.example.miniproject_2.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.miniproject_2.R;
import com.example.miniproject_2.database.AppDatabase;
import com.example.miniproject_2.entity.OrderDetail;
import com.example.miniproject_2.entity.Product;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<OrderDetail> items;
    private AppDatabase db;

    public CartAdapter(List<OrderDetail> items, AppDatabase db) {
        this.items = items;
        this.db = db;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        OrderDetail detail = items.get(position);
        Product product = db.productDao().getById(detail.productId);
        
        if (product != null) {
            holder.tvName.setText(product.name);
            holder.tvPrice.setText(String.format("$%.2f x %d", detail.unitPrice, detail.quantity));
            holder.tvSubtotal.setText(String.format("$%.2f", detail.unitPrice * detail.quantity));
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPrice, tvSubtotal;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvCartProductName);
            tvPrice = itemView.findViewById(R.id.tvCartPrice);
            tvSubtotal = itemView.findViewById(R.id.tvCartSubtotal);
        }
    }
}
