package com.example.usease.bellezza;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;


public class LoginActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private EditText mEmailView, mPasswordView;
    private Button mLoginBtn;
    private FirebaseAuth mAuth;
    private ProgressBar mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmailView = (EditText) findViewById(R.id.login_email);
        mPasswordView = (EditText) findViewById(R.id.login_password);
        mLoginBtn = (Button) findViewById(R.id.login_login_btn);
        mProgress = (ProgressBar) findViewById(R.id.login_progress_bar);

        mAuth = FirebaseAuth.getInstance();

        mToolbar = (Toolbar) findViewById(R.id.login_app_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.admin_login);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mProgress.setVisibility(View.VISIBLE);

                final String email = mEmailView.getText().toString().trim();
                final String password = mPasswordView.getText().toString().trim();

                if(!TextUtils.isEmpty(email) || !TextUtils.isEmpty(password)){

                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                mProgress.setVisibility(View.INVISIBLE);
                                Intent main_intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(main_intent);
                                finish();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mProgress.setVisibility(View.INVISIBLE);
                            Toast.makeText(LoginActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });


                } else {
                    mProgress.setVisibility(View.INVISIBLE);
                    Toast.makeText(LoginActivity.this, R.string.email_or_password_empty, Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if(item.getItemId() == android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);


    }
}
