package letstalk.projetos.com.letstalk.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.preference.EditTextPreference;
import android.provider.ContactsContract;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import letstalk.projetos.com.letstalk.R;
import letstalk.projetos.com.letstalk.adapter.TabAdapter;
import letstalk.projetos.com.letstalk.config.ConfiguracaoFirebase;
import letstalk.projetos.com.letstalk.helper.Base64Custom;
import letstalk.projetos.com.letstalk.helper.Preferencias;
import letstalk.projetos.com.letstalk.model.Contato;
import letstalk.projetos.com.letstalk.model.Usuario;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth autenticacao;
    private DatabaseReference firebase;

    private Toolbar toolbar;

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private String identificadorContato;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = (TabLayout) findViewById(R.id.stl_tabs);
        viewPager = (ViewPager) findViewById(R.id.vp_pagina);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        viewPager.setAdapter(new TabAdapter(getSupportFragmentManager(), getResources().getStringArray(R.array.tab_titulos)));
        tabLayout.setupWithViewPager(viewPager);

        toolbar.setTitle("Let's Talk");
        setSupportActionBar(toolbar);

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.item_sair:
                deslogarUsuario();
                return true;
            case R.id.item_adicionar:
                AbrirCadastroContato();
                return true;
            case R.id.item_configuracoes:
                return true;
            case R.id.item_pesquisa:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void deslogarUsuario(){
        autenticacao.signOut();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void AbrirCadastroContato(){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

        //CONFIGURA JANELA DE DIALOGO
        alertDialog.setTitle("Novo Contato");
        alertDialog.setMessage("Email do usuário");
        alertDialog.setCancelable(false);

        final EditText editText = new EditText(MainActivity.this);
        alertDialog.setView(editText);

        //CONFIGURA BOTÕES
        alertDialog.setPositiveButton("Cadastrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String emailContato = editText.getText().toString();

                if(emailContato.isEmpty()){
                    Toast.makeText(MainActivity.this, "Informe um email", Toast.LENGTH_SHORT).show();
                }else{

                    //VERIFICA SE O USUÁRIO ESTA CADASTRADO NO APP
                    identificadorContato = Base64Custom.CodificarBase64(emailContato);

                    firebase = ConfiguracaoFirebase.getFirebase();
                    firebase = firebase.child("usuarios").child(identificadorContato);
                    firebase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if(dataSnapshot.getValue() != null) {
                                //RECUPERA DADOS DO CONTATO A SER ADICIONADO
                                Usuario userContato = dataSnapshot.getValue(Usuario.class);

                                //PEGA OS DADOS DO USUARIO LOGADO NO SISTEMA
                                Preferencias preferencias = new Preferencias(MainActivity.this);
                                String identificadorUserLogado = preferencias.getIdentificador();

                                firebase = ConfiguracaoFirebase.getFirebase();
                                firebase = firebase.child("contatos")
                                        .child(identificadorUserLogado)
                                        .child(identificadorContato);

                                //INSERE CONTATO
                                Contato contato = new Contato();
                                contato.setIdentificadorUsuario(identificadorContato);
                                contato.setEmail(userContato.getEmail());
                                contato.setNome(userContato.getNome());

                                firebase.setValue(contato);
                            }else{
                                Toast.makeText(MainActivity.this, "O usuário não possui cadastro", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }


            }
        });

        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog.create();
        alertDialog.show();


    }

}























