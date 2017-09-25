package letstalk.projetos.com.letstalk.adapter;

import letstalk.projetos.com.letstalk.R;
import letstalk.projetos.com.letstalk.model.Conversa;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ConversaAdapter extends ArrayAdapter<Conversa> {

    private ArrayList<Conversa> conversas;
    private Context context;


    public ConversaAdapter(Context c,  ArrayList<Conversa> objects) {
        super(c, 0, objects);

        this.conversas = objects;
        this.context = c;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = null;

        if(conversas != null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.lista_conversas, parent, false);

            TextView nomeContato = (TextView) view.findViewById(R.id.tv_nome);
            TextView msgContato = (TextView) view.findViewById(R.id.tv_mensagem);

            Conversa conversa = conversas.get(position);
            nomeContato.setText(conversa.getNome());
            msgContato.setText(conversa.getMensagem());

        }

        return view;
    }
}