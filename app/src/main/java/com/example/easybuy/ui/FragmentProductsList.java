package com.example.easybuy.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentProductsList extends Fragment {
    private RecyclerView recyclerView;
    private EditText newProduct;
    private EditText quantify;
    private EditText price;
    private EditText title;
    private String getTitle;
    private Button save;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    List<Products> productsList = new ArrayList<>();
    private String TAG = "TAG";
    private ProductsListAdapter adapter;
    private String auth = FirebaseAuth.getInstance().getUid();
    private String COLLECTION = auth;

    public FragmentProductsList() {
    }

    public static FragmentProductsList newInstance(String param1, String param2) {
        FragmentProductsList fragment = new FragmentProductsList();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_products_list, container, false);


        newProduct = view.findViewById(R.id.editTextProductProductstList);
        save = view.findViewById(R.id.btnSave);
        price = view.findViewById(R.id.editTextPriceProductsList);
        quantify = view.findViewById(R.id.editTextQuantifyProductsList);
        title = view.findViewById(R.id.editTextTitleProductsList);
        recyclerView = view.findViewById(R.id.recyclerProductsList);

        buttonClick();
        configureRecycler();
        getProduct();

        return view;
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
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ProductsListAdapter(getContext(), productsList);
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

                Map<String, Object> user = new HashMap<>();

                user.put("Product", product);
                user.put("Quantify", quantify2);
                user.put("Price", price2);
                db.collection(COLLECTION).document(getTitle).update(user);
            }
        });
    }

    public void getProduct(){
        String products = getContext().toString();
        DocumentReference docRef = db.collection(COLLECTION).document(products);
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
                        configureRecycler();
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
}