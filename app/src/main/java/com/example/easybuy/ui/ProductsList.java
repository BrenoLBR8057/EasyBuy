package com.example.easybuy.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class ProductsList extends AppCompatActivity {
    private EditText newProduct;
    private EditText quantify;
    private EditText price;
    private EditText title;
    private String getTitle;
    private Button save;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    List<Products> productsList = new ArrayList<>();
    private ProductsListAdapter adapter;
    private final String uid = FirebaseAuth.getInstance().getUid();
    private final String COLLECTION = uid;
    public RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadFields();
        buttonClick();
        configureRecycler();
        loadData();
        getProduct();
    }


    private void loadFields(){
        newProduct = findViewById(R.id.editTextProductProductstList);
        price = findViewById(R.id.editTextPriceProductsList);
        quantify = findViewById(R.id.editTextQuantifyProductsList);
        title = findViewById(R.id.editTextTitleProductsList);
        recyclerView = findViewById(R.id.recyclerProductsList);
        save = findViewById(R.id.btnSave);
    }

    private void configureRecycler(){
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ProductsListAdapter(this, productsList);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(adapter);
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

    private void buttonClick() {
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String product = newProduct.getText().toString();
                int quantify2 = Integer.parseInt(quantify.getText().toString());
                Double price2 = Double.parseDouble(price.getText().toString());
                Products products = new Products(product, quantify2, price2);

                Map<String, Object> user = new HashMap<>();

                user.put("Product", product);
                user.put("Quantify", quantify2);
                user.put("Price", price2);
                db.collection(COLLECTION).document(getTitle).update(user);
            }
        });
    }

    public void getProduct(){
        Intent intent = getIntent();
        String products1 = intent.getStringExtra("product");
        DocumentReference docRef = db.collection(COLLECTION).document(products1);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        getTitle = document.get("Title").toString();
                        String product = document.get("Product").toString();
                        int quantify = Integer.parseInt(document.get("Quantify").toString());
                        double price = Double.parseDouble(document.get("Price").toString());
                        Products products1 = new Products(getTitle, product, quantify, price);
                        productsList.add(products1);
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
                title.setText(getTitle);
            }
        });
    }

    private void loadData(){
        db.collection(COLLECTION).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete (@NonNull Task< QuerySnapshot > task) {
                if (task.isSuccessful()) {
                    productsList.clear();
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        Products products = documentSnapshot.toObject(Products.class);
                        products.setTitle(documentSnapshot.getId());
                        productsList.add(products);
                        configureRecycler();
                    }
                } else {
                    Log.d(TAG, "Erro ao pegar documentos", task.getException());
                }
            }
        });
    }
}
