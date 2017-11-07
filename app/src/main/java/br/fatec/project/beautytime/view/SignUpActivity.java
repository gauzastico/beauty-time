package br.fatec.project.beautytime.view;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import br.fatec.project.beautytime.R;
import br.fatec.project.beautytime.model.User;

public class SignUpActivity extends AppCompatActivity {

    private EditText mName;
    private EditText mEmail;
    private EditText mPassword;
    private EditText mPasswordRepeat;
    private EditText mPhone;
    private EditText mAddress;
    private CheckBox mCompanyCheckBox;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private User mUser = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mName = findViewById(R.id.signup_name);
        mEmail = findViewById(R.id.signup_email);
        mPassword = findViewById(R.id.signup_password);
        mPasswordRepeat = findViewById(R.id.signup_password_repeat);
        mPhone = findViewById(R.id.signup_phone);
        mAddress = findViewById(R.id.signup_address);

        mCompanyCheckBox = findViewById(R.id.signup_company);
        mCompanyCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setVisibilityAddress(isChecked);
            }
        });

        setVisibilityAddress(false);

        final Button cancelButton = findViewById(R.id.signup_cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final Button createButton = findViewById(R.id.signup_button);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPassword.getText().toString().equals(mPasswordRepeat.getText().toString())){
                    mUser.setName(mName.getText().toString());
                    mUser.setEmail(mEmail.getText().toString());
                    mUser.setPassword(mPassword.getText().toString());
                    mUser.setPhone(mPhone.getText().toString());
                    if (mCompanyCheckBox.isChecked()){
                        mUser.setAddress(mAddress.getText().toString());
                        mUser.setType("Company");
                    } else {
                        mUser.setAddress("");
                        mUser.setType("Client");
                    }

                    createUser();
                } else {
                    Toast.makeText(SignUpActivity.this,
                            "As senhas não se correspondem!",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private  void setVisibilityAddress(boolean visibility){
        if (visibility)
            mAddress.setVisibility(View.VISIBLE);
        else
            mAddress.setVisibility(View.INVISIBLE);
    }

    private void createUser(){
        mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(mUser.getEmail(), mUser.getPassword())
            .addOnCompleteListener(SignUpActivity.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            mReference = FirebaseDatabase.getInstance().getReference().child("users");
                            mReference.push().setValue(mUser);
                            Toast.makeText(SignUpActivity.this,
                                    "Usuário cadastrado com sucesso",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(SignUpActivity.this,
                                    "Erro",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
