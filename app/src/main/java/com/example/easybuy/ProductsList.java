package com.example.easybuy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ProductsList extends AppCompatActivity {
    private Button buttonLista;
    private Button buttonSalva;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ProductsList);

        clickButton();
    }

    private void clickButton() {
        buttonLista = findViewById(R.id.buttonLista);
        buttonSalva = findViewById(R.id.buttonSalva);

        buttonLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Lista lista = new Lista();
                trocaLista(lista);
            }


        });
        buttonSalva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Salva salva = new Salva();
                trocaSalva(salva);
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

        }
}