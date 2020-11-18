package com.example.easybuy;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ProductsList extends AppCompatActivity {

    private Button btnSaveList;
    public static final String PRODUCTS_LIST = "PRODUCT_SAVE";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_list);
    }
    private void clickButtons(){
        btnSaveList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductsList.this,ProductsList.class);
                intent.putExtra(PRODUCTS_LIST , productList);
                setResult(Activity.RESULT_OK, intent);
            }
        });

    }
}