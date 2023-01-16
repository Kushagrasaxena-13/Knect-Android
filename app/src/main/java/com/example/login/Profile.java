package com.example.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.login.Adapter.My_postAdapter;
import com.example.login.Adapter.PostAdapter;
import com.example.login.Model.Post;
import com.example.login.fragment.nav_home;
import com.example.login.fragment.nav_search;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends AppCompatActivity {

    public CircleImageView image_profile;
    public TextView username;
    public String str;
    public RecyclerView recyclerView;


    private My_postAdapter postAdapter;
    private List<Post> postList;



    public TextView follow;
    public TextView followers;
    public TextView followings;
    public ImageView back;
    public String user;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        username = findViewById(R.id.username);
        image_profile = findViewById(R.id.profile_image);
        follow = findViewById(R.id.follow);
        followers=findViewById(R.id.followers);
        followings = findViewById(R.id.following);
        back=findViewById(R.id.back);
        recyclerView = findViewById(R.id.recycler_view);

        Intent intent = getIntent();
        str = intent.getStringExtra("username");
        user = FirebaseAuth.getInstance().getCurrentUser().getUid();

        recyclerView.setHasFixedSize(true);
        GridLayoutManager linearLayoutManager = new GridLayoutManager(this,3,GridLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        postList = new ArrayList<>();
        postAdapter = new My_postAdapter(this,postList);
        recyclerView.setAdapter(postAdapter);


//        DatabaseReference reference= FirebaseDatabase.getInstance().getReference();


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(user);

        follow.setVisibility(View.GONE);



        if(str!=null) {
            follow.setVisibility(View.VISIBLE);
            getImage(str);
            getfollowers(str);
            know_follow(str);


        }



        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                follow(str);
            }
        });
        if(str==null){
            ourProfile();
            follow.setVisibility(View.GONE);
            getfollowers(user);
            readPosts(user);

        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    startActivity(new Intent(Profile.this, MainActivity2.class));

            }
        });

        image_profile.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(str==null) {
                    startActivity(new Intent(Profile.this, Change_dp.class));
                }
            }

        });


    }

    public void getfollowers(String str){

        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference().child("Follow").child(str);

        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String str = String.valueOf(snapshot.child("followers").getChildrenCount());
             followers.setText(str);
                String str1 = String.valueOf(snapshot.child("following").getChildrenCount());
                followings.setText(str1);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void follow(String str1){

        if(str1!=null){

            String user = FirebaseAuth.getInstance().getCurrentUser().getUid();


            if(follow.getText().toString().equals("follow")){
                FirebaseDatabase.getInstance().getReference().child("Follow").child(user).child("following")
                        .child(str1).setValue(true);
                FirebaseDatabase.getInstance().getReference().child("Follow").child(str1).child("followers")
                        .child(user).setValue(true);
                follow.setText("following");

            }
            else{
                FirebaseDatabase.getInstance().getReference().child("Follow").child(user).child("following")
                        .child(str1).removeValue();
                FirebaseDatabase.getInstance().getReference().child("Follow").child(str1).child("followers")
                        .child(user).removeValue();
                follow.setText("follow");

            }
        }



    }

    public void getImage(String str){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(str);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {
                username.setText(snapshot.child("username").getValue().toString());

                Glide.with(Profile.this).load(snapshot.child("imageurl").getValue().toString()).into(image_profile);
            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {

            }
        });


        FirebaseDatabase.getInstance().getReference().child("Follow").child(str).child("followers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).exists()) {

                    follow.setText("following");
                }
                else{
                    follow.setText("follow");
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
    public void ourProfile(){

        String user = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(user);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                username.setText(snapshot.child("username").getValue().toString());

                Glide.with(Profile.this).load(snapshot.child("imageurl").getValue().toString()).into(image_profile);
            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {

            }
        });


    }

    private void readPosts(String users){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Post post = snapshot.getValue(Post.class);

                        if(post.getPublisher().equals(users)){
                            postList.add(post);
                        }

                }

                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void know_follow(String str1){
         DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Follow").child(user).child("following");
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        if(snapshot.hasChild(str1)){
                            readPosts(str1);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });
    }


}