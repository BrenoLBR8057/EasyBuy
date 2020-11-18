package com.example.easybuy.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
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
import com.example.easybuy.ui.helper.MyItemTouchHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.widget.Toast.LENGTH_SHORT;
import static androidx.recyclerview.widget.ItemTouchHelper.Callback.makeMovementFlags;

public class ShoppingList extends AppCompatActivity {
    private int REQUEST_CODE_NEW_PRODUCT = 1;
    public static String KEY_NEW_PRODUCT = "NEW_PRODUCT";
    private int REQUEST_CODE_EDIT_PRODUCT = 2;
    public static String KEY_EDIT_PRODUCT = "EDIT_PRODUCT";
    private FloatingActionButton fabShoppingList;
    private RecyclerView recyclerView;
    public FirebaseFirestore db = FirebaseFirestore.getInstance();
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
        adapter = new ShoppingListAdapter(this, productsList);

        ItemTouchHelper.Callback callback = new MyItemTouchHelper(adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        adapter.setTouchHelper(itemTouchHelper);
        itemTouchHelper.attachToRecyclerView(recyclerView);

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
            db.collection("Shopping List").add(products);
            loadData();
        }else if(requestCode == REQUEST_CODE_EDIT_PRODUCT && resultCode == RESULT_OK && data.hasExtra(KEY_EDIT_PRODUCT)){
            Products products = (Products) data.getSerializableExtra(KEY_EDIT_PRODUCT);

            db.collection("Shopping List").document(products.getId()).set(products);
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

    public void ProductsListener(int position){
        db.collection("Shopping List").document(String.valueOf(position)).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot documentSnapshot = task.getResult();
                Products products = documentSnapshot.toObject(Products.class);
                products.setId(documentSnapshot.getId());

                Intent intent = new Intent(ShoppingList.this, ProductsList.class);
                intent.putExtra("Product", products);
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });
    }
}