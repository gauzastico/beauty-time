package br.fatec.project.beautytime.view;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import br.fatec.project.beautytime.R;
import br.fatec.project.beautytime.model.User;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText mEmail;
    private EditText mPassword;
    private Button mLoginButton;

    private User mUser = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmail = findViewById(R.id.login_email);
        mPassword = findViewById(R.id.login_password);
        mLoginButton = findViewById(R.id.login_sign_in_button);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEmail.getText().toString().isEmpty() || mPassword.getText().toString().isEmpty()){
                    Toast.makeText(LoginActivity.this,
                            "Preencha o campo de e-mail e senha.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                mUser.setEmail(mEmail.getText().toString());
                mUser.setPassword(mPassword.getText().toString());
                authentication();
            }
        });
    }

    private void authentication(){
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(mUser.getEmail(), mUser.getPassword())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this,
                                    "Login realizado com sucesso!.",
                                    Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                        } else {
                            Toast.makeText(LoginActivity.this,
                                    "Usuário ou senha invalidos!.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }
}
