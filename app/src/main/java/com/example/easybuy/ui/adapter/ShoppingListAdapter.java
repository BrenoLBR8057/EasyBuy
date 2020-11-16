package com.example.easybuy.ui.adapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.easybuy.R;
import com.example.easybuy.model.Products;
import com.example.easybuy.ui.ShoppingList;

import java.util.ArrayList;
import java.util.List;

public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.ViewHolder> {
    private List<Products> productsList;
    private Context context;

    public ShoppingListAdapter(Context context, List<Products> productsList){
        this.context = context;
        this.productsList = productsList;
    }

    @NonNull
    @Override
    public ShoppingListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_shopping_list_adapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShoppingListAdapter.ViewHolder holder, int position) {
        Products products = productsList.get(position);
        holder.linkProduct(products);
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView shoppingList;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            shoppingList = itemView.findViewById(R.id.textViewShoppingList);
        }

        public void linkProduct(Products products){
            shoppingList.setText(products.getTitle());
        }
    }
}