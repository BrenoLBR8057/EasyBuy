package com.example.easybuy.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.easybuy.R;
import com.example.easybuy.model.Products;
import com.example.easybuy.ui.adapter.ProductsListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class ProductsList extends AppCompatActivity {
    private RecyclerView recyclerView;
    private EditText newProduct;
    private EditText quantify;
    private EditText price;
    private boolean isEdition;
    private Button save;
    ShoppingList db;
    List<Products> productsList;
    private String TAG = "TAG";
    private ProductsListAdapter adapter;
    private String auth = FirebaseAuth.getInstance().getUid();
    private String COLLECTION = auth;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_list);
        
        loadFields();
        generateProducts();
        buttonClick();
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            productsList.remove(viewHolder.getAdapterPosition());
            adapter.notifyDataSetChanged();
        }
    };


    private void configureRecycler() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ProductsListAdapter(getApplicationContext(), productsList);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(adapter);
    }

    private void buttonClick() {
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String product = newProduct.getText().toString();
                int quantify2 = Integer.parseInt(quantify.getText().toString());
                Double price2 = Double.parseDouble(price.getText().toString());
                Products products = new Products(product, quantify2, price2);

                db.db.collection(products.getTitle()).document(products.getProduct()).update("Product", products);
            }
        });
    }

    private void loadFields() {
        recyclerView = findViewById(R.id.recyclerProductsList);
        newProduct = findViewById(R.id.editTextProductProductstList);
        save = findViewById(R.id.btnSave);
        price = findViewById(R.id.editTextPriceProductsList);
        quantify = findViewById(R.id.editTextQuantifyProductsList);
    }

    private void generateProducts(){
        Intent intent = getIntent();
        Products products = (Products) intent.getSerializableExtra("Product");
        db.db.collection(COLLECTION).document(products.getTitle()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot documentSnapshot = task.getResult();
                Products products1 = documentSnapshot.toObject(Products.class);

                newProduct.setText(products.getTitle());
                price.setText(products.getPrice().toString());
                quantify.setText(products.getQuantify());
                configureRecycler();
            }
        });
    }
}