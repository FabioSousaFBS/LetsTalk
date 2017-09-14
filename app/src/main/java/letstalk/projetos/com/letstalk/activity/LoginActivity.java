package letstalk.projetos.com.letstalk.activity;

import android.Manifest;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import letstalk.projetos.com.letstalk.R;
import letstalk.projetos.com.letstalk.config.ConfiguracaoFirebase;
import letstalk.projetos.com.letstalk.helper.Base64Custom;
import letstalk.projetos.com.letstalk.helper.Preferencias;
import letstalk.projetos.com.letstalk.model.Usuario;

public class LoginActivity extends AppCompatActivity {


    private Button btnLogar;
    private EditText edtNome;
    private EditText edtSenha;
    private TextView txtCadastrar;

    private Usuario usuario;
    private String identificadorUsuarioLogado;

    private FirebaseAuth autenticacao;
    private DatabaseReference firebase;
    private ValueEventListener valueEventListener;


    private String[] permissoesNecessarias = new String[]{
            Manifest.permission.INTERNET,
            Manifest.permission.SEND_SMS
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogar = (Button) findViewById(R.id.btnLogar);
        edtNome = (EditText) findViewById(R.id.edtUser);
        edtSenha = (EditText) findViewById(R.id.edtSenha);
        txtCadastrar = (TextView) findViewById(R.id.txtCadastro);

        VerificaUsuarioLogado();

        btnLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usuario = new Usuario();
                usuario.setEmail(edtNome.getText().toString());
                usuario.setSenha(edtSenha.getText().toString());

                ValidarLogin();

            }
        });

    }

    private void VerificaUsuarioLogado(){
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        if(autenticacao.getCurrentUser() != null){
            AbrirTelaPrincipal();
        }
    }

    private void AbrirTelaPrincipal(){
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    private void ValidarLogin(){
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.signInWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    identificadorUsuarioLogado = Base64Custom.CodificarBase64(usuario.getEmail());

                    firebase = ConfiguracaoFirebase.getFirebase();
                    firebase = firebase.child("usuarios")
                            .child(identificadorUsuarioLogado);

                    valueEventListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Usuario userRetornado = dataSnapshot.getValue(Usuario.class);

                            Preferencias preferencias = new Preferencias(LoginActivity.this);
                            preferencias.SalvarDados(identificadorUsuarioLogado, userRetornado.getNome());
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    };

                    firebase.addListenerForSingleValueEvent(valueEventListener);

                    AbrirTelaPrincipal();
                }else{

                    String excecaoErro;
                    try{
                        throw task.getException();

                    }catch (FirebaseAuthInvalidUserException e){
                        excecaoErro = "Usuário invalido, verifique se o email foi digitado corretamente";
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        excecaoErro = "Senha incorreta!";
                    }catch (Exception e){
                        excecaoErro = "Erro ao efetuar o Login!";
                    }

                    Toast.makeText(LoginActivity.this, excecaoErro, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //BOTÕES
    public void AbrirCadastroUser(View view){
        Intent intent = new Intent(LoginActivity.this, CadastroUserActivity.class);
        startActivity(intent);
    }





}
