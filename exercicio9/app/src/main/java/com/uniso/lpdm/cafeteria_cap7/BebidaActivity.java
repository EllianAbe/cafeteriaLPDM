package com.uniso.lpdm.cafeteria_cap7;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class BebidaActivity extends AppCompatActivity {

    public static final String EXTRA_BEBIDA_ID = "bebida_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bebida);

        int id_bebida = (Integer) getIntent().getExtras().get(EXTRA_BEBIDA_ID);

        SQLiteOpenHelper databaseHelper = new DatabaseHelper(this);

        try{
            SQLiteDatabase db = databaseHelper.getReadableDatabase();

            /*Precisamos modificar as consulta que fazemos para que no cursor
            * conste a informação se a bebida selecionada é favorita ou não.*/
            Cursor cursor = db.query("BEBIDA",
                    new String[] {"nome", "descricao", "imagem_resource_id, favorita"},
                    "_id = ?",
                    new String[] {Integer.toString(id_bebida)},
                    null, null, null);

            if(cursor.moveToFirst()){

                String nomeString = cursor.getString(0);
                String descricaoString = cursor.getString(1);
                int fotoId = cursor.getInt(2);
                /*recuperamos do cursor a informação se a bebida é favorita ou não. Como o SQLite
                * não tem a figura do tipo de dado booleano, criamos esse campo como numeric.
                * Entretanto, o checkbox usa um valor booleano para saber se ele está "marcado" ou não,
                * então fazemos um condicional para "converter" esse vamor numerico em um boleano. */
                boolean isFavorita = (cursor.getInt(3) == 1);

                TextView nome = (TextView) findViewById(R.id.nome);
                nome.setText(nomeString);

                TextView descricao = (TextView) findViewById(R.id.descricao);
                descricao.setText(descricaoString);

                ImageView foto = (ImageView) findViewById(R.id.foto);
                foto.setImageResource(fotoId);
                foto.setContentDescription(nomeString);

                /*Como recuperamos lá em cima se a bebida é favorita ou não, precisamos agora
                * atribuir esswe valor para exibir no checkbox. A propriedade que "marca" ou não
                * o checkbox se chama Checked*/
                CheckBox favorita = (CheckBox) findViewById(R.id.favorita);
                favorita.setChecked(isFavorita);
            }

            cursor.close();
            db.close();
            
        }catch (SQLiteException e){

            Toast toast = Toast.makeText(this, "Banco indisponivel", Toast.LENGTH_SHORT);

            Log.d("Erro de banco de dados", e.getMessage());
            toast.show();
        }

    }

    public void onFavoritaClicked(View view){

        /*Recuperamos diretamente da intenção o ID da bebida que iremos atualizar a situação de
        * bebida favorita ou não*/
        int id_bebida = (Integer) getIntent().getExtras().get(EXTRA_BEBIDA_ID);
        /*Recuperamos o checobox atribuímos o o valor da propriedade checked como o
        * valor que será atualizado no banco de dados. Usamos o objeto ContentValues
        * para associarmos o valor com o campo "favorita" da tabela de bebidas.
        * Como estamos usando o método put() do ContentValues, não precisamos fazer a conversão
        * do booleano para o valor numérico que deverá ser inserido no banco para representar
        * um booleano*/
        CheckBox favorita = (CheckBox) findViewById(R.id.favorita);
        ContentValues bebidaValues = new ContentValues();
        bebidaValues.put("favorita", favorita.isChecked());

        /*Criamos o objeto para manipular o banco de dados.*/
        SQLiteOpenHelper databaseHelper = new DatabaseHelper(this);
        try{

            /*Diferente do que fizemos anteriormente, aqui recueparmos uma instancia editavel do
            * banco de dados. Isso porque iremos atualizar um registro, ou seja, vamos escrever no banco*/
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            /*Usamos o método update para atualizar os registros. Informamos a tabela, os dados
            * que serão atualizados, que é o objeto ContentValues, informamos qual será o campo que
            * filtrará quais registros serão atualiados e passamos o valor que esses campos deverão
            * receber.*/
            db.update(
                    "BEBIDA",
                    bebidaValues,
                    "_id = ?",
                    new String[]{Integer.toString(id_bebida)}
                    );

            /*Como de costume, fechamos a comunicação com o banco após utiliza-lo*/
            db.close();


        }catch(SQLiteException e){
            Toast toast = Toast.makeText(this, "Banco indisponivel", Toast.LENGTH_SHORT);

            Log.d("Erro de banco de dados", e.getMessage());
            toast.show();
        }

    }
}