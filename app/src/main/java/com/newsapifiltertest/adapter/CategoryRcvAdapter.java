package com.newsapifiltertest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.newsapifiltertest.R;
import com.newsapifiltertest.modals.CategoryModal;

import java.util.ArrayList;

public class CategoryRcvAdapter extends RecyclerView.Adapter<CategoryRcvAdapter.ViewHolder> {
    private ArrayList<CategoryModal> categoryModals;
    private Context context;
    private CategoryClickInterface categoryClickInterface;

    public CategoryRcvAdapter(ArrayList<CategoryModal> categoryModals, Context context, CategoryClickInterface categoryClickInterface) {
        this.categoryModals = categoryModals;
        this.context = context;
        this.categoryClickInterface = categoryClickInterface;
    }

    @NonNull
    @Override
    public CategoryRcvAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rcv_item,parent,false);
        return new CategoryRcvAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryRcvAdapter.ViewHolder holder, int position) {
        CategoryModal categoryModal = categoryModals.get(position);
        holder.categoryTV.setText(categoryModal.getCategory());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                categoryClickInterface.onCategoryClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryModals.size();
    }

    //to change the data on category click in the main act.
    public interface CategoryClickInterface{
        void onCategoryClick(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView categoryTV;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryTV = itemView.findViewById(R.id.tv_category);
        }
    }
}
