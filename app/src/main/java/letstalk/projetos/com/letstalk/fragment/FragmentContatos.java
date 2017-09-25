package letstalk.projetos.com.letstalk.fragment;

import android.content.Intent;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import letstalk.projetos.com.letstalk.R;
import letstalk.projetos.com.letstalk.activity.ConversaActivity;
import letstalk.projetos.com.letstalk.adapter.ContatoAdapter;
import letstalk.projetos.com.letstalk.config.ConfiguracaoFirebase;
import letstalk.projetos.com.letstalk.helper.Base64Custom;
import letstalk.projetos.com.letstalk.helper.Preferencias;
import letstalk.projetos.com.letstalk.model.Contato;

public class FragmentContatos extends Fragment {


    private ListView listView;
    private ArrayAdapter adapter;
    private ArrayList<Contato> contatos;

    private DatabaseReference firebase;
    private ValueEventListener valueEventListenerContato;

    public FragmentContatos(){
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        //INICIA O MONITORAMENTO DO FIREBASE
        firebase.addListenerForSingleValueEvent(valueEventListenerContato);
    }

    @Override
    public void onStop() {
        super.onStop();
        //PARA O LISTENER DO FIREBASE PARA NÃO CONSUMIR RECURSOS DO DISPOSITIVO
        firebase.removeEventListener(valueEventListenerContato);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //instanciar array contatos
        contatos = new ArrayList<>();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contatos, container, false);

        listView = (ListView) view.findViewById(R.id.lv_contatos);
        /*adapter = new ArrayAdapter(
                getActivity(), R.layout.lista_contato, contatos
        );*/

        adapter = new ContatoAdapter(getActivity(), contatos);
        listView.setAdapter(adapter);

        //RECUPERAR CONTATOS
        Preferencias preferencias = new Preferencias(getActivity());
        String identificadoruserLogado = preferencias.getIdentificador();

        firebase = ConfiguracaoFirebase.getFirebase().child("contatos").child(identificadoruserLogado);

        valueEventListenerContato = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //Limpar lista
                contatos.clear();

                //Listar contatos
                for(DataSnapshot dados: dataSnapshot.getChildren()){
                    Contato contato = dados.getValue(Contato.class);
                    contatos.add(contato);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ConversaActivity.class);

                //recupera nome e email
                Contato contato = contatos.get(position);

                intent.putExtra("nome", contato.getNome());
                intent.putExtra("email", contato.getEmail());

                startActivity(intent);
            }
        });


        return view;
    }


}
