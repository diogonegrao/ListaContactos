package com.example.listacontactos;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.listacontactos.Utils.Utils;


public class AddActivity extends AppCompatActivity {
    EditText nome ;
    EditText apelido;
    EditText numero;
    EditText idade;
    EditText email;
    EditText endereco;


    // CRIAR O METODO ONCREATE - metodo que apenas e utilizado ao lançar a atividade
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        nome = (EditText) findViewById(R.id.nome);
        apelido = (EditText) findViewById(R.id.apelido);
        numero = (EditText) findViewById(R.id.numero);
        idade = (EditText) findViewById(R.id.idade);
        email = (EditText) findViewById(R.id.email);
        endereco = (EditText) findViewById(R.id.endereco);
    }


    // metodo que faz com que o menu de opcoes apareça
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.adicionar, menu);
        return true;
    }


    // metodo que permite que as opcoes do menu de opcoes sejam funcionais
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Context mContext = this;
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.cancelar:
                finish();
                return true;
            case R.id.confirmar:
                EditText add_nome = (EditText)findViewById(R.id.nome);
                EditText add_apelido = (EditText)findViewById(R.id.apelido);
                EditText add_numero = (EditText)findViewById(R.id.numero);
                EditText add_idade = (EditText)findViewById(R.id.idade);
                EditText add_email = (EditText)findViewById(R.id.email);
                EditText add_endereco = (EditText)findViewById(R.id.endereco);

                String nome = add_nome.getText().toString();
                String apelido = add_apelido.getText().toString();
                String numero = add_numero.getText().toString();
                String email = add_email.getText().toString();
                String idade = add_idade.getText().toString();
                String endereco = add_endereco.getText().toString();

                // confirmar se os campos Nome, Apelido e Numero foram preenchidos
                if(nome.matches("") || apelido.matches("") || numero.matches("")) {
                    Toast.makeText(AddActivity.this, R.string.preencher, Toast.LENGTH_SHORT).show();
                    return true;
                }

                // alerta se o email nao for valido
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {      // permite verificar se o email introduzido corresponde ao formato pretendido
                                                                            /*
                                                                            public static final Pattern EMAIL_ADDRESS
                                                                                = Pattern.compile(
                                                                                    "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                                                                                    "\\@" +
                                                                                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                                                                                    "(" +
                                                                                        "\\." +
                                                                                        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                                                                                    ")+"
                                                                             );
                                                                             // o email tem que ser do formato: xxxxx@xxxxx.xxxxx , em que x podem ser letras (maisculas ou minusculas) ou numeros. pode incluir . _ % -
                                                                             // tamanho maximo do 1o conjunto->256
                                                                             // tamanho maximo do 2o conjunto->64
                                                                             // tamanho maximo do 3o conjunto->25
                                                                             */
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setCancelable(true);
                    builder.setMessage(R.string.emailinvalido);
                    builder.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });


                    AlertDialog dialog = builder.create();
                    dialog.show();

                    return true;
                }

                Intent output = new Intent();
                output.putExtra(Utils.PARAM_NOME, add_nome.getText().toString());
                output.putExtra(Utils.PARAM_APELIDO, add_apelido.getText().toString());
                output.putExtra(Utils.PARAM_NUMERO, Integer.parseInt(add_numero.getText().toString()));
                output.putExtra(Utils.PARAM_IDADE, Integer.parseInt(add_idade.getText().toString()));
                output.putExtra(Utils.PARAM_EMAIL, add_email.getText().toString());
                output.putExtra(Utils.PARAM_ENDERECO, add_endereco.getText().toString());

                setResult(RESULT_OK, output);
                finish();

            default:
                return super.onOptionsItemSelected(item);
        }
    }



}
