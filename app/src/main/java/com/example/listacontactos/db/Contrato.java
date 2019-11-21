package com.example.listacontactos.db;

import android.provider.BaseColumns;

public class Contrato {
    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INTEGER";

    public Contrato() {
    }

    public static abstract class Contacto implements BaseColumns {
        public static final String TABLE_NAME = "contacto";
        public static final String COLUMN_NOME = "nome";
        public static final String COLUMN_APELIDO = "apelido";
        public static final String COLUMN_NUMERO = "numero";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_ENDERECO = "endereco";
        public static final String COLUMN_IDADE = "idade";
        public static final String COLUMN_ID_USER = "iduser";

        public static final String[] PROJECTION = {Contacto._ID, Contacto.COLUMN_NOME, Contacto.COLUMN_APELIDO, Contacto.COLUMN_NUMERO, Contacto.COLUMN_EMAIL, Contacto.COLUMN_ENDERECO, Contacto.COLUMN_IDADE, Contacto.COLUMN_ID_USER};

        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + Contacto.TABLE_NAME + "(" +
                        Contacto._ID + INT_TYPE + " PRIMARY KEY," +
                        Contacto.COLUMN_NOME + TEXT_TYPE + "," +
                        Contacto.COLUMN_APELIDO + TEXT_TYPE + "," +
                        Contacto.COLUMN_NUMERO + INT_TYPE + "," +
                        Contacto.COLUMN_EMAIL + TEXT_TYPE + "," +
                        Contacto.COLUMN_ENDERECO + TEXT_TYPE + "," +
                        Contacto.COLUMN_IDADE + INT_TYPE + "," +
                        Contacto.COLUMN_ID_USER + INT_TYPE + " REFERENCES " +
                        User.TABLE_NAME + "(" + User._ID + "));";

        public static final String SQL_DROP_ENTRIES =
                "DROP TABLE " + Contacto.TABLE_NAME + ";";
    }

    public static abstract class User implements BaseColumns {
        public static final String TABLE_NAME = "user";
        public static final String COLUMN_USERNAME = "username";
        public static final String COLUMN_PASSWORD = "password";

        public static final String[] PROJECTION = {User._ID, User.COLUMN_USERNAME, User.COLUMN_PASSWORD};

        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + User.TABLE_NAME + "(" +
                        User._ID + INT_TYPE + " PRIMARY KEY," +
                        User.COLUMN_USERNAME + TEXT_TYPE + "," +
                        User.COLUMN_PASSWORD + TEXT_TYPE + ");";

        public static final String SQL_DROP_ENTRIES =
                "DROP TABLE " + User.TABLE_NAME + ";";
    }

}
