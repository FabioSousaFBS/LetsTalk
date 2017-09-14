package letstalk.projetos.com.letstalk.fragment;

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

    /*@Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_contatos, container, false);
    }*/

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
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,
                             Bundle savedInstanceState) {

        //ISTANCIA O ARRAY DE CONTATOS
        contatos = new ArrayList<>();

        View view = inflater.inflate(R.layout.fragment_contatos, container, false);

        listView = (ListView) view.findViewById(R.id.lv_contatos);
        adapter = new ContatoAdapter(getActivity(), contatos);

        listView.setAdapter(adapter);


        //RECUPERA DADOS DO USER LOGADO

        Preferencias preferencias = new Preferencias(getActivity());
        String identificadorUserLogado = preferencias.getIdentificador();

        firebase = ConfiguracaoFirebase.getFirebase();
        firebase = firebase.child("contatos").child(identificadorUserLogado);

        //ATIVA O EVENTLITENER PARA MONITORAR SE HÁ UM NOVO CONTATO NA LISTA.
        valueEventListenerContato = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //LIMPA A LISTA PARA NÃO DUPLICAR INFORMAÇÕES
                contatos.clear();

                //lista os dados
                for(DataSnapshot dados : dataSnapshot.getChildren()){
                    Contato contato = dados.getValue(Contato.class);
                    contatos.add(contato);
                }

                //NOTIFICA O ADAPTER DO LISTVIEW DE CONTATOS QUE HOUVE ALTERAÇÕES
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        return view;
    }


}
