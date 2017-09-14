package letstalk.projetos.com.letstalk.helper;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;


public class Permissao {

    public  static boolean ValidaPermissao(int requestCode, Activity activity, String[] permissoes){

        if(Build.VERSION.SDK_INT >= 23){
            List<String> listaPermissoes = new ArrayList<>();

            for(String permissao : permissoes){
                boolean validaPermissao = ContextCompat.checkSelfPermission(activity, permissao) == PackageManager.PERMISSION_GRANTED;

                if(!validaPermissao){
                    listaPermissoes.add(permissao);
                }

            }

            //SE A LISTA ESTIVER VAZIA, ENTÃO QUER DIZER QUE TEM TODAS AS PERMISSOES
            if(listaPermissoes.isEmpty()) return true;

            String[] novasPermissoes = new String[listaPermissoes.size()];
            listaPermissoes.toArray(novasPermissoes);

            //SOLICITAR PERMISSÃO AO USUARIO
            ActivityCompat.requestPermissions(activity, novasPermissoes, requestCode);

        }
        return true;
    }

}
