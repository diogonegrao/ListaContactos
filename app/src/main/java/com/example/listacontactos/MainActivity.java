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
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.example.listacontactos.adapter.MyCursorAdapter;
import com.example.listacontactos.db.Contrato;
import com.example.listacontactos.db.DB;

import com.example.listacontactos.Utils.Utils;

import java.io.Serializable;


public class MainActivity extends AppCompatActivity implements Serializable {

    private  int REQUEST_CODE_OP = 1;
    private  int REQUEST_CODE_OP_2 = 2;

    DB mDbHelper;
    SQLiteDatabase db;
    ListView listView;
    Cursor c;
    MyCursorAdapter madapter;
    int iduser;
    EditText pesquisanome;
    SharedPreferences sharedPreferences;
    String user_name;
    Intent login;
    int id_user;


    // CRIAR O METODO ONCREATE - metodo que apenas e utilizado ao lançar a atividade
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);     // 1ª atividade lançada pela aplicacao

        iduser = getIntent().getIntExtra("ID", -1);

        mDbHelper = new DB(this);       // permite a ligacao a BD criada
        db = mDbHelper.getReadableDatabase();   // permite que a BD seja aberta para leitura

        listView = (ListView)findViewById(R.id.lista);  // representa a lista declarada no layout

        registerForContextMenu(findViewById(R.id.lista));
        fillLista();      // metodo para preencher a lista


        sharedPreferences = getSharedPreferences("USER_CREDENTIALS",MODE_PRIVATE);
        user_name = sharedPreferences.getString("NAME","DEFAULT_NAME");
        id_user = sharedPreferences.getInt("IDUSER",-1);

        // com um click abre a atividade onde e possivel ver todos os detalhes do contacto
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(MainActivity.this, ViewActivity.class);
                c.moveToPosition(i);    // move o cursor para o index da linha selecionada de maneira a ter informacao pretendida
                intent.putExtra(Utils.PARAM_NOME, c.getString(c.getColumnIndex(Contrato.Contacto.COLUMN_NOME)));
                intent.putExtra(Utils.PARAM_APELIDO, c.getString(c.getColumnIndex(Contrato.Contacto.COLUMN_APELIDO)));
                intent.putExtra(Utils.PARAM_NUMERO, c.getInt(c.getColumnIndex(Contrato.Contacto.COLUMN_NUMERO)));
                intent.putExtra(Utils.PARAM_EMAIL, c.getString(c.getColumnIndex(Contrato.Contacto.COLUMN_EMAIL)));
                intent.putExtra(Utils.PARAM_ENDERECO, c.getString(c.getColumnIndex(Contrato.Contacto.COLUMN_ENDERECO)));
                intent.putExtra(Utils.PARAM_IDADE, c.getInt(c.getColumnIndex(Contrato.Contacto.COLUMN_IDADE)));
                // utiliza o valor que esta no cursor para atribuir as variaveis do UTILS os dados correspondentes

                startActivity(intent);
            }
        });
    }

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


    private void fillLista(){
        getCursor();
        madapter = new MyCursorAdapter(MainActivity.this, c);   // a info que esta contida no cursor c (alimentado pela BD) vai ser utilizada para construir o cursor personalizado
        listView.setAdapter(madapter);
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
        Context mContext = this;
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.adicionar:
                Intent i = new Intent(MainActivity.this, AddActivity.class);
                startActivityForResult(i, REQUEST_CODE_OP);
                break;

            case R.id.logout:
                login=new Intent(this,LoginActivity.class);
                final SharedPreferences sharedPreferences=getSharedPreferences("USER_CREDENTIALS",MODE_PRIVATE);
                sharedPreferences.edit().putBoolean("ISLOGGEDIN",false).commit();
                startActivity(login);
                finish();
                break;

            case R.id.sobre:
                Intent j = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(j);
                break;

                // pesquisa por nome
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
                //arrayCon.add(new Contact(data.getStringExtra(Utils.PARAM_NOME), data.getStringExtra(Utils.PARAM_APELIDO), data.getIntExtra(Utils.PARAM_NUMERO, 0), data.getIntExtra(Utils.PARAM_IDADE,0), data.getStringExtra(Utils.PARAM_EMAIL), data.getStringExtra(Utils.PARAM_ENDERECO)));
                ContentValues cv = new ContentValues();
                cv.put(Contrato.Contacto.COLUMN_NOME, data.getStringExtra(Utils.PARAM_NOME));
                cv.put(Contrato.Contacto.COLUMN_APELIDO, data.getStringExtra(Utils.PARAM_APELIDO));
                cv.put(Contrato.Contacto.COLUMN_NUMERO, data.getIntExtra(Utils.PARAM_NUMERO, -1));
                cv.put(Contrato.Contacto.COLUMN_EMAIL, data.getStringExtra(Utils.PARAM_EMAIL));
                cv.put(Contrato.Contacto.COLUMN_ENDERECO, data.getStringExtra(Utils.PARAM_ENDERECO));
                cv.put(Contrato.Contacto.COLUMN_IDADE, data.getIntExtra(Utils.PARAM_IDADE, -1));
                cv.put(Contrato.Contacto.COLUMN_ID_USER, iduser);
                db.insert(Contrato.Contacto.TABLE_NAME, null, cv);
                fillLista();
            }
        }

        if (requestCode == REQUEST_CODE_OP_2) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                //int i = data.getIntExtra(Utils.PARAM_INDEX, 0);
                //arrayCon.set(i,new Contact(data.getStringExtra(Utils.PARAM_NOME), data.getStringExtra(Utils.PARAM_APELIDO), data.getIntExtra(Utils.PARAM_NUMERO, 0), data.getIntExtra(Utils.PARAM_IDADE,0), data.getStringExtra(Utils.PARAM_EMAIL), data.getStringExtra(Utils.PARAM_ENDERECO)));

                ContentValues cv = new ContentValues();
                cv.put(Contrato.Contacto.COLUMN_NOME, data.getStringExtra(Utils.PARAM_NOME));
                cv.put(Contrato.Contacto.COLUMN_APELIDO, data.getStringExtra(Utils.PARAM_APELIDO));
                cv.put(Contrato.Contacto.COLUMN_NUMERO, data.getIntExtra(Utils.PARAM_NUMERO, -1));
                cv.put(Contrato.Contacto.COLUMN_EMAIL, data.getStringExtra(Utils.PARAM_EMAIL));
                cv.put(Contrato.Contacto.COLUMN_ENDERECO, data.getStringExtra(Utils.PARAM_ENDERECO));
                cv.put(Contrato.Contacto.COLUMN_IDADE, data.getIntExtra(Utils.PARAM_IDADE, -1));
                cv.put(Contrato.Contacto.COLUMN_ID_USER, iduser);
                int id = data.getIntExtra(Utils.PARAM_INDEX, -1);
                db.update(Contrato.Contacto.TABLE_NAME, cv, Contrato.Contacto._ID + " = ?", new String[]{id+""});
                fillLista();
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
        c.moveToPosition(itemPosition);     // permite ir ao cursor correspondente da linha selecionada e recolher toda a informacao que contem

        switch (item.getItemId()) {
            case R.id.editar:
                Intent intent = new Intent(MainActivity.this, EditActivity.class);

                int id_contacto = c.getInt(c.getColumnIndex(Contrato.Contacto._ID));
                intent.putExtra(Utils.PARAM_NOME, c.getString(c.getColumnIndex(Contrato.Contacto.COLUMN_NOME)));
                intent.putExtra(Utils.PARAM_APELIDO, c.getString(c.getColumnIndex(Contrato.Contacto.COLUMN_APELIDO)));
                intent.putExtra(Utils.PARAM_NUMERO, c.getInt(c.getColumnIndex(Contrato.Contacto.COLUMN_NUMERO)));
                intent.putExtra(Utils.PARAM_EMAIL, c.getString(c.getColumnIndex(Contrato.Contacto.COLUMN_EMAIL)));
                intent.putExtra(Utils.PARAM_ENDERECO, c.getString(c.getColumnIndex(Contrato.Contacto.COLUMN_ENDERECO)));
                intent.putExtra(Utils.PARAM_IDADE, c.getInt(c.getColumnIndex(Contrato.Contacto.COLUMN_IDADE)));
                intent.putExtra(Utils.PARAM_INDEX, id_contacto);
                // utiliza o valor que esta no cursor para atribuir as variaveis do UTILS os dados correspondentes (para estes estarem visiveis na edicao)

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
                                int id_contacto = c.getInt(c.getColumnIndex(Contrato.Contacto._ID));    // recolher o ID do contacto (que estava no cursor)
                                deleteFromBD(id_contacto);  // remover
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
        db.delete(Contrato.Contacto.TABLE_NAME, Contrato.Contacto._ID + " = ?", new String[]{id+""});
        fillLista();
    }




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

    private void preencheListaPesquisaNome(String nomep){

        getCursorPesquisaNome(nomep);
        madapter = new MyCursorAdapter(MainActivity.this, c);   // a info que esta contida no cursor c (alimentado pela BD) vai ser utilizada para construir o cursor personalizado
        listView.setAdapter(madapter);
    }

    private void preencheListaOrdenaIdade(){

        getCursorOrdenaIdade();
        madapter = new MyCursorAdapter(MainActivity.this, c);   // a info que esta contida no cursor c (alimentado pela BD) vai ser utilizada para construir o cursor personalizado
        listView.setAdapter(madapter);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();

        if(!c.isClosed()) {     // se o cursor estiver aberto
            c.close();          // fecha o cursor
            c = null;           // e poem-no a nulo
        }
        if(db.isOpen()){        // se a BD estiver aberta
            db.close();         // fecha a BD
            db = null;          // e poem-na a nulo
        }
    }
}
