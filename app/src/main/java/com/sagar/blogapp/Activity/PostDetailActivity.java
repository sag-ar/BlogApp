package com.sagar.blogapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Message;
import android.text.format.DateFormat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sagar.blogapp.Adapters.CommentAdapter;
import com.sagar.blogapp.Models.Comment;
import com.sagar.blogapp.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class PostDetailActivity extends AppCompatActivity {


    ImageView Post_image,Post_profile,Post_mini_photo;
    TextView PostTitle,PostDiscription,PostDate;
    EditText PostComment;
    String PostKey;
    Button AddComment;
    RecyclerView CommentRecyclerView;
    CommentAdapter commentAdapter;
    List<Comment> listComment;
    static String COMMENT_KEY = "Comment";

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);


        CommentRecyclerView = findViewById(R.id.rv_comment);
        Post_image = findViewById(R.id.Post_img);
        Post_profile  =findViewById(R.id.post_profile);
        Post_mini_photo = findViewById(R.id.post_mini_photo);
        PostTitle = findViewById(R.id.Post_title);
        PostDiscription = findViewById(R.id.post_discription);
        PostDate = findViewById(R.id.date);
        PostComment = findViewById(R.id.post_comment);
        AddComment = findViewById(R.id.post_add);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();

         AddComment.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 AddComment.setVisibility(View.INVISIBLE);
                 DatabaseReference databaseReference = firebaseDatabase.getReference().child(COMMENT_KEY).push();
                 String Comment_Content = PostComment.getText().toString();
                 String uid = firebaseUser.getUid();
                 String uname = firebaseUser.getDisplayName();
                 String uimg = firebaseUser.getPhotoUrl().toString();

                 Comment comment = new Comment(Comment_Content,uid,uimg,uname);
                 databaseReference.setValue(comment).addOnSuccessListener(new OnSuccessListener<Void>() {
                     @Override
                     public void onSuccess(Void aVoid) {
                         ShowMessage("Comment Added");
                         PostComment.setText("");
                         AddComment.setVisibility(View.VISIBLE);

                     }
                 }).addOnFailureListener(new OnFailureListener() {
                     @Override
                     public void onFailure(@NonNull Exception e) {
                         ShowMessage("Fail to add comment: " + e.getMessage());
                         AddComment.setVisibility(View.VISIBLE);
                     }
                 });

             }
         });

        String Postimage = getIntent().getExtras().getString("postimage");
        Glide.with(this).load(Postimage).into(Post_image);

        String Miniphoto = getIntent().getExtras().getString("userprofile");
        Glide.with(this).load(Miniphoto).into(Post_mini_photo);

        String posttitle = getIntent().getExtras().getString("title");
        PostTitle.setText(posttitle);

        String postdiscription = getIntent().getExtras().getString("discription");
        PostDiscription.setText(postdiscription);

        Glide.with(this).load(firebaseUser.getPhotoUrl()).into(Post_profile);


        PostKey = getIntent().getExtras().getString("postkey");

        String date = timestamptostring(getIntent().getExtras().getLong("postdate"));
        PostDate.setText(date);

        iniRecyclerView();

    }

    private void iniRecyclerView() {

        CommentRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        DatabaseReference CommentReference = firebaseDatabase.getReference(COMMENT_KEY).child(PostKey);
        CommentReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                 listComment = new ArrayList<>();
                 for(DataSnapshot snap:dataSnapshot.getChildren()){
                     Comment comment = snap.getValue(Comment.class);
                     listComment.add(comment);
                 }
                commentAdapter = new CommentAdapter(getApplicationContext(),listComment);
                CommentRecyclerView.setAdapter(commentAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    private void ShowMessage(String Message) {
        Toast.makeText(this, Message, Toast.LENGTH_SHORT).show();
    }

    private String timestamptostring(long time){

             Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
             calendar.setTimeInMillis(time);
             String date = DateFormat.format("dd-mm-yyyy",calendar).toString();
             return date;
         }
}