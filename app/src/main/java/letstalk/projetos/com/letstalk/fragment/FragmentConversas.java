package letstalk.projetos.com.letstalk.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import letstalk.projetos.com.letstalk.R;
import letstalk.projetos.com.letstalk.adapter.ContatoAdapter;
import letstalk.projetos.com.letstalk.adapter.ConversaAdapter;
import letstalk.projetos.com.letstalk.config.ConfiguracaoFirebase;
import letstalk.projetos.com.letstalk.helper.Preferencias;
import letstalk.projetos.com.letstalk.model.Contato;
import letstalk.projetos.com.letstalk.model.Conversa;


public class FragmentConversas  extends Fragment {


    private ListView listView;
    private ArrayAdapter adapter;
    private ArrayList<Conversa> conversas;

    private DatabaseReference firebase;
    private ValueEventListener valueEventListenerConversa;

    public FragmentConversas(){
        // Required empty public constructor
    }


    @Override
    public void onStart() {
        super.onStart();
        //INICIA O MONITORAMENTO DO FIREBASE
        firebase.addListenerForSingleValueEvent(valueEventListenerConversa);
    }

    @Override
    public void onStop() {
        super.onStop();
        //PARA O LISTENER DO FIREBASE PARA NÃO CONSUMIR RECURSOS DO DISPOSITIVO
        firebase.removeEventListener(valueEventListenerConversa);
    }


    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,
                             Bundle savedInstanceState) {

        conversas = new ArrayList<>();

        View view = inflater.inflate(R.layout.fragment_conversas, container, false);

        listView = (ListView) view.findViewById(R.id.lv_conversas);
        adapter = new ConversaAdapter(getActivity(), conversas);
        listView.setAdapter(adapter);

        //Recupera conversas
        Preferencias preferencias = new Preferencias(getActivity());
        String idUserLogado = preferencias.getIdentificador();

        firebase = ConfiguracaoFirebase.getFirebase().child("conversas").child(idUserLogado);

        //ATIVA O EVENTLITENER PARA MONITORAR SE HÁ UM NOVO CONTATO NA LISTA.
        valueEventListenerConversa = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //limpa lista
                conversas.clear();

                for( DataSnapshot dados : dataSnapshot.getChildren() ){
                    Conversa conversa = dados.getValue(Conversa.class);
                    conversas.add(conversa);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        return view;
    }


}
