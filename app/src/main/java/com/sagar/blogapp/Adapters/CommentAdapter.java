package com.sagar.blogapp.Adapters;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sagar.blogapp.Models.Comment;
import com.sagar.blogapp.R;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    Context mContext;
    List<Comment> mData;

    public CommentAdapter(Context mContext, List<Comment> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.row_comment,parent,false);
        return new CommentViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Glide.with(mContext).load(mData.get(position).getUimg()).into(holder.CommentImg);
        holder.CommentName.setText(mData.get(position).getUname());
        holder.CommentDetail.setText(mData.get(position).getContent());
        holder.CommentDate.setText(timestamptostring((long) mData.get(position).getTimestamp()));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {

        ImageView CommentImg;
        TextView CommentName,CommentDetail,CommentDate;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);

            CommentImg = itemView.findViewById(R.id.comment_photo);
            CommentName = itemView.findViewById(R.id.comment_name);
            CommentDetail = itemView.findViewById(R.id.comment_text);
            CommentDate = itemView.findViewById(R.id.comment_date);

        }
    }

    private String timestamptostring(long time){

        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);
        String date = DateFormat.format("hh:mm",calendar).toString();
        return date;
    }
}
