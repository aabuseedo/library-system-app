package com.example.librarysystemapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.librarysystemapp.R;
import com.example.librarysystemapp.models.Category;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private Context context;
    private List<Category> categoryList;
    private OnCategoryClickListener listener;
    private int selectedPosition = 0; // الافتراضي: "الكل"

    public interface OnCategoryClickListener {
        void onCategoryClick(Category category);
    }

    public CategoryAdapter(Context ctx, List<Category> list, OnCategoryClickListener listener) {
        this.context = ctx;
        this.categoryList = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.category_item, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categoryList.get(position);
        holder.tvCategoryName.setText(category.getName());

        // التمييز اللوني للتصنيف المحدد
        if (selectedPosition == position) {
            holder.tvCategoryName.setBackgroundResource(R.drawable.selected_category_bg);
            holder.tvCategoryName.setTextColor(Color.WHITE);
        } else {
            holder.tvCategoryName.setBackgroundResource(R.drawable.unselected_category_bg);
            holder.tvCategoryName.setTextColor(Color.parseColor("#4F46E5"));
        }

        holder.itemView.setOnClickListener(v -> {
            int previousPosition = selectedPosition;
            selectedPosition = position;
            notifyItemChanged(previousPosition);
            notifyItemChanged(selectedPosition);
            listener.onCategoryClick(category);
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategoryName;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
        }
    }
}
