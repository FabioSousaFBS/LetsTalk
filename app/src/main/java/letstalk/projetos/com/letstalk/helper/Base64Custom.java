package letstalk.projetos.com.letstalk.helper;

import android.util.Base64;

public class Base64Custom {


    public static String CodificarBase64(String texto){
        return Base64.encodeToString(texto.getBytes(), Base64.DEFAULT).replaceAll("(\\n|\\r)", "");
    }

    public  static String DecodificarBase64(String textoCod){
        return new String(Base64.decode(textoCod, Base64.DEFAULT));
    }



}
