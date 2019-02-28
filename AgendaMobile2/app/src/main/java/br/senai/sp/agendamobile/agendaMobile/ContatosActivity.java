package br.senai.sp.agendamobile.agendaMobile;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.List;

import br.senai.sp.agendamobile.R;
import br.senai.sp.agendamobile.dao.contatoDao;
import br.senai.sp.agendamobile.modelo.Contato;

public class ContatosActivity extends AppCompatActivity {

    private ListView listaContatos;
    private Button btnNovoContato;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contatos);

        listaContatos = findViewById(R.id.list_contatos);

        btnNovoContato = findViewById(R.id.btn_novo_contato);

        btnNovoContato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent abrirCadastroContato = new Intent(ContatosActivity.this, CadastroActivity.class);
                startActivity(abrirCadastroContato);

            }
        });

        /// definição de menu de contexto

        registerForContextMenu(listaContatos);

        listaContatos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Contato contato = (Contato) listaContatos.getItemAtPosition(position);

                Intent cadastro = new Intent(ContatosActivity.this, CadastroActivity.class);
                cadastro.putExtra("contato", contato);

                startActivity(cadastro);

                Toast.makeText(ContatosActivity.this, String.valueOf(position) + "sucesso", Toast.LENGTH_LONG).show();

            }
        });

    }

    public void  onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_context_contatos, menu);

        super.onCreateContextMenu(menu, v, menuInfo);
    }

    public boolean onContextItemSelected (MenuItem item){
        final contatoDao dao = new contatoDao(this);

        AdapterView.AdapterContextMenuInfo inf = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        final Contato contato = (Contato) listaContatos.getItemAtPosition(inf.position);

        new AlertDialog.Builder(this).setTitle("deletar contato").
                setMessage("tem certeza que quer deletar esse contato" + contato.getNome()+ "?")
                .setPositiveButton("sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dao.excluir(contato);
                        dao.close();
                        carregarLista();
                    }
                }).setNegativeButton("não", null).show();


        return super.onContextItemSelected(item);
    }

    protected void onResume(){
        carregarLista();
        super.onResume();
    }


    private void carregarLista(){
        //*** vetor de contatos//

        contatoDao dao = new contatoDao(this);
        List<Contato> contatos = dao.getContatos();
        dao.close();


        ArrayAdapter<Contato> adapterContatos = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, contatos);

        listaContatos.setAdapter(adapterContatos);

    }
}
