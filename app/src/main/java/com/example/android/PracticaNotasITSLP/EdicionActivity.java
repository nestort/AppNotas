package com.example.android.PracticaNotasITSLP;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class EdicionActivity extends AppCompatActivity {

    private String accion;
    private EditText editor;
    private String filtro;
    private String copiaText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar);

        editor = (EditText) findViewById(R.id.editText);

        Intent intent = getIntent();

        Uri uri = intent.getParcelableExtra(NotesProveedor.CONTENT_ITEM_TYPE);

        if (uri == null) {
            accion = Intent.ACTION_INSERT;
            setTitle(getString(R.string.nueva_nota));
        } else {
            accion = Intent.ACTION_EDIT;
            setTitle(getString(R.string.editar_nota));

            filtro = BaseDatosHelper.Id + "=" + uri.getLastPathSegment();

            Cursor cursor = getContentResolver().query(uri,
                    BaseDatosHelper.columnas, filtro, null, null);

            cursor.moveToFirst();
            copiaText = cursor.getString(cursor.getColumnIndex(BaseDatosHelper.texto));
            editor.setText(copiaText);

            editor.requestFocus();
            cursor.close();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //This ensures delete option only appears on existing notes
        if (accion.equals(Intent.ACTION_EDIT)) {
            getMenuInflater().inflate(R.menu.menu_editor, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {

            case android.R.id.home:
                finalizaEdicion();
                break;
            case R.id.action_delete:
                eliminacionNotas();
                break;
        }
        return true;
    }

    private void finalizaEdicion() {
        String nuevaNota = editor.getText().toString().trim();

        switch (accion) {
            case Intent.ACTION_INSERT:
                if (nuevaNota.length() == 0) {
                    setResult(RESULT_CANCELED);
                }
                else {
                    InsertarNota(nuevaNota);
                }
                break;
            case Intent.ACTION_EDIT:
                if (nuevaNota.length() == 0) {
                    eliminacionNotas();
                } else if (copiaText.equals(nuevaNota)) {
                    setResult(RESULT_CANCELED);
                } else {
                    actualizacion(nuevaNota);
                }
        }
        finish();
    }

    private void eliminacionNotas() {

        DialogInterface.OnClickListener dialogClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int button) {
                        if (button == DialogInterface.BUTTON_POSITIVE) {
                            //se elimina
                            getContentResolver().delete(NotesProveedor.CONTENT_URI,
                                    filtro, null);

                            Toast.makeText(EdicionActivity.this, R.string.nota_eliminada, Toast.LENGTH_SHORT).show();
                            setResult(RESULT_OK);
                            finish();
                        }
                    }
                };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.estas_seguro))
                .setPositiveButton(getString(android.R.string.yes), dialogClickListener)
                .setNegativeButton(getString(android.R.string.no), dialogClickListener)
                .show();

    }

    private void actualizacion(String noteText) {
        ContentValues values = new ContentValues();
        values.put(BaseDatosHelper.texto, noteText);

        getContentResolver().update(NotesProveedor.CONTENT_URI, values,
                filtro, null);

        Toast.makeText(this, R.string.nota_actualizada, Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
    }

    private void InsertarNota(String noteText) {
        ContentValues valor = new ContentValues();
        valor.put(BaseDatosHelper.texto, noteText);
        getContentResolver().insert(NotesProveedor.CONTENT_URI, valor);
        setResult(RESULT_OK);
    }

    @Override
    public void onBackPressed() {
        finalizaEdicion();
    }
}
