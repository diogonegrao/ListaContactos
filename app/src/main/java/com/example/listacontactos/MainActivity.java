package com.example.listacontactos;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.listacontactos.adapter.CustomArrayAdapter;
import com.example.listacontactos.adapter.MyCursorAdapter;
import com.example.listacontactos.db.Contrato;
import com.example.listacontactos.db.DB;
import com.example.listacontactos.entities.Contact;

import com.example.listacontactos.Utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements Serializable {

    private  int REQUEST_CODE_OP = 1;
    private  int REQUEST_CODE_OP_2 = 2;


    SQLiteDatabase db;
    ListView listView;

    ArrayList<Contact> arraycon = new ArrayList<>();


    // CRIAR O METODO ONCREATE - metodo que apenas e utilizado ao lançar a atividade
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);     // 1ª atividade lançada pela aplicacao
        registerForContextMenu(findViewById(R.id.lista));


        listView = (ListView)findViewById(R.id.lista);  // representa a lista declarada no layout


        fillLista();      // metodo para preencher a lista


        // com um click abre a atividade onde e possivel ver todos os detalhes do contacto
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                Intent intent = new Intent(MainActivity.this, ViewActivity.class);

                intent.putExtra(Utils.PARAM_NOME, arraycon.get(i).nome);
                intent.putExtra(Utils.PARAM_APELIDO, arraycon.get(i).apelido);
                intent.putExtra(Utils.PARAM_NOME, arraycon.get(i).numero);
                intent.putExtra(Utils.PARAM_NOME, arraycon.get(i).idade);
                intent.putExtra(Utils.PARAM_NOME, arraycon.get(i).email);
                intent.putExtra(Utils.PARAM_NOME, arraycon.get(i).endereco);
                intent.putExtra(Utils.PARAM_NOME, arraycon.get(i).cidade);
                // utiliza o valor que esta no array para atribuir as variaveis do UTILS os dados correspondentes


                startActivity(intent);
            }
        });
    }

    /*
    private void getCursor() {

        // construir a query para o cursor
        String sql = "select " + Contrato.Contacto.TABLE_NAME + "." +
                Contrato.Contacto._ID + "," +
                Contrato.Contacto.COLUMN_NOME + "," +
                Contrato.Contacto.COLUMN_APELIDO + "," +
                Contrato.Contacto.COLUMN_NUMERO + "," +
                Contrato.Contacto.COLUMN_EMAIL + "," +
                Contrato.Contacto.COLUMN_ENDERECO + "," +
                Contrato.Contacto.COLUMN_IDADE + " FROM " +
                Contrato.Contacto.TABLE_NAME +
                " WHERE " + Contrato.Contacto.COLUMN_ID_USER + "=" + iduser;
        c = db.rawQuery(sql, null);     // toda a info recolhida a partir da query, e posta no cursor c

    }
     */


    private void fillLista(){
        arraycon.removeAll(arraycon);

        String url = "http://listacontactos.000webhostapp.com/listacontactos/api/contactos";

        JsonArrayRequest jsonArrRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject obj = response.getJSONObject(i);
                        arraycon.add(new Contact(obj.getInt("id"),obj.getString("nome"), obj.getString("apelido"), obj.getInt("numero"), obj.getInt("idade"), obj.getString("email"), obj.getString("endereco"), obj.getString("cidade")));

                        CustomArrayAdapter itemsAdapter = new CustomArrayAdapter(MainActivity.this, arraycon);
                        ((ListView) findViewById(R.id.lista)).setAdapter(itemsAdapter);
                    }
                } catch (JSONException e) {
                    Log.d("fillLista", "" + e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        MySingleton.getInstance(this).addToRequestQueue(jsonArrRequest);
    }


    // metodo que faz com que o menu de opcoes apareça
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.lista, menu);
        return true;
    }


    // metodo que permite que as opcoes do menu de opcoes sejam funcionais
    // metodo para saltar para a atividade onde é possivel criar um contacto ou ler sobre a aplicacao
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle item selection
        switch (item.getItemId()) {
            case R.id.adicionar:
                Intent i = new Intent(MainActivity.this, AddActivity.class);
                startActivityForResult(i, REQUEST_CODE_OP);
                break;
            /*
            case R.id.logout:
                login=new Intent(this,LoginActivity.class);
                startActivity(login);
                finish();
                break;

             */

            case R.id.sobre:
                Intent j = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(j);
                break;

                // pesquisa por nome
            /*
            case R.id.pesquisa:
                pesquisanome = new EditText(this);

                AlertDialog.Builder builder2 = new AlertDialog.Builder(mContext);
                builder2.setCancelable(true);
                builder2.setMessage(R.string.pesquisarnome);
                builder2.setView(pesquisanome);
                builder2.setPositiveButton(R.string.confirmar2,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(!pesquisanome.getText().toString().isEmpty()) {
                                    preencheListaPesquisaNome(pesquisanome.getText().toString());
                                }
                            }
                        });
                builder2.setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                AlertDialog dialog2 = builder2.create();
                dialog2.show();
                break;

             */

            // ordenar por idade
            case R.id.ordenar:
                preencheListaOrdenaIdade();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }


    // CRIAR O METODO ONRESUME
    // metodo que e lançado sempre que a aplicacao volta a esta atividade
    @Override
    protected void onResume() {
        super.onResume();
        fillLista();
    }



    // metodo que permite que os valores introduzidos numa atividade nova lançada, sejam recuperados para depois serem apresentados nesta atividade
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == REQUEST_CODE_OP) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {

                // PARA A ATIVIDADE DE ADICIONAR UM CONTACTO
                String url = "http://listacontactos.000webhostapp.com/listacontactos/api/contacto";

                Map<String, String> jsonParams = new HashMap<String, String>();
                jsonParams.put("nome", data.getStringExtra(Utils.PARAM_NOME));
                jsonParams.put("apelido", data.getStringExtra(Utils.PARAM_APELIDO));
                jsonParams.put("numero", String.valueOf(data.getIntExtra(Utils.PARAM_NUMERO, -1)));
                jsonParams.put("idade", String.valueOf(data.getIntExtra(Utils.PARAM_IDADE, -1)));
                jsonParams.put("email", data.getStringExtra(Utils.PARAM_EMAIL));
                jsonParams.put("endereco", data.getStringExtra(Utils.PARAM_ENDERECO));
                jsonParams.put("user_id", "1");
                jsonParams.put("cidade_id", data.getStringExtra(Utils.PARAM_CIDADE));

                fillLista();

                JsonObjectRequest post = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(jsonParams), new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("status")) {
                                Toast.makeText(MainActivity.this, response.getString("MSG"), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MainActivity.this, response.getString("MSG"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("Content-Type", "application/json; charset=utf-8");
                        headers.put("User-agent", System.getProperty("http.agent"));
                        return headers;
                    }
                };

                MySingleton.getInstance(this).addToRequestQueue(post);
            }
        }

        if (requestCode == REQUEST_CODE_OP_2) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {

                // PARA A ATIVIDADE DE EDITAR UM CONTACTO
                String url = "http://listacontactos.000webhostapp.com/listacontactos/api/contacto";

                Map<String, String> jsonParams = new HashMap<String, String>();
                jsonParams.put("nome", data.getStringExtra(Utils.PARAM_NOME));
                jsonParams.put("apelido", data.getStringExtra(Utils.PARAM_APELIDO));
                jsonParams.put("numero", String.valueOf(data.getIntExtra(Utils.PARAM_NUMERO, -1)));
                jsonParams.put("idade", String.valueOf(data.getIntExtra(Utils.PARAM_IDADE, -1)));
                jsonParams.put("email", data.getStringExtra(Utils.PARAM_EMAIL));
                jsonParams.put("endereco", data.getStringExtra(Utils.PARAM_ENDERECO));
                jsonParams.put("user_id", "1");
                jsonParams.put("cidade_id", data.getStringExtra(Utils.PARAM_CIDADE));

                fillLista();

                JsonObjectRequest post = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(jsonParams), new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("status")) {
                                Toast.makeText(MainActivity.this, response.getString("MSG"), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MainActivity.this, response.getString("MSG"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("Content-Type", "application/json; charset=utf-8");
                        headers.put("User-agent", System.getProperty("http.agent"));
                        return headers;
                    }
                };

                MySingleton.getInstance(this).addToRequestQueue(post);
            }
        }
    }


    // metodo que permite que com um long click seja apresentado o menu contextual (Editar e Remover)
    @Override
    public void onCreateContextMenu(ContextMenu menu,View view,ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu,view,menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.contextual, menu);
    }


    // metodo que atua em conformidade com a opcao escolha apos o long click
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        //final int index = info.position;
        Context mContext = this;
        int itemPosition = info.position;   // posicao que foi clicada na lista

        switch (item.getItemId()) {
            case R.id.editar:
                Intent intent = new Intent(MainActivity.this, EditActivity.class);

                int id_contacto = arraycon.get(itemPosition).id;
                intent.putExtra(Utils.PARAM_NOME, id_contacto);
                intent.putExtra(Utils.PARAM_NOME, arraycon.get(itemPosition).nome);
                intent.putExtra(Utils.PARAM_APELIDO, arraycon.get(itemPosition).apelido);
                intent.putExtra(Utils.PARAM_NUMERO, arraycon.get(itemPosition).numero);
                intent.putExtra(Utils.PARAM_IDADE, arraycon.get(itemPosition).idade);
                intent.putExtra(Utils.PARAM_EMAIL, arraycon.get(itemPosition).email);
                intent.putExtra(Utils.PARAM_ENDERECO, arraycon.get(itemPosition).endereco);
                intent.putExtra(Utils.PARAM_CIDADE, arraycon.get(itemPosition).cidade);
                // utiliza o valor que esta no array para atribuir as variaveis do UTILS os dados correspondentes (para estes estarem visiveis na edicao)

                startActivityForResult(intent, REQUEST_CODE_OP_2);

                return true;

            case R.id.remover:
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(mContext);
                builder.setCancelable(true);
                builder.setMessage(R.string.confirmar1);
                builder.setPositiveButton(R.string.confirmar2,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                int itemPosition = info.position;
                                int remove_id = arraycon.get(itemPosition).id;
                                deleteFromBD(remove_id);
                                fillLista();
                            }
                        });
                builder.setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void deleteFromBD(int id) {
        fillLista();

        String url = "http://listacontactos.000webhostapp.com/listacontactos/api/contacto" + id;

        JsonObjectRequest delete = new JsonObjectRequest(Request.Method.DELETE, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    boolean status = response.getBoolean("status");
                    if (status == true) {
                        Toast.makeText(MainActivity.this, R.string.removido, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Log.d("login", "" + e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        MySingleton.getInstance(this).addToRequestQueue(delete);
    }



/*
    private void getCursorOrdenaIdade() {
        String sql = "select " + Contrato.Contacto.TABLE_NAME + "." +
                Contrato.Contacto._ID + "," +
                Contrato.Contacto.COLUMN_NOME + "," +
                Contrato.Contacto.COLUMN_APELIDO + "," +
                Contrato.Contacto.COLUMN_NUMERO + "," +
                Contrato.Contacto.COLUMN_EMAIL + "," +
                Contrato.Contacto.COLUMN_ENDERECO + "," +
                Contrato.Contacto.COLUMN_IDADE + " FROM " +
                Contrato.Contacto.TABLE_NAME +
                " WHERE " + Contrato.Contacto.COLUMN_ID_USER + "=" + iduser +
                " ORDER BY " + Contrato.Contacto.COLUMN_IDADE;
        c = db.rawQuery(sql, null);     // toda a info recolhida a partir da query, e posta no cursor c
    }

 */

/*
    private void getCursorPesquisaNome(String nomep) {
        String sql = "select " + Contrato.Contacto.TABLE_NAME + "." +
                Contrato.Contacto._ID + "," +
                Contrato.Contacto.COLUMN_NOME + "," +
                Contrato.Contacto.COLUMN_APELIDO + "," +
                Contrato.Contacto.COLUMN_NUMERO + "," +
                Contrato.Contacto.COLUMN_EMAIL + "," +
                Contrato.Contacto.COLUMN_ENDERECO + "," +
                Contrato.Contacto.COLUMN_IDADE + " FROM " +
                Contrato.Contacto.TABLE_NAME +
                " WHERE " + Contrato.Contacto.COLUMN_ID_USER + "=" + iduser +
                " AND " + Contrato.Contacto.COLUMN_NOME + "='" + nomep + "'";
        c = db.rawQuery(sql, null);     // toda a info recolhida a partir da query, e posta no cursor c
    }

 */
/*
    private void preencheListaPesquisaNome(String nomep){

        getCursorPesquisaNome(nomep);
        madapter = new MyCursorAdapter(MainActivity.this, c);   // a info que esta contida no cursor c (alimentado pela BD) vai ser utilizada para construir o cursor personalizado
        listView.setAdapter(madapter);
    }
*/


    private void preencheListaOrdenaIdade(){
        arraycon.removeAll(arraycon);

        String url = "http://listacontactos.000webhostapp.com/listacontactos/api/contactosporidade";

        JsonArrayRequest jsonArrRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject obj = response.getJSONObject(i);
                        arraycon.add(new Contact(obj.getInt("id"), obj.getString("nome"), obj.getString("apelido"), obj.getInt("numero"), obj.getInt("idade"), obj.getString("email"), obj.getString("endereco"), obj.getString("cidade")));

                        CustomArrayAdapter itemsAdapter = new CustomArrayAdapter(MainActivity.this, arraycon);
                        ((ListView) findViewById(R.id.lista)).setAdapter(itemsAdapter);
                    }
                } catch (JSONException e) {
                    Log.d("fillLista", "" + e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        MySingleton.getInstance(this).addToRequestQueue(jsonArrRequest);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }
}
