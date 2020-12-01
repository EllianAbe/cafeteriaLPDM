package com.uniso.lpdm.cafeteria_cap7;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.uniso.lpdm.cafeteria_cap7.model.Comida;

public class ComidaActivity extends AppCompatActivity {

    public static final String EXTRA_COMIDA_ID = "comida_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comida);

        int id_comida = (Integer) getIntent().getExtras().get(EXTRA_COMIDA_ID);
        Comida comida = Comida.comidas[id_comida];

        TextView nome = (TextView) findViewById(R.id.nome);
        nome.setText(comida.getNome());

        TextView descricao = (TextView) findViewById(R.id.descricao);
        descricao.setText(comida.getDescricao());

    }
}