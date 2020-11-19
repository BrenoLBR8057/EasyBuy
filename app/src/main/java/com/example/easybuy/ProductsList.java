package com.example.easybuy;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ProductsList extends AppCompatActivity {

    private Button btnSaveList;
    public static final String PRODUCTS_LIST = "PRODUCT_SAVE";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_list);
    }

    private void clickButtons(){

             Button button = findViewById(R.id.btnSaveList);
             btnSaveList.setOnClickListener (new View.OnClickListener() {

                @Override
                public void onClick(View v) {


                }
        });
    }
}

