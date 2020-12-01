package com.uniso.lpdm.cafeteria_cap7.activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.uniso.lpdm.cafeteria_cap7.R;
import com.uniso.lpdm.cafeteria_cap7.database.DatabaseHelper;

public class CategoriaComidaActivity extends AppCompatActivity {

    private SQLiteDatabase db;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoria_comida);

        ListView listView = findViewById(R.id.lista_comidas);

        SQLiteOpenHelper databaseHelper = new DatabaseHelper(this);

        try {
            db = databaseHelper.getReadableDatabase();
            cursor = db.query(
                    "comida",
                    new String[] {"_id", "nome"},
                    null, null, null, null, "nome ASC"
            );


            SimpleCursorAdapter listAdapter = new SimpleCursorAdapter(this,
                    android.R.layout.simple_list_item_1,
                    cursor,
                    new String[] {"nome"},
                    new int[] {android.R.id.text1},
                    0
            );

            listView.setAdapter(listAdapter);
        }catch(SQLiteException e){

            Toast toast = Toast.makeText(this, "Banco indisponivel", Toast.LENGTH_SHORT);
            Log.d("Erro de banco de dados", e.getMessage());
            toast.show();
        }


        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int posicao, long id) {
                Intent intent = new Intent(CategoriaComidaActivity.this, ComidaActivity.class);
                intent.putExtra(ComidaActivity.EXTRA_COMIDA_ID, (int) id);
                startActivity(intent);
            }
        };

        listView.setOnItemClickListener(itemClickListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cursor.close();
        db.close();
    }
}