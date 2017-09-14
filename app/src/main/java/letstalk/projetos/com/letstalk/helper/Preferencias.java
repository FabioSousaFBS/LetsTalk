package letstalk.projetos.com.letstalk.helper;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferencias {

    private Context contexto;
    private SharedPreferences preferences;

    private final String NOME_ARQUIVO = "LetsTalk.preferencias";
    private final int MODE = 0;
    private SharedPreferences.Editor editor;

    private final String CHAVE_IDENTIFICADOR = "identificadorUsuarioLogado";
    private final String CHAVE_NOME = "nomeUsuarioLogado";


    public Preferencias(Context contextoParametro){

        contexto = contextoParametro;
        preferences = contexto.getSharedPreferences(NOME_ARQUIVO, MODE);
        editor = preferences.edit();

    }

    public void SalvarDados(String identificadorUsuario, String NomeUser) {

        editor.putString(CHAVE_IDENTIFICADOR, identificadorUsuario);
        editor.putString(CHAVE_NOME, NomeUser);

        editor.commit();

    }


    public String getIdentificador(){
        return preferences.getString(CHAVE_IDENTIFICADOR, null);
    }


    public String getNomeUsuario(){
        return preferences.getString(CHAVE_NOME, null);
    }


}
