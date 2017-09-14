package letstalk.projetos.com.letstalk.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.ArrayList;
import java.util.List;

import letstalk.projetos.com.letstalk.R;
import letstalk.projetos.com.letstalk.model.Contato;

public class ContatoAdapter extends ArrayAdapter<Contato> {

    private ArrayList<Contato> contatos;
    private Context contexto;

    public ContatoAdapter(Context c, ArrayList<Contato> objects) {
        super(c, 0, objects);

        this.contexto = c;
        this.contatos = objects;

    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = null;

        //verifica se existe dados para serem listados
        if(contatos != null){
            //inicializa o objeto para montagem da view
            LayoutInflater inflater = (LayoutInflater) contexto.getSystemService(contexto.LAYOUT_INFLATER_SERVICE);

            //MONTAR VIEW A PARTIR DO XML
            view = inflater.inflate(R.layout.lista_contatos, parent, false);

            TextView nomeContato = (TextView) view.findViewById(R.id.tv_nome);
            TextView emailContato = (TextView) view.findViewById(R.id.tv_email);

            Contato contato = contatos.get(position);
            nomeContato.setText(contato.getNome());
            emailContato.setText(contato.getEmail());

        }

        return view;

    }
}
