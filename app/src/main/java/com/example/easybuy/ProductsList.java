package com.example.easybuy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.easybuy.dummy.ListaFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ProductsList extends AppCompatActivity {
    private Button btnSaveList;
    private FloatingActionButton fabSalvaList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ProductsList);

        clickButton();
    }

    private void clickButton() {
        btnSaveList = findViewById(R.id.btnSaveList);
        fabSalvaList = findViewById(R.id.fabSalvaList);

        btnSaveList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListaFragment listaFragment = new ListaFragment();

            }
        });

        fabSalvaList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FabSalvaList fabSalvaList = new FabSalvaList();


            }

            private void trocaSalva(Salva salva) {
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.fragment_container, salva);
                transaction.commit();
            }
        });
    }

    private void trocaLista(Lista lista) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment_container, lista);
        transaction.commit();
    }
}