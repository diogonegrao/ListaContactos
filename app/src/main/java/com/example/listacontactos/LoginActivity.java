package com.example.listacontactos;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.listacontactos.db.Contrato;
import com.example.listacontactos.db.DB;

public class LoginActivity extends AppCompatActivity {

    DB mDbHelper;
    SQLiteDatabase db;
    Cursor c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mDbHelper=new DB(this);
        db= mDbHelper.getReadableDatabase();


        final SharedPreferences sharedPreferences=getSharedPreferences("USER_CREDENTIALS",MODE_PRIVATE);    //invocar credencias do user logado
        final Boolean isloggedin=sharedPreferences.getBoolean("ISLOGGEDIN",false);
        if(isloggedin)  //se estiver logado
        {
            Intent main = new Intent(LoginActivity.this, MainActivity.class);   // passa desta ativiade para a Main
            startActivity(main);
        }
        final String required_user=sharedPreferences.getString("USER","DEFAULT_USER");              //passar o email para sharedPreferences
        final String required_password=sharedPreferences.getString("PASSWORD","DEFAULT_PASSWORD");  //passar a password para sharedPreferences

        final EditText user_field=(EditText)findViewById(R.id.user);        // declaração de editText username
        final EditText password_field=(EditText)findViewById(R.id.password);// declaração de editText password

        Button login=(Button)findViewById(R.id.login);      //botão login
        Button register=(Button)findViewById(R.id.registo); //botão registo



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //guarda valores introduzidos pelo o user em variaveis do tipo string
                String user = user_field.getText().toString();  // atribui a variavel user o valor introduzido na editText do login

                String r_user=null;

                String r_passwd=null;

                //query a base de dados que verifica se o user introduzido existe na base de dados
                c=db.rawQuery(" select * from " + Contrato.User.TABLE_NAME + " where " + Contrato.User.COLUMN_USERNAME + " = ?" , new String [] {user});

                //se existir guarda nas strings o email e a password
                if(c!=null && c.getCount() > 0){
                    c.moveToFirst();
                    r_user = c.getString(c.getColumnIndexOrThrow(Contrato.User.COLUMN_USERNAME));
                    r_passwd = c.getString(c.getColumnIndexOrThrow(Contrato.User.COLUMN_PASSWORD));
                }
                // compara a password introduzida com a passowrd guardada na base de dados se for igual guarda em sharedPreferences e passa para a ativadade Main
                String password = password_field.getText().toString();  // atribui a variavel password o valor introduzido na editText do login
                if(user.equals(r_user) && password.equals(r_passwd)) {
                    SharedPreferences sharedPreferences=getSharedPreferences("USER_CREDENTIALS",MODE_PRIVATE);  //"guarda"as credenciais do user
                    SharedPreferences.Editor editor=sharedPreferences.edit();  //inicialização
                    int idu = c.getInt(c.getColumnIndexOrThrow(Contrato.User._ID));
                    editor.putInt("IDUSER",idu);
                    editor.putString("USER",user);  //passar dados por sharedpreferences
                    editor.putString("PASSWD",password);
                    editor.putBoolean("ISLOGGEDIN",true);
                    editor.commit();
                    Intent main = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(main);
                }
                //caso contrario lança um aviso
                else
                {
                    Toast.makeText(LoginActivity.this,getResources().getString(R.string.avisologin),Toast.LENGTH_LONG).show();
                }
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent register=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(register);
                finish();
            }
        });








    }





}
