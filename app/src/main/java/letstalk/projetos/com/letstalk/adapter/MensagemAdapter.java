package letstalk.projetos.com.letstalk.adapter;


import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import letstalk.projetos.com.letstalk.R;
import letstalk.projetos.com.letstalk.helper.Preferencias;
import letstalk.projetos.com.letstalk.model.Mensagem;


public class MensagemAdapter extends ArrayAdapter<Mensagem>{

    private ArrayList<Mensagem> mensagens;
    private Context contexto;

    public MensagemAdapter(Context c, ArrayList<Mensagem> objects) {
        super(c, 0, objects);

        this.contexto = c;
        this.mensagens = objects;

    }


    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = null;

        if(mensagens != null ){

            Preferencias preferencias = new Preferencias(contexto);
            String idUserRemetente = preferencias.getIdentificador();

            LayoutInflater inflater = (LayoutInflater) contexto.getSystemService(contexto.LAYOUT_INFLATER_SERVICE);

            Mensagem mensagem = mensagens.get(position);

            if(idUserRemetente.equals(mensagem.getIdUsuario())){
                view = inflater.inflate(R.layout.item_direita, parent, false);
            }else{
                view = inflater.inflate(R.layout.item_esquerda, parent, false);
            }

            TextView msgEnviada = (TextView) view.findViewById(R.id.tv_mensagem);
            msgEnviada.setText(mensagem.getMensagem());

        }

        return view;
    }
}






















