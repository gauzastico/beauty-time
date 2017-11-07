package br.fatec.project.beautytime.view;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class SignInActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText mEmail;
    private EditText mPassword;

    private User mUser = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (isAuthenticated()){
            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
            startActivity(intent);
        }



        mEmail = findViewById(R.id.login_email);
        mPassword = findViewById(R.id.login_password);

        Button loginButton = findViewById(R.id.login_sign_in_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEmail.getText().toString().isEmpty() || mPassword.getText().toString().isEmpty()){
                    Toast.makeText(SignInActivity.this,
                            "Preencha o campo de e-mail e senha.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                mUser.setEmail(mEmail.getText().toString());
                mUser.setPassword(mPassword.getText().toString());
                authentication();
            }
        });

        Button signUpButton = findViewById(R.id.login_create_button);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
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
                            Toast.makeText(SignInActivity.this,
                                    "Login realizado com sucesso!.",
                                    Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                        } else {
                            Toast.makeText(SignInActivity.this,
                                    "Usuário ou senha invalidos!.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    private boolean isAuthenticated(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return user != null;
    }
}
