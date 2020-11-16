package com.example.easybuy.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.easybuy.R;
import com.example.easybuy.model.Products;
import com.example.easybuy.ui.adapter.ShoppingListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.widget.Toast.LENGTH_SHORT;

public class ShoppingList extends AppCompatActivity {
    private int REQUEST_CODE_NEW_PRODUCT = 1;
    public static String KEY_NEW_PRODUCT = "NEW_PRODUCT";
    private int REQUEST_CODE_EDIT_PRODUCT = 2;
    public static String KEY_EDIT_PRODUCT = "EDIT_PRODUCT";
    private FloatingActionButton fabShoppingList;
    private RecyclerView recyclerView;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String TAG = "TAG";
    private List<Products> productsList;
    private ShoppingListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        productsList = new ArrayList<>();
        buttonClick();
        configureRecycler();
        loadData();
    }

    private void configureRecycler() {
        recyclerView = findViewById(R.id.recyclerShoppingList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ShoppingListAdapter(getApplicationContext(), productsList);
        recyclerView.setAdapter(adapter);
    }

    private void buttonClick() {
        fabShoppingList = findViewById(R.id.fabShoppingList);

        fabShoppingList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShoppingList.this, CreateAndEditList.class);
                startActivityForResult(intent, REQUEST_CODE_NEW_PRODUCT);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_NEW_PRODUCT && resultCode == RESULT_OK && data.hasExtra(KEY_NEW_PRODUCT)){
            Products products = (Products)data.getSerializableExtra(KEY_NEW_PRODUCT);
            Map<String, Object> user = new HashMap<>();
            user.put("Title", products.getTitle());
            user.put("Product", products.getProduct());
            user.put("Description", products.getDescription());
            user.put("Quantify", products.getQuantify());
            user.put("Price", products.getPrice());

            db.collection("Shopping List")
                    .add(user)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(ShoppingList.this, "Success", LENGTH_SHORT);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error adding document", e);
                        }
                    });
            loadData();
        }else if(requestCode == REQUEST_CODE_EDIT_PRODUCT && resultCode == RESULT_OK && data.hasExtra(KEY_EDIT_PRODUCT)){
            Products products = (Products) data.getSerializableExtra(KEY_EDIT_PRODUCT);

            db.collection("Shopping List").document(String.valueOf(products.getId())).set(products);
            loadData();
        }
    }

    void loadData(){
        db.collection("Shopping List").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete (@NonNull Task< QuerySnapshot > task) {
                if (task.isSuccessful()) {
                    productsList.clear();
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        Products products = documentSnapshot.toObject(Products.class);
                        products.setId(documentSnapshot.getId());
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