package com.uniso.lpdm.cafeteria_cap7.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.uniso.lpdm.cafeteria_cap7.R;
import com.uniso.lpdm.cafeteria_cap7.model.Localidade;

public class LocalidadeActivity extends AppCompatActivity {

    public static final String EXTRA_LOCALIDADE_ID = "localidade_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localidade);

        int id_localidade = (Integer) getIntent().getExtras().get(EXTRA_LOCALIDADE_ID);
        Localidade localidade = Localidade.localidades[id_localidade];

        TextView nome = (TextView) findViewById(R.id.nome);
        nome.setText(localidade.getNome());

        TextView descricao = (TextView) findViewById(R.id.endereco);
        descricao.setText(localidade.getEndereco());

    }
}