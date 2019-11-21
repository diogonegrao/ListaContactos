package com.example.listacontactos.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DB extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 9;
    public static final String DATABASE_NAME = "contactos.db";

    public DB(Context context) { super(context, DATABASE_NAME, null, DATABASE_VERSION); }

    // CRIACAO DAS DB's
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Contrato.User.SQL_CREATE_ENTRIES);       // cria a BD de users
        db.execSQL(Contrato.Contacto.SQL_CREATE_ENTRIES);   // cria a BD de contactos

        //db.execSQL("insert into " + Contrato.User.TABLE_NAME + " values (1, 'admin', 'admin');");
        //db.execSQL("insert into " + Contrato.Contacto.TABLE_NAME + " values (1, 'Diogo', 'Negrão', 914559072, 'diogo@teste.pt', 'Rua de teste', 21, 1);");
    }

    // PARA EPOCA DE DESENVOLVIMENTO, ESTE METODO PERMITE ATUALIZAR AS TABELAS, APAGANDO-AS E VOLTANDO A CRIA-LAS
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(Contrato.Contacto.SQL_DROP_ENTRIES);
        db.execSQL(Contrato.User.SQL_DROP_ENTRIES);
        onCreate(db);
    }

    // PERMITE REGRESSAR A UMA VERSAO ANTERIOR
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
