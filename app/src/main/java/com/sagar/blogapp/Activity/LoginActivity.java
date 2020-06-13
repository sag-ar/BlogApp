package com.sagar.blogapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.strictmode.WebViewMethodCalledOnWrongThreadViolation;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sagar.blogapp.R;

public class LoginActivity extends AppCompatActivity {

    EditText email,password;
    Button Login;
    ProgressBar loding;
    FirebaseAuth mAuth;
    Intent HomeActivity;
    ImageView userimg;
    TextView create_Account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        Login = findViewById(R.id.btn_log);
        loding = findViewById(R.id.progressBar);
        create_Account = findViewById(R.id.create_account);
        mAuth = FirebaseAuth.getInstance();
        HomeActivity = new Intent(this, com.sagar.blogapp.Activity.Home.class);
        userimg = findViewById(R.id.iv_user);

        userimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }

        });

                create_Account.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });




        loding.setVisibility(View.INVISIBLE);

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login.setVisibility(View.INVISIBLE);
                loding.setVisibility(View.VISIBLE);
                String emails = email.getText().toString();
                String passwords = password.getText().toString();
                if(emails.isEmpty() || passwords.isEmpty()){
                    ShowMessage("Please verify the field");
                    Login.setVisibility(View.VISIBLE);
                    loding.setVisibility(View.INVISIBLE);
                }else{
                    signin(emails,passwords);
                }
            }
        });


    }

    private void signin(String emails, String passwords) {

        mAuth.signInWithEmailAndPassword(emails, passwords).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    ShowMessage("Login Success");
                    updateui();
                }else{
                    ShowMessage(task.getException().getMessage());
                    Login.setVisibility(View.VISIBLE);
                    loding.setVisibility(View.INVISIBLE);

                }
            }
        });
    }

    private void updateui() {
        startActivity(HomeActivity);
        finish();
    }

    private void ShowMessage(String message) {
        Toast.makeText(this, message , Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null){
            updateui();
        }
    }
}
