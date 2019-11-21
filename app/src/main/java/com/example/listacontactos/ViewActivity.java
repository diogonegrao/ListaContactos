package com.example.listacontactos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.listacontactos.Utils.Utils;
import com.example.listacontactos.entities.Contact;

import org.w3c.dom.Text;

import java.io.Serializable;

public class ViewActivity extends AppCompatActivity {

    // CRIAR O METODO ONCREATE - metodo que apenas e utilizado ao lançar a atividade
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        TextView nome = (TextView)findViewById(R.id.nome2);
        TextView apelido = (TextView)findViewById(R.id.apelido2);
        TextView numero = (TextView)findViewById(R.id.numero2);
        TextView idade = (TextView)findViewById(R.id.idade2);
        TextView email = (TextView)findViewById(R.id.email2);
        TextView endereco = (TextView)findViewById(R.id.endereco2);

        nome.setText(getIntent().getStringExtra(Utils.PARAM_NOME));
        apelido.setText(getIntent().getStringExtra(Utils.PARAM_APELIDO));
        numero.setText(String.valueOf(getIntent().getIntExtra(Utils.PARAM_NUMERO, 0)));
        idade.setText(String.valueOf(getIntent().getIntExtra(Utils.PARAM_IDADE, 0)));
        email.setText(getIntent().getStringExtra(Utils.PARAM_EMAIL));
        endereco.setText(getIntent().getStringExtra(Utils.PARAM_ENDERECO));

    }


    // metodo que faz com que o menu de opcoes apareça
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.ver_detalhes, menu);
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
