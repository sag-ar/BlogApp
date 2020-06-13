package com.sagar.blogapp.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sagar.blogapp.R;

public class RegisterActivity extends AppCompatActivity {

    ImageView userimg;
    static  final int pReqcode = 1;
    static final int REQUESCODE = 1;
    Uri pickedimguri;
    EditText user,email,password,password2;
    Button register;
    ProgressBar loading;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        user = findViewById(R.id.user);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        password2 = findViewById(R.id.password2);
        register = findViewById(R.id.btn_reg);
        loading = findViewById(R.id.progressBar);
        loading.setVisibility(View.INVISIBLE);
        userimg = findViewById(R.id.iv_user);
        auth = FirebaseAuth.getInstance();


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register.setVisibility(View.INVISIBLE);
                loading.setVisibility(View.VISIBLE);
                String users = user.getText().toString();
                String emails = email.getText().toString();
                String mPassword = password.getText().toString();
                String mPassword2 = password2.getText().toString();

                if(users.isEmpty() || emails.isEmpty() || mPassword.isEmpty() || !mPassword.equals(mPassword2)){
                        ShowMessage("Please Verify The Fields");
                        register.setVisibility(View.VISIBLE);
                        loading.setVisibility(View.INVISIBLE);
                }else if(mPassword.length() < 6){
                        ShowMessage("Password length is short");
                    register.setVisibility(View.VISIBLE);
                    loading.setVisibility(View.INVISIBLE);
                }else{
                    RegisterUser(emails,users,mPassword);
                }
            }
        });





        userimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT>22){
                    Checkandrequestforpermission();
                }else{
                    Opengallery();
                }
            }
        });

    }

    private void RegisterUser(String emails, final String users, String mPassword) {
        auth.createUserWithEmailAndPassword(emails,mPassword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    UpdateUserInfo(users,pickedimguri,auth.getCurrentUser());
                }else{
                    ShowMessage("Registeration Fsiled");
                }
            }
        });

    }

    private void UpdateUserInfo(final String users, Uri pickedimguri, final FirebaseUser currentUser) {
        StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("users_photo");
        final StorageReference imgfilepath = mStorage.child(pickedimguri.getLastPathSegment());
        imgfilepath.putFile(pickedimguri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                imgfilepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                .setDisplayName(users).setPhotoUri(uri).build();

                        currentUser.updateProfile(profileUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    ShowMessage("Register Complete");
                                    UpdateUi();
                                }
                            }
                        });
                    }
                });

            }
        });


    }

    private void UpdateUi() {
        Intent intent = new Intent(getApplicationContext(), Home.class);
        startActivity(intent);
        finish();
    }

    private void ShowMessage(String Message) {
        Toast.makeText(this, Message, Toast.LENGTH_SHORT).show();
    }

    private void Opengallery() {
        Intent galleryintent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryintent.setType("image/*");
        startActivityForResult(galleryintent,REQUESCODE);


    }

    private void Checkandrequestforpermission() {
        if(ContextCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(RegisterActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)){
                Toast.makeText(RegisterActivity.this, "Please accept reqiured permissions", Toast.LENGTH_SHORT).show();
            }else{
                ActivityCompat.requestPermissions(RegisterActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, pReqcode);
            }

        }else{
            Opengallery();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == REQUESCODE && data != null){
            pickedimguri = data.getData();
            userimg.setImageURI(pickedimguri);
        }

    }
}
