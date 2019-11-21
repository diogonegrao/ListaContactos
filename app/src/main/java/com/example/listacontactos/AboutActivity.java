package com.example.listacontactos;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class AboutActivity extends AppCompatActivity {

    // CRIAR O METODO ONCREATE - metodo que apenas e utilizado ao lançar a atividade
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }

    // metodo que faz com que o menu de opcoes apareça
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sobre, menu);
        return true;
    }

    // metodo que permite que as opcoes do menu de opcoes sejam funcionais
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.voltar:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
