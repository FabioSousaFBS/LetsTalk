package letstalk.projetos.com.letstalk.adapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import letstalk.projetos.com.letstalk.R;
import letstalk.projetos.com.letstalk.model.Conversa;

public class ConversaAdapter extends ArrayAdapter<Conversa> {

    private ArrayList<Conversa> conversas;
    private Context contexto;

    public ConversaAdapter(Context c, ArrayList<Conversa> objects) {
        super(c, 0, objects);

        this.contexto = c;
        this.conversas = objects;

    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = null;

        //verifica se existe dados para serem listados
        if(conversas != null){
            //inicializa o objeto para montagem da view
            LayoutInflater inflater = (LayoutInflater) contexto.getSystemService(contexto.LAYOUT_INFLATER_SERVICE);

            //MONTAR VIEW A PARTIR DO XML
            view = inflater.inflate(R.layout.lista_conversas, parent, false);

            TextView nomeContato = (TextView) view.findViewById(R.id.tv_nome);
            TextView mensagemContato = (TextView) view.findViewById(R.id.tv_mensagem);

            Conversa conversa = conversas.get(position);
            nomeContato.setText(conversa.getNome());
            mensagemContato.setText(conversa.getMensagem());

        }

        return view;

    }
}