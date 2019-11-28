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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.listacontactos.db.Contrato;
import com.example.listacontactos.db.DB;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    Intent next_activity;
    DB mDbHelper;
    SQLiteDatabase db;
    EncriptarPass e = new EncriptarPass();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //inicilizaçoes da base de dados e declaração de editText existentes no cenário
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
                final String username = username_field.getText().toString();
                final String password_1 = password_field.getText().toString();
                final String password_2 = confirm_password_field.getText().toString();


                if (password_1.length() < 6) {
                    Toast.makeText(RegisterActivity.this, R.string.passwordcurta, Toast.LENGTH_SHORT).show();
                }
                else {
                    String url = "http://listacontactos.000webhostapp.com/listacontactos/api/user" + username;

                    JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {

                            Toast.makeText(RegisterActivity.this, R.string.userexiste, Toast.LENGTH_SHORT).show();
                        }

                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            String passwordEnc=null;
                            if (password_1.equals(password_2)) {
                                registaUser(username, password_1);
                                startActivity(next_activity);
                            }
                            else{
                                Toast.makeText(RegisterActivity.this, R.string.avisopassword, Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                    MySingleton.getInstance(RegisterActivity.this).addToRequestQueue(jsonArrayRequest);

                }
            }


            public void registaUser(String username,String password) {
                String url = "http://listacontactos.000webhostapp.com/listacontactos/api/user";
                Map<String, String> jsonParams = new HashMap<String, String>();
                jsonParams.put("username",username);
                jsonParams.put("password",password);


                JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(jsonParams), new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("status")) {
                                Toast.makeText(RegisterActivity.this, response.getString("MSG"), Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(RegisterActivity.this, response.getString("MSG"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException ex) {
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RegisterActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }){

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("Content-Type", "application/json; charset=utf-8");
                        headers.put("User-agent", System.getProperty("http.agent"));
                        return headers;
                    }
                };

                MySingleton.getInstance(RegisterActivity.this).addToRequestQueue(postRequest);

                finish();
            }
        });
    }
}
