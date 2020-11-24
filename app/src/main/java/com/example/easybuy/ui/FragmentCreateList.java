package com.example.easybuy.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.easybuy.R;
import com.example.easybuy.model.Products;

import static android.app.Activity.RESULT_OK;
import static com.example.easybuy.ui.FragmentShoppingList.KEY_EDIT_PRODUCT;
import static com.example.easybuy.ui.FragmentShoppingList.KEY_NEW_PRODUCT;

public class FragmentCreateList extends Fragment {

    public FragmentCreateList() {
    }

    public static FragmentCreateList newInstance(String param1, String param2) {
        FragmentCreateList fragment = new FragmentCreateList();
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
        View view = inflater.inflate(R.layout.fragment_create_list, container, false);
        btnSave = view.findViewById(R.id.btnCreateList);
        title = view.findViewById(R.id.editTextTitle);
        product = view.findViewById(R.id.editTextProduct);
        quantify = view.findViewById(R.id.editTextQuantify);
        price = view.findViewById(R.id.editTextPrice);
        loadFields();
//        Intent intent = getIntent();
//        if(intent.hasExtra(KEY_NEW_PRODUCT)){
//            isEditing = true;
//
//            title.setText(products.getTitle());
//            product.setText(products.getProduct());
//            quantify.setText(products.getQuantify());
//            price.setText(products.getPrice().toString());
//        }
        buttonClick();
        return view;
    }

    private EditText title;
    private EditText product;
    private EditText quantify;
    private EditText price;
    private Button btnSave;
    private Boolean isEditing = false;
    private Products products;

    private void buttonClick() {

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(isEditing){
//                    products = getTexts();
//                    Intent intent = new Intent(CreateAndEditList.this, ShoppingList.class);
//                    intent.putExtra(KEY_EDIT_PRODUCT, products);
//                    setResult(RESULT_OK, intent);
//                    finish();
//
//                }else{
//                    products = getTexts();
//                    Intent intent = new Intent(CreateAndEditList.this, ShoppingList.class);
//                    intent.putExtra(KEY_NEW_PRODUCT, products);
//                    setResult(RESULT_OK, intent);
//                    finish();
//                }
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

    }
}