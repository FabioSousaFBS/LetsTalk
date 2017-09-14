package letstalk.projetos.com.letstalk.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

import letstalk.projetos.com.letstalk.R;
import letstalk.projetos.com.letstalk.config.ConfiguracaoFirebase;
import letstalk.projetos.com.letstalk.helper.Base64Custom;
import letstalk.projetos.com.letstalk.helper.Preferencias;
import letstalk.projetos.com.letstalk.model.Usuario;

public class CadastroUserActivity extends AppCompatActivity {

    private Button btnCadastrar;
    private EditText edtCadUsuario;
    private EditText edtCadSenha;
    private EditText edtCadNomeuser;

    private Usuario usuario;

    private FirebaseAuth autenticacao;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_user);

        btnCadastrar = (Button) findViewById(R.id.btnAddCadastro);
        edtCadNomeuser = (EditText) findViewById(R.id.edtAddNome);
        edtCadUsuario = (EditText) findViewById(R.id.edtAddEmail);
        edtCadSenha = (EditText) findViewById(R.id.edtAddSenha);

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usuario = new Usuario();
                usuario.setNome(edtCadNomeuser.getText().toString());
                usuario.setEmail(edtCadUsuario.getText().toString());
                usuario.setSenha(edtCadSenha.getText().toString());

                CadastrarUsuario();

            }
        });

    }


    private void CadastrarUsuario(){

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        autenticacao.createUserWithEmailAndPassword(
                usuario.getEmail(), usuario.getSenha()
        ).addOnCompleteListener(CadastroUserActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(CadastroUserActivity.this, "Usuário cadastrado com sucesso!", Toast.LENGTH_SHORT).show();

                    FirebaseUser usuarioFirebase = task.getResult().getUser();

                    String identificadorUsuario = Base64Custom.CodificarBase64(usuario.getEmail());

                    //usuario.setId(usuarioFirebase.getUid());
                    usuario.setId(identificadorUsuario);
                    usuario.salvar();

                    Preferencias preferencias = new Preferencias(CadastroUserActivity.this);
                    preferencias.SalvarDados(identificadorUsuario, usuario.getNome());

                    AbrirLoginUsuario();

                }else{

                    String erroExcecao = "";

                    try{
                        throw task.getException();
                    }catch (FirebaseAuthWeakPasswordException e){
                        erroExcecao = "Digite uma senha mais forte, contendo mais caracteres e com letras e números!";
                    }catch (FirebaseAuthInvalidCredentialsException e ) {
                        erroExcecao = "O email digitado é invalido, digite um novo email.";
                    }catch (FirebaseAuthUserCollisionException e ){
                        erroExcecao = "Usuário já esta cadastrado no sistema.";
                    }catch (Exception e){
                        erroExcecao = "Erro ao efetuar o cadastro!";
                        e.printStackTrace();
                    }
                    Toast.makeText(CadastroUserActivity.this, "Erro: " + erroExcecao, Toast.LENGTH_SHORT).show();

                }
            }
        });

    }


    public void AbrirLoginUsuario(){
        Intent intent = new Intent(CadastroUserActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }


}
