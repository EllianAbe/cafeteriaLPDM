package com.uniso.lpdm.cafeteria_cap7;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class CategoriaLocalidadeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoria_localidade);

        ArrayAdapter<Localidade> localidadeArrayAdapter = new ArrayAdapter<>(
                this, //contexto
                android.R.layout.simple_list_item_1, // simple_list_item_1 é um recurso de layout definido pela plataforma para itens em uma ListView. Você pode usá-lo em vez de criar o próprio layout para itens de lista.
                Localidade.localidades //Array que será a fonte de dados
        );

        ListView listView = findViewById(R.id.lista_localidades);
        listView.setAdapter(localidadeArrayAdapter);

        /*Após criarmos o adaptador, temos os itens do menu preenchidos. Para isso precisamos
         * novamente utilizar um Ouvinte, que vai funcionar de maneira muito semelhante ao da
         * MainActivity*/
        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int posicao, long id) {
                Intent intent = new Intent(CategoriaLocalidadeActivity.this, LocalidadeActivity.class);
                intent.putExtra(LocalidadeActivity.EXTRA_LOCALIDADE_ID, (int) id);
                startActivity(intent);
            }
        };

        listView.setOnItemClickListener(itemClickListener);
    }
}