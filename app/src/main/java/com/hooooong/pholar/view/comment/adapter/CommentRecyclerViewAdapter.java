package com.hooooong.pholar.view.comment.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hooooong.pholar.R;
import com.hooooong.pholar.model.Comment;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Heepie on 2017. 11. 11..
 */

public class CommentRecyclerViewAdapter extends RecyclerView.Adapter<CommentRecyclerViewAdapter.Holder> {
    List<Comment> data;

    public void setDataAndRefresh(List<Comment> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                                  .inflate(R.layout.item_read_comment, parent,false);
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Comment item = data.get(position);

        holder.setItem(item);
        holder.setDataToScreen();
    }

    @Override
    public int getItemCount() {
        if (data == null)
            return 0;
        return data.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        private View view;
        private Comment item;

        private TextView textCommentId;
        private TextView textCommentContext;
        private TextView textCommentDate;
        private CircleImageView imageCommentProfile;

        public Holder(View itemView) {
            super(itemView);
            view = itemView;
            textCommentId = itemView.findViewById(R.id.comment_writer_id);
            textCommentContext = itemView.findViewById(R.id.comment_content);
            textCommentDate = itemView.findViewById(R.id.comment_date);
            imageCommentProfile = itemView.findViewById(R.id.comment_writer_profile);


        }

        public void setItem(Comment item) {
            this.item = item;
        }

        public void setDataToScreen() {
            textCommentId.setText(item.nickname);
            textCommentContext.setText(item.comment_content);
            textCommentDate.setText(item.comment_date);
            Glide.with(view.getContext())
                    .load(item.profile_path)
                    .into(imageCommentProfile);
        }
    }
}
