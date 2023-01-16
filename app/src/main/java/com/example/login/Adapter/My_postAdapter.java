package com.example.login.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.login.Model.Post;
import com.example.login.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class My_postAdapter extends RecyclerView.Adapter<My_postAdapter.ViewHolder>{

    public Context mcontext;
    public List<Post> mpost;

    public My_postAdapter(Context mcontext, List<Post> mpost) {
        this.mcontext = mcontext;
        this.mpost = mpost;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.my_post,parent,false);
        return new My_postAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull My_postAdapter.ViewHolder holder, int position) {
       Post post = mpost.get(position);
        Glide.with(mcontext).load(post.getPostimage()).into(holder.image_profile);
    }

    @Override
    public int getItemCount() {
        return mpost.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public com.makeramen.roundedimageview.RoundedImageView image_profile;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image_profile = itemView.findViewById(R.id.postimage);





        }

    }
}
