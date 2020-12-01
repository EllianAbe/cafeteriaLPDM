package com.uniso.lpdm.cafeteria_cap7;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.uniso.lpdm.cafeteria_cap7.model.Comida;

public class CategoriaComidaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoria_comida);

        ArrayAdapter<Comida> comidaArrayAdapter = new ArrayAdapter<>(
                this, //contexto
                android.R.layout.simple_list_item_1, // simple_list_item_1 é um recurso de layout definido pela plataforma para itens em uma ListView. Você pode usá-lo em vez de criar o próprio layout para itens de lista.
                Comida.comidas //Array que será a fonte de dados
        );

        ListView listView = findViewById(R.id.lista_comidas);
        listView.setAdapter(comidaArrayAdapter);

        /*Após criarmos o adaptador, temos os itens do menu preenchidos. Para isso precisamos
         * novamente utilizar um Ouvinte, que vai funcionar de maneira muito semelhante ao da
         * MainActivity*/
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
}