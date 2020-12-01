package com.uniso.lpdm.cafeteria_cap7;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

/*Para ler os comentários desse código, o ideal é começar pela BebidaActivity e seu layout e depois a
* MainActivity e seu layout */
public class MainActivity extends AppCompatActivity {
    /*Declaramos as variavies para armazenar o cursor e o banco de dados para leitura aqui
     * porque eles vão precisar ficar abertos por quanto tempo a aplicação ATIVIDADE existir. Isso
     * porque pode se ter muitos dados obtidos no cursor, e se isso ocorre ele não recupera todos os
     * dados de uma só vez, mas sim em partes conforme o usuário solicitar. Então só vamos poder
     * fechar essas conexões quando a atividade for destruída, lembrando do ciclo de vida da atividade */
    private SQLiteDatabase db;
    private Cursor favoritasCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> adapterView, //Este é o view clicado, no caso, o list view
                                    View view, // O view que foi clicado, o elemento clicado
                                    int posicao, // a posicao no listview, começando em 0
                                    long id) { // o id da linha

                if(posicao == 0){
                    Intent intent = new Intent(MainActivity.this, CategoriaBebidaActivity.class);
                    startActivity(intent);
                }
            }
        };

        ListView listview = (ListView) findViewById(R.id.lista_opcoes);
        listview.setOnItemClickListener(itemClickListener);

        /*Precisamos chamar o método que verifica as bebidas favoritas no onCreate da aplicação*/
        setupFavoritesListView();
    }

    /*Para que o código executado no onCreate fique mais simples de ler, vamos criar um método/função
    * separado para tratar do nosso novo ListView. Observe que a mesma abordagem poderia ser feita
    * para o códido que já se encontra no onCreate para o ListView original*/
    private void setupFavoritesListView(){
        /*Recuperamos o novo ListView para exibir as bebidas favoritas*/
        ListView listaFavoritas = (ListView) findViewById(R.id.lista_favoritas);

        /*Como vamos trabalhar com Banco de Dados, estamos sujeitos a problemas e como vimos
        * precisamos utilizar a estrutura try catch para trata-los. */
        try{
            /*Obtemos a referencia para o nosso Helper*/
            SQLiteOpenHelper databaseHelper = new DatabaseHelper(this);
            /*Como vimos, ao trabalhar com adaptadores que utilizam cursores, só podemos
            * encerrar o acesso ao cursor e ao banco de dados após a atividade ter sido destruida.
            * Por isso criamos as variaveis para armazenar a referencia ao banco e ao cursor la em cima
            * para podemos fecha-los apenas quando a atividade for destruída.
            *
            * Então pegamos a referencia para o banco de dados e criamos o cursor, usando
            * uma consulta onde pegamos apenas as bebidas que tenham o campo favorita com o valor
            * 1, pois como já vimos, será o equivalente a verdadeiro do booleano*/
            db = databaseHelper.getReadableDatabase();
            favoritasCursor = db.query(
                    "BEBIDA", // Selecionamos a tabela
                    // Selecionamos os campos que precisamos, lembrando de sempre listar o _id para
                    // trabalhar com adaptador
                    new String[] {"_id", "nome"},
                    /* Aqui a nossa condição de seleção é fixa, queremos que o campo favorita seja 1,
                    ou seja, verdadeiro. Sendo assim, não precisamos de uma lista de valores que serão
                    subistituidos na consulta, como fizemos na BebidaActivity*/
                    "favorita = 1",
                    /*Nenhum pamatro mais é necessário. Se quisermos poderíamos ordenar
                    * as bebidas favoritas em ordem alfabetica, por exemplo, mas seria
                    * opcional*/
                    null, null, null, null
            );

            /*Aqui fazemos a configuração do SimpleCursorAdapter, como vimos na aula passada
            * passando como parametro os layouts dos TextViews que serão uitilizados para
            * exibir as informações, o cursor com a fonte de dados, os campos que serão exibidos
            * e as flags, que não precisamos, mas colocamos para não usar uma versão depreciada
            * do construtor*/
            CursorAdapter favoritasAdapter = new SimpleCursorAdapter(
                    MainActivity.this,
                    android.R.layout.simple_list_item_1,
                    favoritasCursor,
                    new String[] {"nome"},
                    new int[] {android.R.id.text1},
                    0
            );

            /*Como vimos, depois de criar o cursor e o adaptador, precisamos associar ele com
            * o ListView*/
            listaFavoritas.setAdapter(favoritasAdapter);

        }catch(SQLiteException e){
            Toast toast = Toast.makeText(this, "Banco indisponivel", Toast.LENGTH_SHORT);

            Log.d("Erro de banco de dados", e.getMessage());
            toast.show();
        }

        /*Aqui temos o tratamento de eventos como conversado na aula sobre Listeners,
        * que é igual ao que fazemos na CategoriaBebidaActivity*/
        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int posicao, long id) {
                Intent intent = new Intent(MainActivity.this, BebidaActivity.class);
                intent.putExtra(BebidaActivity.EXTRA_BEBIDA_ID, (int) id);
                startActivity(intent);
            }
        };

        listaFavoritas.setOnItemClickListener(itemClickListener);
    }

    /*Os cursores apenas recuperarm dados quando são criados. Eles não controlam as eventuais alterações
    * nos dados subjacentes contidos no banco de dados. Para podermos corrigir isso e o usuário não
    * precisar reiniciar a aplicação para que a lista de bebidas favoritas seja atualizada, vamos colocar
    * no método/função onRestart() um código que permita pegar as possíveis modificações que os dados
    * sofreram (como as bebidas sendo "favoritadas"). Os cursores recuperam dados apenas quando são criados,
    * por isso precisamos contornar isso para pegarmos dados modificados durante o uso da aplicação.*/
    @Override
    public void onRestart() {
        super.onRestart();
        /*Como a referencia ao banco é recuperada quando a atividade é criada (está no método onCreate)
        * não precisamos fazer isso novamente. Criamos um novo cursor para recuperar novamente os dados
        * e atualizar possíveis modificações no banco.
        * O novo cursor é definido igual ao original no onCreate, mas agora ele irá pegar os dados
        * com as possíveis atualizações.*/
        Cursor novoCursor = db.query(
                "BEBIDA",
                new String[] {"_id", "nome"},
                "favorita = 1",
                null, null, null, null
        );

        /*Precisamos recuperar o ListView para poder atualizar o cursor associado ao adaptador*/
        ListView listaFavoritas = (ListView) findViewById(R.id.lista_favoritas);
        /*Esse ListView já possui um adaptador associado a ele, que foi criado no onCreate.
        * Não precisamos criar um novo adaptador, apenas atualizar o cursor desse adaptador para
        * que as possíveis modificações dos dados sejam recuperadas. Para isso, usamos o método/função
        * getAdapter(), que retorna o adaptador que atualmente está associado ao ListView. Como
        * esse método/função retorna um adaptador mais genérico (polimorfismo), precisamos
        * converte-lo de volta para um CursorAdapter para podermos utilizar os métodos/funções
        * relacionados com cursores*/
        CursorAdapter adapter = (CursorAdapter) listaFavoritas.getAdapter();
        /*O método/função changeCursor troca o cursor atual do CursorAdapter por um novo cursor.
        * Em seguida, esse método fecha o cursor anterior automaticamente.*/
        adapter.changeCursor(novoCursor);
        /*Precisamos atualizar a referencia do cursor que foi criado no onCreate e já foi fechado,
        * para que possamos fechar esse novo cursor ao destrutuir a atividade*/
        favoritasCursor = novoCursor;

    }

    /*Como não podemos fechar o cursor e o banco até que a atividade seja destruída, fazemos isso
     * onDestroy*/
    @Override
    public void onDestroy(){
        super.onDestroy();
        favoritasCursor.close();
        db.close();
    }


}