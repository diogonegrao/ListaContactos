package com.example.listacontactos;

import android.content.ContentValues;
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

public class RegisterActivity extends AppCompatActivity {
    Intent next_activity;
    DB mDbHelper;
    SQLiteDatabase db;
    Cursor c;
    String email1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //inicilizaçoes da base de dados e declração de editText existentes no cenário
        mDbHelper=new DB(this);
        db= mDbHelper.getReadableDatabase();
        setContentView(R.layout.activity_register);

        next_activity=new Intent(this,LoginActivity.class);

        final EditText username_field = (EditText)findViewById(R.id.username);
        final EditText password_field = (EditText)findViewById(R.id.password);
        final EditText confirm_password_field = (EditText)findViewById(R.id.checkpassword);
        Button registerbutton = (Button)findViewById(R.id.registar);


        //ao carregar no botão regista
        registerbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //extração dos carateres digitados para variaveis do tipo string
                String username = username_field.getText().toString();
                String password_1 = password_field.getText().toString();
                String password_2 = confirm_password_field.getText().toString();

                //query a base de dados que verifica se o email introduzido já existe
                c=db.rawQuery(" select * from " + Contrato.User.TABLE_NAME + " where " + Contrato.User.COLUMN_USERNAME + " = ?" , new String [] {username});

                //caso ja exista user na base de dados lança um aviso
                if(c!=null&&c.getCount()>0)
                {
                    Toast.makeText(RegisterActivity.this,getResources().getString(R.string.avisoregisto),Toast.LENGTH_LONG).show();

                }
                //se as passwordes são iguais o user é adicionado a base de dados
                else {

                    if (password_1.equals(password_2)) {

                        ContentValues cv = new ContentValues();
                        cv.put(Contrato.User.COLUMN_USERNAME, username);
                        cv.put(Contrato.User.COLUMN_PASSWORD, password_1);
                        db.insert(Contrato.User.TABLE_NAME, null, cv);
                        startActivity(next_activity);

                    }

                    //caso contrario emite aviso que as password não correspodem
                    else {
                        Toast.makeText(RegisterActivity.this, getResources().getString(R.string.avisopassword), Toast.LENGTH_LONG).show();
                    }
                }

            }
        });

    }
}


