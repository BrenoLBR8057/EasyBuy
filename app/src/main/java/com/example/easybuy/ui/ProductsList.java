package com.example.easybuy.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
    private Button save;
    private String getTitle;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    List<Products> productsList = new ArrayList<>();
    private ProductsListAdapter adapter;
    private final String uid = FirebaseAuth.getInstance().getUid();
    public RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.products_list);
        loadFields();
        buttonClick();
        configureRecycler();
        getProduct();
    }


    private void loadFields(){
        newProduct = findViewById(R.id.editTextProductProductstList);
        price = findViewById(R.id.editTextPriceProductsList);
        quantify = findViewById(R.id.editTextQuantifyProductsList);
        recyclerView = findViewById(R.id.recyclerProductsList);
        title = findViewById(R.id.editTextTitleProductsList);
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
        save = findViewById(R.id.btnSaveProductsList);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String product = newProduct.getText().toString();
                int quantify2 = Integer.parseInt(quantify.getText().toString());
                Double price2 = Double.parseDouble(price.getText().toString());
                Products products = new Products(product, quantify2, price2);

                Map<String, Object> user = new HashMap<>();
                db.collection(uid).document(getTitle).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        int id = 0;
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();

                            if (documentSnapshot.exists()) {
                                while (documentSnapshot.contains("Products" + id)) {
                                    id++;
                                }
                                if (id == 0) {
                                    user.put("Product" + id, product);
                                    user.put("Quantify" + id, quantify2);
                                    user.put("Price" + id, price2);
                                    user.put("Id", id);
                                    db.collection(uid).document(getTitle).update(user);
                                } else {
                                    id = productsList.size() - 1;
                                    user.put("Product" + id, product);
                                    user.put("Quantify", quantify2);
                                    user.put("Price", price2);
                                    user.put("Id", id);
                                    db.collection(uid).document(getTitle).update(user);
                                }
                            }
                        }
                    }
                });
            }
        });
    }


    public void getProduct(){
        Intent intent = getIntent();
        Products products1 = (Products) intent.getSerializableExtra("product");
        getTitle = products1.getTitle();
        DocumentReference docRef = db.collection(uid).document(getTitle);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        int i = 0;
                        while (document.contains("Product" + i)){
                            String id = String.valueOf(i);
                            String product = document.get("Product" + i).toString();
                            int quantify = Integer.parseInt(document.get("Quantify" + i).toString());
                            double price = Double.parseDouble(document.get("Price" + i).toString());
                            Products products1 = new Products(getTitle, product, quantify, price);
                            productsList.add(products1);
                            i++;
                            configureRecycler();
                        }
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
                title.setText(getTitle);
                configureRecycler();
            }
        });
    }
}
