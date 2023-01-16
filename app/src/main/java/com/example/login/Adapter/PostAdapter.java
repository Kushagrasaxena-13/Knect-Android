package com.example.login.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.login.Comment_Activity;
import com.example.login.Model.Post;
import com.example.login.Model.User;
import com.example.login.Profile;
import com.example.login.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Timer;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    public Context mcontext;
    public List<Post> mpost;
    int times;



    private FirebaseUser firebaseUser;

    public PostAdapter(Context context, List<Post> mpost) {
        this.mcontext = context;
        this.mpost = mpost;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mcontext).inflate(R.layout.post_item,parent,false);
        times=0;
        return new PostAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {

      firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
      Post post = mpost.get(i);


      Glide.with(mcontext).load(post.getPostimage()).into(holder.post_image);

      if(post.getDescription().equals("")){
          holder.description.setVisibility(View.GONE);
      }

      else{
          holder.description.setVisibility(View.VISIBLE);
          holder.description.setText(post.getDescription());
      }
      publisherinfo(holder.image_profile,holder.username,holder.publisher,post.getPublisher());

      isliked(post.getPostid(),holder.like);
      noliked(holder.likes,post.getPostid());
      getcomments(post.getPostid(),holder.comments);

      holder.like.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              if(holder.like.getTag().equals("like"))
              {
                  FirebaseDatabase.getInstance().getReference().child("Likes")
                          .child(post.getPostid())
                          .child(firebaseUser.getUid()).setValue(true);
              }
              else {
                  FirebaseDatabase.getInstance().getReference().child("Likes")
                          .child(post.getPostid())
                          .child(firebaseUser.getUid()).removeValue();
              }
          }

      });

      holder.post_image.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              times=times+1;
              String first =null;
              if(times==1) {
                   first = post.getPostid();
              }

              if(times==2){

                  if(first.equals(post.getPostid())) {
                      FirebaseDatabase.getInstance().getReference().child("Likes")
                              .child(post.getPostid())
                              .child(firebaseUser.getUid()).setValue(true);
                      times = 0;
                  }

                  else {
                      times =0;
                  }


              }
          }
      });

      holder.username.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
//              AppCompatActivity activity = (AppCompatActivity)mcontext;
////              activity.getSupportFragmentManager().beginTransaction().replace(R.id.)
//            Intent intent = new Intent(mcontext,profile.class);
//              Bundle bundle = new Bundle();
//              bundle.putString("username",holder.username.getText().toString());
//              intent.putExtras(bundle);
//              mcontext.startActivity(intent;

              Intent intent = new Intent(mcontext, Profile.class);
              intent.putExtra("username",post.getPublisher());

              mcontext.startActivity(intent);

          }
      });


      holder.comment.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent intent = new Intent(mcontext, Comment_Activity.class);
              intent.putExtra("postid",post.getPostid());
              intent.putExtra("publisherid",post.getPublisher());

              mcontext.startActivity(intent);


          }
      });


        holder.comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext, Comment_Activity.class);
                intent.putExtra("postid",post.getPostid());
                intent.putExtra("publisherid",post.getPublisher());

                mcontext.startActivity(intent);

            }
        });


       }

    @Override
    public int getItemCount() {
        return mpost.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public  CircleImageView image_profile;
        public ImageView post_image,like,comment,save;
        public TextView username,likes,publisher,description,comments;
        public android.app.FragmentManager manager ;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image_profile = itemView.findViewById(R.id.image_profile);
            post_image = itemView.findViewById(R.id.postimage);
            like = itemView.findViewById(R.id.like);
            comment = itemView.findViewById(R.id.comment);
            username = itemView.findViewById(R.id.username);
            likes = itemView.findViewById(R.id.no_of_likes);
            publisher = itemView.findViewById(R.id.publisher);
            description = itemView.findViewById(R.id.description);
            comments = itemView.findViewById(R.id.no_of_comments);





        }

    }

    private void  publisherinfo(CircleImageView image_profile, TextView username, TextView publisher, String userid){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                assert user != null;
                Glide.with(mcontext).load(user.getImageurl()).into(image_profile);
                username.setText(user.getUsername());
                publisher.setText(user.getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void isliked(String postId,ImageView imageView){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Likes")
                .child(postId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(firebaseUser.getUid()).exists()){
                    imageView.setImageResource(R.drawable.heart_yes);
                    imageView.setTag("liked");
                }
                else {
                    imageView.setImageResource(R.drawable.heart);
                    imageView.setTag("like");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void noliked(TextView likes,String postid){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Likes")
                .child(postid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount()>0){
                    likes.setVisibility(View.VISIBLE);
                    likes.setText(dataSnapshot.getChildrenCount()+" likes");
                }
                else{
                    likes.setVisibility(View.GONE);
                }
                }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void getcomments(String postid,final TextView comments){
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Comments").child(postid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getChildrenCount()>0) {
                    comments.setText("View All " + snapshot.getChildrenCount() + " comments");
                }
                else{
                    comments.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }



}