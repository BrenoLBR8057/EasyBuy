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
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.okhttp.internal.DiskLruCache;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.List;

public class ProductsList extends AppCompatActivity {
    private RecyclerView recyclerView;
    private EditText newProduct;
    private EditText quantify;
    private EditText price;
    private EditText title;
    private boolean isEdition;
    private Button save;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    List<Products> productsList = new ArrayList<>();
    private String TAG = "TAG";
    private ProductsListAdapter adapter;
    private String auth = FirebaseAuth.getInstance().getUid();
    private String COLLECTION = auth;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_list);
        
        loadFields();
//        generateProducts();
        buttonClick();
        configureRecycler();
        getProduct();
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
        recyclerView = findViewById(R.id.recyclerProductsList);
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

                db.collection(products.getTitle()).document(products.getProduct()).update("Product", products);
            }
        });
    }

    private void loadFields() {
        newProduct = findViewById(R.id.editTextProductProductstList);
        save = findViewById(R.id.btnSave);
        price = findViewById(R.id.editTextPriceProductsList);
        quantify = findViewById(R.id.editTextQuantifyProductsList);
        title = findViewById(R.id.editTextTitleProductsList);
    }

//    private void generateProducts(){
//        Intent intent = getIntent();
//        Products products = (Products) intent.getSerializableExtra("product");
//
//        newProduct.setText(products.getProduct());
//        title.setText(products.getTitle());
//        price.setText(products.getPrice().toString());
//        quantify.setText(products.getQuantify());
//        configureRecycler();
//    }

    public void getProduct(){
        Intent intent = getIntent();
        Products product = (Products)intent.getSerializableExtra("product");
        DocumentReference docRef = db.collection(COLLECTION).document(product.getTitle());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    configureRecycler();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        Products products1 = document.toObject(Products.class);
                        productsList.add(products1);
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }
}