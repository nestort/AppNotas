package com.example.android.PracticaNotasITSLP;


import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class NotesProveedor extends ContentProvider{

    private static final String AUTHORITY = "com.example.plainolnotes.notesprovider";
    private static final String BASE_PATH = "notas";
    public static final Uri CONTENT_URI =
            Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH );

    public static final String CONTENT_ITEM_TYPE = "Nota";

    // Constant to identify the requested operation
    private static final int NOTES = 1;
    private static final int nota_id = 2;

    private static final UriMatcher uriMatcher =
            new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(AUTHORITY, BASE_PATH, NOTES);
        // /# significa cualquier valor numérico, esto nos ayudará a encontrar una nota particular
        uriMatcher.addURI(AUTHORITY, BASE_PATH + "/#", nota_id);
    }

    private SQLiteDatabase database;
    @Override
    public boolean onCreate() {
        BaseDatosHelper helper = new BaseDatosHelper(getContext());
        database = helper.getWritableDatabase();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        //se obtiene nota
        if (uriMatcher.match(uri) == nota_id) {
            selection = BaseDatosHelper.Id + "=" + uri.getLastPathSegment();
        }

        //parametros
        return database.query(BaseDatosHelper.NombreTabla, BaseDatosHelper.columnas,
                selection, null, null, null, BaseDatosHelper.creacion + " DESC");
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        long id = database.insert(BaseDatosHelper.NombreTabla, null, values);
        return Uri.parse(BASE_PATH + "/" + id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return database.delete(BaseDatosHelper.NombreTabla, selection, selectionArgs);
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return database.update(BaseDatosHelper.NombreTabla,
                values, selection, selectionArgs);
    }
}
