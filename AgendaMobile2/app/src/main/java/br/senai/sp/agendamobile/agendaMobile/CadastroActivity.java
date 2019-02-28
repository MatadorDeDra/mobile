package br.senai.sp.agendamobile.agendaMobile;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import br.senai.sp.agendamobile.R;
import br.senai.sp.agendamobile.agendaMobile.ContatosHelper;
import br.senai.sp.agendamobile.dao.contatoDao;
import br.senai.sp.agendamobile.modelo.Contato;

public class CadastroActivity extends AppCompatActivity {

    private ContatosHelper helper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //criando a tela
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        //contato helper, responsavel por pegar todos os dados digitados
        helper = new ContatosHelper(this);

        //intent é um metodo interno que tem a função de fazer transição entre telas
        Intent intencao = getIntent();
        Contato contato = (Contato) intencao.getSerializableExtra("contato");

        //se o contato não existir ele ira preencher o formulario com os dados puchados do banco.
        if(contato != null){
            helper.PreencherFormulario(contato);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        // MenuInflater, metodo que permite criar o menu na tela
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_cadastro_contatos, menu);

        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item){

            switch (item.getItemId()){
                case R.id.menu_salvar:

                    Contato contato = helper.getContato();

                     contatoDao dao = new contatoDao(this);

                 if(contato.getId() !=0){
                     dao.atualizar(contato);
                     Toast.makeText(CadastroActivity.this, contato.getNome() + "contato atualizado", Toast.LENGTH_LONG).show();
                 }else{
                      dao.salvar(contato);
                      Toast.makeText(CadastroActivity.this, contato.getNome() + "contato salvo", Toast.LENGTH_LONG).show();
                 }



                 dao.close();
                 finish();

                 break;
            }


        switch(item.getItemId()){
            case R.id.menu_deletar:

                final Contato contato = helper.getContato();

                final contatoDao dao = new contatoDao(this);

                new AlertDialog.Builder(this).setTitle("deletar contato").
                        setMessage("tem certeza que quer deletar esse contato" + contato.getNome()+ "?")
                        .setPositiveButton("sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                dao.excluir(contato);
                                dao.close();
                                finish();
                            }
                        }).setNegativeButton("não", null).show();

        }
        return super.onOptionsItemSelected(item);
    }
}
