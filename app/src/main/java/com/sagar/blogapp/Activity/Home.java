package com.sagar.blogapp.Activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sagar.blogapp.Fragments.HomeFragment;
import com.sagar.blogapp.Fragments.ProfileFragment;
import com.sagar.blogapp.Fragments.SettingFragment;
import com.sagar.blogapp.Models.Post;
import com.sagar.blogapp.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;

    FirebaseAuth mAuth;
    FirebaseUser user;
    int pReqcode  = 2;
    int REQUESCODE = 2;
    Uri pickedimguri;
    DrawerLayout drawer;
    Dialog PopupAddPost;
    ImageView UserPhoto,BackgroundPhoto,Add;
    TextView PopupTitle,PopupDiscription;
    ProgressBar LoadingBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupAddPost.show();
            }
        });

        inipopup();
        SetupPopupImageClick();
         drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        UpdateNavHeader();

        getSupportFragmentManager().beginTransaction().replace(R.id.container,new HomeFragment()).commit();
    }

    private void SetupPopupImageClick() {
        BackgroundPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Checkandrequestforpermission();
            }
        });

    }
    private void Checkandrequestforpermission() {
        if(ContextCompat.checkSelfPermission(Home.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(Home.this, Manifest.permission.READ_EXTERNAL_STORAGE)){
                Toast.makeText(Home.this, "Please accept reqiured permissions", Toast.LENGTH_SHORT).show();
            }else{

                ActivityCompat.requestPermissions(Home.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, pReqcode);
            }

        }else{
            Opengallery();
        }

    }
    private void Opengallery() {
        Intent galleryintent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryintent.setType("image/*");
        startActivityForResult(galleryintent, REQUESCODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == REQUESCODE && data != null){
            pickedimguri = data.getData();
            BackgroundPhoto.setImageURI(pickedimguri);
        }

    }

    private void inipopup() {
        PopupAddPost = new Dialog(Home.this);
        PopupAddPost.setContentView(R.layout.popup_add_post);
        PopupAddPost.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        PopupAddPost.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT,Toolbar.LayoutParams.WRAP_CONTENT);
        PopupAddPost.getWindow().getAttributes().gravity = Gravity.TOP;

        UserPhoto = PopupAddPost.findViewById(R.id.popup_user_photo);
        BackgroundPhoto = PopupAddPost.findViewById(R.id.popup_background);
        Add = PopupAddPost.findViewById(R.id.popup_add);
        PopupTitle = PopupAddPost.findViewById(R.id.popup_title);
        PopupDiscription = PopupAddPost.findViewById(R.id.popup_discription);
        LoadingBar = PopupAddPost.findViewById(R.id.loding_bar);


        Glide.with(Home.this).load(user.getPhotoUrl()).into(UserPhoto);

        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Add.setVisibility(View.INVISIBLE);
                LoadingBar.setVisibility(View.VISIBLE);

                if(!PopupTitle.getText().toString().isEmpty() && !PopupDiscription.getText().toString().isEmpty() && pickedimguri != null){
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Blog_images");
                    final StorageReference ImagefilePath = storageReference.child(pickedimguri.getLastPathSegment());
                    ImagefilePath.putFile(pickedimguri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            ImagefilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                  String  imageDownloadLink = uri.toString();
                                  Post post = new Post(PopupTitle.getText().toString(),
                                            PopupDiscription.getText().toString(),
                                            imageDownloadLink,
                                            user.getUid(),
                                            user.getPhotoUrl().toString());


                                    addPost(post);
                                }

                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    ShowMessage(e.getMessage());
                                    Add.setVisibility(View.VISIBLE);
                                    LoadingBar.setVisibility(View.INVISIBLE);
                                }
                            });

                        }
                    });
                }

                else{
                    ShowMessage("Please verify all the Fields");
                    Add.setVisibility(View.VISIBLE);
                    LoadingBar.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void addPost(Post post) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference Myref = database.getReference("Posts").push();

        String key = Myref.getKey();
        post.setPostKey(key);

        Myref.setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                ShowMessage("Post Added Successfully");
                Add.setVisibility(View.VISIBLE);
                LoadingBar.setVisibility(View.INVISIBLE);
                PopupAddPost.dismiss();
            }
        });

    }

    private void ShowMessage(String message) {
        Toast.makeText(this, message , Toast.LENGTH_SHORT).show();

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.nav_home){
            getSupportActionBar().setTitle("Home");
            getSupportFragmentManager().beginTransaction().replace(R.id.container,new HomeFragment()).commit();
        }else if(id == R.id.nav_profile){
            getSupportActionBar().setTitle("Profile");
            getSupportFragmentManager().beginTransaction().replace(R.id.container,new ProfileFragment()).commit();
        }else if(id == R.id.nav_setting){
            getSupportActionBar().setTitle("Setting");
            getSupportFragmentManager().beginTransaction().replace(R.id.container,new SettingFragment()).commit();
        }else if(id == R.id.nav_logout){
            FirebaseAuth.getInstance().signOut();
            Intent intent  = new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(intent);
            finish();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else{
        super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }


    public void UpdateNavHeader(){

        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = headerView.findViewById(R.id.tv_name);
        TextView navemail = headerView.findViewById(R.id.tv_mail);
        ImageView navUserPhoto = headerView.findViewById(R.id.iv_photo);

        navUsername.setText(user.getDisplayName());
        navemail.setText(user.getEmail());
        Glide.with(this).load(user.getPhotoUrl()).into(navUserPhoto);
    }
}