package com.hooooong.pholar.view.mypage;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hooooong.pholar.R;
import com.hooooong.pholar.model.PostThumbnail;
import com.hooooong.pholar.view.home.DetailActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by quf93 on 2017-11-09.
 */

public class MypageAdapter extends RecyclerView.Adapter<MypageAdapter.MypageHolder> {
    public MypageAdapter() {
    }

    Context context;
    public MypageAdapter(Context context) {
        this.context = context;
    }

    @Override
    public MypageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mypage, parent, false);
        return new MypageHolder(view);
    }

    @Override
    public void onBindViewHolder(MypageHolder holder, int position) {
        if(keys != null  && keys.length != 0) {
            Glide.with(context).load(Uri.parse(map.get(keys[position]).first_pic_path)).centerCrop().into(holder.postItem);
            int count_picture = Integer.valueOf(map.get(keys[position]).count_picture);
            if(count_picture != 1) {
                holder.item_tv.setText("+"+(count_picture-1));
            }
            holder.post_id = keys[position];
        }
    }

    @Override
    public int getItemCount() {
        return map.size();
        // map.size();
    }

    Map<String, PostThumbnail> map = new HashMap<>();
    String[] keys;
    public void dataRefreshing(Map<String, PostThumbnail> newData) {
        map.clear();
        map.putAll(newData);

        keys = new String[map.keySet().size()];
        map.keySet().toArray(keys);
        notifyDataSetChanged();
    }

    class MypageHolder extends RecyclerView.ViewHolder {
        ImageView postItem;
        TextView item_tv;
        String post_id;
        public MypageHolder(final View itemView) {
            super(itemView);
            postItem = itemView.findViewById(R.id.post_item);
            item_tv = itemView.findViewById(R.id.item_tv);
            postItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), DetailActivity.class);
                    if(post_id != null)
                        intent.putExtra("post_id", post_id);
                    view.getContext().startActivity(intent);
                }
            });
        }
    }
}
