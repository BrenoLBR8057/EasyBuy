package com.example.easybuy.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.easybuy.MainActivity;
import com.example.easybuy.R;
import com.example.easybuy.model.Products;
import com.example.easybuy.ui.adapter.ShoppingListAdapter;
import com.example.easybuy.ui.helper.MyItemTouchHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class FragmentShoppingList extends Fragment {
    private int REQUEST_CODE_NEW_PRODUCT = 1;
    public static String KEY_NEW_PRODUCT = "NEW_PRODUCT";
    private int REQUEST_CODE_EDIT_PRODUCT = 2;
    public static String KEY_EDIT_PRODUCT = "EDIT_PRODUCT";
    private RecyclerView recyclerView;
    public FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String TAG = "TAG";
    private List<Products> productsList;
    private ShoppingListAdapter adapter;
    private String uid = FirebaseAuth.getInstance().getUid();
    private String COLLECTION = uid;
    private String DOCUMENT;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shopping_list, container, false);
        loadFields(view);
        productsList = new ArrayList<>();
        configureRecycler();
        loadData();
        return view;
    }

    private void loadFields(View view){
        recyclerView = view.findViewById(R.id.recyclerShoppingList);
    }

    private void configureRecycler() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ShoppingListAdapter(getContext(), productsList);

        ItemTouchHelper.Callback callback = new MyItemTouchHelper(adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        adapter.setTouchHelper(itemTouchHelper);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_NEW_PRODUCT && resultCode == RESULT_OK && data.hasExtra(KEY_NEW_PRODUCT)){
            Products products = (Products)data.getSerializableExtra(KEY_NEW_PRODUCT);
            DOCUMENT = products.getTitle();
            Map<String, Object> user = new HashMap<>();
            user.put("Title", products.getTitle());
            user.put("Product", products.getProduct());
            user.put("Quantify", products.getQuantify());
            user.put("Price", products.getPrice());

            db.collection(COLLECTION).document(DOCUMENT).set(user);
            loadData();
        }else if(requestCode == REQUEST_CODE_EDIT_PRODUCT && resultCode == RESULT_OK && data.hasExtra(KEY_EDIT_PRODUCT)){
            Products products = (Products) data.getSerializableExtra(KEY_EDIT_PRODUCT);

            db.collection(COLLECTION).document(products.getTitle()).set(products);
            loadData();
        }
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