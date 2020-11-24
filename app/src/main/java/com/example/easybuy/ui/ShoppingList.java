package com.example.easybuy.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.easybuy.R;
import com.example.easybuy.ui.main.SectionsPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class ShoppingList extends AppCompatActivity {
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);
       button = findViewById(R.id.btnShoppingList);

       onClick();
    }

    private void onClick() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentShoppingList fragmentA = new FragmentShoppingList();
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.fragment, fragmentA);
                transaction.commit();
            }
        });
    }
}