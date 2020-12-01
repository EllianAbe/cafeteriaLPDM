package com.uniso.lpdm.cafeteria_cap7.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.uniso.lpdm.cafeteria_cap7.database.DatabaseHelper;
import com.uniso.lpdm.cafeteria_cap7.R;

public class ComidaActivity extends AppCompatActivity {

    public static final String EXTRA_COMIDA_ID = "comida_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comida);

        int id_comida = (Integer) getIntent().getExtras().get(EXTRA_COMIDA_ID);

        SQLiteOpenHelper databaseHelper = new DatabaseHelper(this);

        try{
            SQLiteDatabase db = databaseHelper.getReadableDatabase();

            /*Precisamos modificar as consulta que fazemos para que no cursor
             * conste a informação se a comida selecionada é favorita ou não.*/
            Cursor cursor = db.query("COMIDA",
                    new String[] {"nome", "descricao"},
                    "_id = ?",
                    new String[] {Integer.toString(id_comida)},
                    null, null, null);

            if(cursor.moveToFirst()){

                String nomeString = cursor.getString(0);
                String descricaoString = cursor.getString(1);

                TextView nome = (TextView) findViewById(R.id.nome);
                nome.setText(nomeString);

                TextView descricao = (TextView) findViewById(R.id.descricao);
                descricao.setText(descricaoString);
            }

            cursor.close();
            db.close();

        }catch (SQLiteException e){

            Toast toast = Toast.makeText(this, "Banco indisponivel", Toast.LENGTH_SHORT);

            Log.d("Erro de banco de dados", e.getMessage());
            toast.show();
        }

    }
}