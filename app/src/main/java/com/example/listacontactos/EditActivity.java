package com.example.listacontactos;

import androidx.appcompat.app.AppCompatActivity;

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

import com.example.listacontactos.Utils.Utils;

public class EditActivity extends AppCompatActivity {


    // CRIAR O METODO ONCREATE - metodo que apenas e utilizado ao lançar a atividade
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        EditText edit_nome = (EditText)findViewById(R.id.nome);
        EditText edit_apelido = (EditText)findViewById(R.id.apelido);
        EditText edit_numero = (EditText)findViewById(R.id.numero);
        EditText edit_idade = (EditText)findViewById(R.id.idade);
        EditText edit_email = (EditText)findViewById(R.id.email);
        EditText edit_endereco = (EditText)findViewById(R.id.endereco);


        edit_nome.setText(getIntent().getStringExtra(Utils.PARAM_NOME));
        edit_apelido.setText(getIntent().getStringExtra(Utils.PARAM_APELIDO));
        edit_numero.setText(String.valueOf(getIntent().getIntExtra(Utils.PARAM_NUMERO, 0)));
        edit_idade.setText(String.valueOf(getIntent().getIntExtra(Utils.PARAM_IDADE, 0)));
        edit_email.setText(getIntent().getStringExtra(Utils.PARAM_EMAIL));
        edit_endereco.setText(getIntent().getStringExtra(Utils.PARAM_ENDERECO));
    }


    // metodo que faz com que o menu de opcoes apareça
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.editar ,menu);
        return true;
    }

    // metodo que permite que as opcoes do menu de opcoes sejam funcionais
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Context mContext = this;
        switch(item.getItemId()) {
            case R.id.cancelar:
                finish();
                return true;
            case R.id.check:
                EditText edit_nome = (EditText)findViewById(R.id.nome);
                EditText edit_apelido = (EditText)findViewById(R.id.apelido);
                EditText edit_numero = (EditText)findViewById(R.id.numero);
                EditText edit_idade = (EditText)findViewById(R.id.idade);
                EditText edit_email = (EditText)findViewById(R.id.email);
                EditText edit_endereco = (EditText)findViewById(R.id.endereco);

                String nome = edit_nome.getText().toString();
                String apelido = edit_apelido.getText().toString();
                String numero = edit_numero.getText().toString();
                String idade = edit_idade.getText().toString();
                String email = edit_email.getText().toString();
                String endereco = edit_endereco.getText().toString();

                if (nome.matches("") || apelido.matches("") || numero.matches("")) {
                    Toast.makeText(EditActivity.this,R.string.preencher, Toast.LENGTH_SHORT).show();
                    return true;
                }

                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {  // permite verificar se o email introduzido corresponde ao formato pretendido
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
                output.putExtra(Utils.PARAM_NOME, edit_nome.getText().toString());
                output.putExtra(Utils.PARAM_APELIDO, edit_apelido.getText().toString());
                output.putExtra(Utils.PARAM_NUMERO, Integer.parseInt(edit_numero.getText().toString()));
                output.putExtra(Utils.PARAM_IDADE, Integer.parseInt(edit_idade.getText().toString()));
                output.putExtra(Utils.PARAM_EMAIL, edit_email.getText().toString());
                output.putExtra(Utils.PARAM_ENDERECO, edit_endereco.getText().toString());

                int i = getIntent().getIntExtra(Utils.PARAM_INDEX, 0);
                output.putExtra(Utils.PARAM_INDEX, i);

                setResult(RESULT_OK, output);

                Toast.makeText(EditActivity.this,R.string.editado, Toast.LENGTH_SHORT).show();

                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
