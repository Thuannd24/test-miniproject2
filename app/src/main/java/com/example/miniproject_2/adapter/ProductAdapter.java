package com.example.miniproject_2.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.miniproject_2.R;
import com.example.miniproject_2.entity.Product;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> products;
    private boolean isAdmin;
    private OnProductActionListener listener;

    public interface OnProductActionListener {
        void onProductClick(Product product);
        void onEditClick(Product product);
        void onDeleteClick(Product product);
    }

    public ProductAdapter(List<Product> products, boolean isAdmin, OnProductActionListener listener) {
        this.products = products;
        this.isAdmin = isAdmin;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = products.get(position);
        holder.tvProductName.setText(product.name);
        holder.tvProductPrice.setText(String.format("₫%.3f", product.price));
        
        Glide.with(holder.itemView.getContext())
                .load(product.imageUrl)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.stat_notify_error)
                .into(holder.ivProductImage);

        if (isAdmin) {
            holder.layoutAdminActions.setVisibility(View.VISIBLE);
            holder.ivEdit.setOnClickListener(v -> listener.onEditClick(product));
            holder.ivDelete.setOnClickListener(v -> listener.onDeleteClick(product));
        } else {
            holder.layoutAdminActions.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> listener.onProductClick(product));
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView tvProductName, tvProductPrice;
        ImageView ivProductImage, ivEdit, ivDelete;
        View layoutAdminActions;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
            ivProductImage = itemView.findViewById(R.id.ivProductImage);
            ivEdit = itemView.findViewById(R.id.ivEditProduct);
            ivDelete = itemView.findViewById(R.id.ivDeleteProduct);
            layoutAdminActions = itemView.findViewById(R.id.layoutAdminProductActions);
        }
    }
}
