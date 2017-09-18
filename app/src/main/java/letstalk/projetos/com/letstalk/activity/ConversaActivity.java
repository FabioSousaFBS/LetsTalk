package letstalk.projetos.com.letstalk.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import letstalk.projetos.com.letstalk.R;
import letstalk.projetos.com.letstalk.adapter.MensagemAdapter;
import letstalk.projetos.com.letstalk.config.ConfiguracaoFirebase;
import letstalk.projetos.com.letstalk.helper.Base64Custom;
import letstalk.projetos.com.letstalk.helper.Preferencias;
import letstalk.projetos.com.letstalk.model.Conversa;
import letstalk.projetos.com.letstalk.model.Mensagem;

import android.support.v7.widget.*;
import android.view.View;
import android.widget.*;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ConversaActivity extends AppCompatActivity {

    private EditText edtMensagemn;
    private ImageView btnMensagem;
    private Toolbar toolbar;

    private ListView listView;
    private ArrayList<Mensagem> mensagens;
    private ArrayAdapter<Mensagem> adapter;

    private DatabaseReference firebase;
    private ValueEventListener valueEventListener;

    //DADOS USER DESTINATARIO
    private String nomeUserDestinatario;
    private String idUserDestinatario;

    //DADOS REMETENTE
    private String nomeUserRemetente;
    private String idUserRemetente;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversa);

        toolbar = (Toolbar) findViewById(R.id.tb_conversa);
        edtMensagemn = (EditText) findViewById(R.id.edtMensagem);
        btnMensagem = (ImageButton) findViewById(R.id.btnEnviarMensagem);
        listView = (ListView) findViewById(R.id.lv_conversas);

        //dados usuario logado
        final Preferencias preferencias = new Preferencias(ConversaActivity.this);
        idUserRemetente = preferencias.getIdentificador();
        nomeUserRemetente = preferencias.getNomeUsuario();

        Bundle extra = getIntent().getExtras();

        if(extra != null){
            nomeUserDestinatario = extra.getString("nome");
            idUserDestinatario = Base64Custom.CodificarBase64(extra.getString("email"));
        }

        //CONFIGURA TOOLBAR
        toolbar.setTitle(nomeUserDestinatario);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(toolbar);

        //CONFIGURA LISTVIEW
        mensagens = new ArrayList<>();
        /*adapter = new ArrayAdapter(ConversaActivity.this,
                android.R.layout.simple_list_item_1,
                mensagens);
        listView.setAdapter(adapter);
        */

        adapter = new MensagemAdapter(ConversaActivity.this,  mensagens);
        listView.setAdapter(adapter);


        //RECUPERAR MENSAGENS DO FIREBASE
        firebase = ConfiguracaoFirebase.getFirebase().child("mensagens")
                .child(idUserRemetente)
                .child(idUserDestinatario);

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mensagens.clear();

                for (DataSnapshot dados: dataSnapshot.getChildren()){
                    Mensagem mensagem = dados.getValue(Mensagem.class);
                    mensagens.add(mensagem);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        firebase.addValueEventListener(valueEventListener);

        //enviar mensagem
        btnMensagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textoMensagem = edtMensagemn.getText().toString();

                if (textoMensagem.isEmpty()){
                    Toast.makeText(ConversaActivity.this, "Digite uma mensagem para enviar", Toast.LENGTH_SHORT).show();
                }else{
                    Mensagem mensagem = new Mensagem();
                    mensagem.setIdUsuario(idUserRemetente);
                    mensagem.setMensagem(textoMensagem);

                    Boolean retornoMSGRementente = salvarMensagem(idUserRemetente, idUserDestinatario , mensagem);

                    if( !retornoMSGRementente){
                        Toast.makeText(ConversaActivity.this, "Problema ao enviar a mensagem, tente novamente mais tarde.(remetente)", Toast.LENGTH_SHORT).show();
                    }else{
                        Boolean retornoMSGDestinatario = salvarMensagem(idUserDestinatario, idUserRemetente , mensagem);

                        if( !retornoMSGDestinatario) {
                            Toast.makeText(ConversaActivity.this, "Problema ao enviar a mensagem, tente novamente mais tarde.(destinatario)", Toast.LENGTH_SHORT).show();
                        }
                    }

                    Conversa conversaDes = new Conversa();
                    conversaDes.setIdUsuario(idUserDestinatario);
                    conversaDes.setNome(nomeUserDestinatario);
                    conversaDes.setMensagem(textoMensagem);

                    Boolean retConversaRemetente = salvarConversa(idUserRemetente, idUserDestinatario, conversaDes);

                    if(!retConversaRemetente){
                        Toast.makeText(ConversaActivity.this, "Problema ao salvar a conversa, tente novamente!", Toast.LENGTH_SHORT).show();
                    }else{

                        Conversa conversaRem = new Conversa();
                        conversaRem.setIdUsuario(idUserRemetente);
                        conversaRem.setNome(nomeUserRemetente);
                        conversaRem.setMensagem(textoMensagem);

                        Boolean retConversaDestinatario = salvarConversa(idUserDestinatario, idUserRemetente, conversaRem);

                    }

                    edtMensagemn.setText("");
                }

            }
        });
    }


    private boolean salvarMensagem(String idRemetente, String idDestinatario, Mensagem mensagem){
        try{
            firebase = ConfiguracaoFirebase.getFirebase().child("mensagens");

            firebase.child(idRemetente)
                    .child(idDestinatario)
                    .push()
                    .setValue(mensagem);

            return true;
        }catch (Exception e){
            e.printStackTrace();
            return  false;
        }

    }

    private boolean salvarConversa(String idRemetente, String idDestinatario, Conversa conversa){
        try{
            firebase = ConfiguracaoFirebase.getFirebase().child("conversas");
            firebase.child(idRemetente)
                    .child(idDestinatario)
                    .setValue(conversa);
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        firebase.removeEventListener(valueEventListener);
    }

}
