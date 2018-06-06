package com.example.android.PracticaNotasITSLP;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BaseDatosHelper extends SQLiteOpenHelper{

    //Constantes para el nombre y la versión de db
    private static final String NombreBaseDatos = "notas.db";
    private static final int VersionBaseDatos = 1;

    //nombre de tabla y columnas
    public static final String NombreTabla = "notes";
    public static final String Id = "_id";
    public static final String texto = "texto";
    public static final String creacion = "creacion";

    //columnas 
    public static final String[] columnas =
            {Id, texto, creacion};

   //se crea la tabla de notas
    private static final String TABLE_CREATE =
            "CREATE TABLE " + NombreTabla + " (" +
                    Id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    texto + " TEXT, " +
                    creacion + " TEXT default CURRENT_TIMESTAMP" +
                    ")";

    public BaseDatosHelper(Context context) {
        super(context, NombreBaseDatos, null, VersionBaseDatos);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + NombreTabla);
        onCreate(db);
    }
}
