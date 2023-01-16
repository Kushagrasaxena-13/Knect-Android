package com.example.login.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.login.Model.Comment;
import com.example.login.Model.Notification_model;
import com.example.login.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.Viewholder> {


    private Context mcontext;
    private List<Notification_model> mNotify;

    private FirebaseUser firebaseUser;


    public NotificationAdapter(Context mcontext, List<Notification_model> mNotify) {
        this.mcontext = mcontext;
        this.mNotify = mNotify;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.notification_item,parent,false);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        return new NotificationAdapter.Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  NotificationAdapter.Viewholder holder, int position) {

   Notification_model notify = mNotify.get(position);


   DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(notify.getWho());

   ref.addValueEventListener(new ValueEventListener() {
       @Override
       public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
           if(snapshot.child("id").getValue().toString().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
               holder.text.setText("You" + notify.getWhat());
           }
           else {
               holder.text.setText(snapshot.child("username").getValue().toString() + notify.getWhat());

           }
           Glide.with(mcontext).load(snapshot.child("imageurl").getValue()).into(holder.profile);
           }

       @Override
       public void onCancelled(@NonNull @NotNull DatabaseError error) {

       }
   });

    }

    @Override
    public int getItemCount() {
        return mNotify.size();
    }

    public class  Viewholder extends RecyclerView.ViewHolder{

        public CircleImageView profile;
        public TextView text;

        public Viewholder(@NonNull @NotNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.text);
            profile = itemView.findViewById(R.id.profile);
        }
    }
}
