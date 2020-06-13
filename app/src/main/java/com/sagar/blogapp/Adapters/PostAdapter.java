package com.sagar.blogapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sagar.blogapp.Activity.PostDetailActivity;
import com.sagar.blogapp.Models.Post;
import com.sagar.blogapp.R;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>{

    Context mContext;
    List<Post> mData;

    public PostAdapter(Context mContext, List<Post> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.row_img_style,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.PostTitle.setText(mData.get(position).getTitle());
        Glide.with(mContext).load(mData.get(position).getPicture()).into(holder.PostImage);
        Glide.with(mContext).load(mData.get(position).getUserPhoto()).into(holder.PostProfile);

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView PostTitle;
        ImageView PostImage,PostProfile;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            PostTitle = itemView.findViewById(R.id.Post_title);
            PostImage = itemView.findViewById(R.id.Post_img);
            PostProfile = itemView.findViewById(R.id.Post_profile);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent postDetailActivity = new Intent(mContext, PostDetailActivity.class);
                    int position = getAdapterPosition();

                    postDetailActivity.putExtra("title",mData.get(position).getTitle());
                    postDetailActivity.putExtra("discription",mData.get(position).getDiscription());
                    postDetailActivity.putExtra("postimage",mData.get(position).getPicture());
                    postDetailActivity.putExtra("userprofile",mData.get(position).getUserPhoto());
                    postDetailActivity.putExtra("postkey",mData.get(position).getPostKey());

                    long timestamp = (long) mData.get(position).getTimestamp();
                    postDetailActivity.putExtra("postdate",timestamp);
                    mContext.startActivity(postDetailActivity);

                }
            });

        }
    }

}
