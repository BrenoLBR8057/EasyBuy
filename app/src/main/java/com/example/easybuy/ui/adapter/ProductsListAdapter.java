package com.example.easybuy.ui.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.easybuy.R;
import com.example.easybuy.model.Products;

import java.util.List;

public class ProductsListAdapter extends RecyclerView.Adapter<ProductsListAdapter.ViewHolder> {
    private Context context;
    private List<Products> productsList;

    public ProductsListAdapter(Context context, List<Products> productsList) {
        this.context = context;
        this.productsList = productsList;
    }

    @NonNull
    @Override
    public ProductsListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_products_list_adapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductsListAdapter.ViewHolder holder, int position) {
        Products products = productsList.get(position);
        holder.setProducts(products);
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView newProducts;
        private TextView quantify;
        private TextView price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            newProducts = itemView.findViewById(R.id.textViewProduct);
            quantify = itemView.findViewById(R.id.textViewQuantify);
            price = itemView.findViewById(R.id.textViewPrice);
        }

        public void setProducts(Products products) {
            newProducts.setText(products.getProduct());
            quantify.setText(String.valueOf(products.getQuantify()));
            price.setText(String.valueOf(products.getPrice()));
        }
    }
}