package com.example.easybuy.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.easybuy.MainActivity;
import com.example.easybuy.R;
import com.example.easybuy.model.Products;

import static com.example.easybuy.ui.FragmentShoppingList.KEY_EDIT_PRODUCT;
import static com.example.easybuy.ui.FragmentShoppingList.KEY_NEW_PRODUCT;

public class CreateList extends AppCompatActivity {
    private EditText title;
    private EditText product;
    private EditText quantify;
    private EditText price;
    private Button btnSave;
    private Boolean isEditing = false;
    private Products products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_list);
        loadFields();
        Intent intent = getIntent();
        if(intent.hasExtra(KEY_NEW_PRODUCT)){
            isEditing = true;

            title.setText(products.getTitle());
            product.setText(products.getProduct());
            quantify.setText(products.getQuantify());
            price.setText(products.getPrice().toString());
        }
        buttonClick();
    }

    private void buttonClick() {
        btnSave = findViewById(R.id.btnCreateList);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isEditing){
                    products = getTexts();
                    Intent intent = new Intent(CreateList.this, MainActivity.class);
                    intent.putExtra(KEY_EDIT_PRODUCT, products);
                    setResult(RESULT_OK, intent);
                    finish();

                }else{
                    products = getTexts();
                    Intent intent = new Intent(CreateList.this, MainActivity.class);
                    intent.putExtra(KEY_NEW_PRODUCT, products);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }

    private Products getTexts() {
        String title = this.title.getText().toString();
        String product = this.product.getText().toString();
        int quantify = Integer.parseInt(this.quantify.getText().toString());
        double price = Double.parseDouble(this.price.getText().toString());

        return new Products(title, product, quantify, price);
    }

    private void loadFields() {
        btnSave = findViewById(R.id.btnCreateList);
        title = findViewById(R.id.editTextTitle);
        product = findViewById(R.id.editTextProduct);
        quantify = findViewById(R.id.editTextQuantify);
        price = findViewById(R.id.editTextPrice);
    }
}
