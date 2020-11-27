package com.example.easybuy;

import android.content.Intent;
import android.os.Bundle;

import com.example.easybuy.model.Products;
import com.example.easybuy.ui.CreateList;
import com.example.easybuy.ui.Login;
import com.example.easybuy.ui.ProductsList;
import com.example.easybuy.ui.adapter.ShoppingListAdapter;
import com.example.easybuy.ui.helper.MyItemTouchHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.easybuy.ui.main.SectionsPagerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.easybuy.ui.FragmentShoppingList.KEY_NEW_PRODUCT;

public class MainActivity extends AppCompatActivity {
    private FloatingActionButton fab;
    private int REQUEST_CODE_NEW_PRODUCT = 1;
    private String DOCUMENT;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String auth = FirebaseAuth.getInstance().getUid();
    private RecyclerView recyclerView;
    private ShoppingListAdapter adapter;
    private List<Products> productsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        productsList = new ArrayList<>();
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        fab = findViewById(R.id.fabShoppingList);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateList.class);
                startActivityForResult(intent, REQUEST_CODE_NEW_PRODUCT);
            }
        });
    }

    public void goToProductsList(Products products){
        Intent intent = new Intent(MainActivity.this, ProductsList.class);
        intent.putExtra("product", products);
        startActivity(intent);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_NEW_PRODUCT && resultCode == RESULT_OK && data.hasExtra(KEY_NEW_PRODUCT)){
            Products products = (Products)data.getSerializableExtra(KEY_NEW_PRODUCT);
            DOCUMENT = products.getTitle();
            Map<String, Object> user = new HashMap<>();
            user.put("Title", products.getTitle());
            user.put("Product", products.getProduct());
            user.put("Quantify", products.getQuantify());
            user.put("Price", products.getPrice());

            db.collection(auth).document(DOCUMENT).set(user);
            loadData();
        }
    }

    private void loadData(){
        db.collection(auth).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete (@NonNull Task< QuerySnapshot > task) {
                if (task.isSuccessful()) {
                    productsList.clear();
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        String title = documentSnapshot.getId();
                        String product = documentSnapshot.get("Product").toString();
                        int quantify = Integer.parseInt(documentSnapshot.get("Quantify").toString());
                        Double price = Double.parseDouble(documentSnapshot.get("Price").toString());
                        Products products1 = new Products(title, product, quantify, price);
                        productsList.add(products1);
                        configureRecycler();
                    }
                } else {
                    String TAG = "";
                    Log.d(TAG, "Erro ao pegar documentos", task.getException());
                }
            }
        });
    }

    private void configureRecycler() {
        recyclerView = findViewById(R.id.recyclerProductsList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ShoppingListAdapter(this, productsList);

        ItemTouchHelper.Callback callback = new MyItemTouchHelper(adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        adapter.setTouchHelper(itemTouchHelper);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        recyclerView.setAdapter(adapter);
    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.itemLogOut:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}