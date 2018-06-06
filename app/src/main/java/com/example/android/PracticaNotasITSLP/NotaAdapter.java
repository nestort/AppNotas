package com.example.android.PracticaNotasITSLP;


import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

//se extraen los datos de la base de datos

public class NotaAdapter extends CursorAdapter{

    public NotaAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(
                R.layout.lista_notas, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String noteText = cursor.getString(
                cursor.getColumnIndex(BaseDatosHelper.texto));

        //Look for a line feed
        //10 is the ASCII value of a line feed character
        int pos = noteText.indexOf(10);

        // -1 means the character is not present
        if (pos != -1) {
            noteText = noteText.substring(0,pos) + " ...";
        }

        //Cast noteText to TextView component
        TextView tv = (TextView) view.findViewById(R.id.tvNote);
        tv.setText(noteText);
    }
}
