package com.example.easybuy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ShoppingList extends AppCompatActivity {
    private FloatingActionButton fabShoppingList;
    private RecyclerView recyclerView;
    private Button btnSaveList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_shopping);

        buttonClick();
        loadViews();
    }

    private void buttonClick() {
        fabShoppingList = findViewById(R.id.fabShoppingList);
        btnSaveList = findViewById(R.id.btnSaveList);

        fabShoppingList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShoppingList.this, ProductsList.class);
                int REQUEST_CODE = 1;
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

//        btnSaveList.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(ShoppingList.this, ProductsList.class);
//                int REQUEST_CODE = 2;
//                startActivityForResult(intent, REQUEST_CODE);
//            }
//        });
    }
   private void loadViews() {
       View btnSaveList = findViewById(R.id.btnSaveList);

    }
}
