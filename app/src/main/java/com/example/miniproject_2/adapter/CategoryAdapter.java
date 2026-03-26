package com.example.miniproject_2.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.miniproject_2.R;
import com.example.miniproject_2.entity.Category;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private List<Category> categories;
    private boolean isAdmin;
    private OnCategoryActionListener listener;

    public interface OnCategoryActionListener {
        void onCategoryClick(Category category);
        void onEditClick(Category category);
        void onDeleteClick(Category category);
    }

    public CategoryAdapter(List<Category> categories, boolean isAdmin, OnCategoryActionListener listener) {
        this.categories = categories;
        this.isAdmin = isAdmin;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categories.get(position);
        holder.tvCategoryName.setText(category.name);

        if (isAdmin) {
            holder.layoutAdminActions.setVisibility(View.VISIBLE);
            holder.ivEditCategory.setOnClickListener(v -> listener.onEditClick(category));
            holder.ivDeleteCategory.setOnClickListener(v -> listener.onDeleteClick(category));
        } else {
            holder.layoutAdminActions.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> listener.onCategoryClick(category));
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategoryName;
        View layoutAdminActions;
        ImageView ivEditCategory, ivDeleteCategory;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
            layoutAdminActions = itemView.findViewById(R.id.layoutAdminActions);
            ivEditCategory = itemView.findViewById(R.id.ivEditCategory);
            ivDeleteCategory = itemView.findViewById(R.id.ivDeleteCategory);
        }
    }
}
