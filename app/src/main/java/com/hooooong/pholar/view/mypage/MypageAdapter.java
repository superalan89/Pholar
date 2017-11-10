package com.hooooong.pholar.view.mypage;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hooooong.pholar.R;
import com.hooooong.pholar.model.PostThumbnail;
import com.hooooong.pholar.view.home.DetailActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by quf93 on 2017-11-09.
 */

public class MypageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    public MypageAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if(viewType == NO_ITEM) {
            view = LayoutInflater.from(context).inflate(R.layout.item_no_post, parent, false);
            return new NoPostHolder(view);
        }
        else {
            view = LayoutInflater.from(context).inflate(R.layout.item_mypage, parent, false);
            return new MypageHolder(view);
        }


    }

    private final int NO_ITEM = 174;
    @Override
    public int getItemViewType(int position) {
        if(map.size() == 0) {

            return NO_ITEM;
        }
        return super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof MypageHolder) {
            if (keys != null && keys.length != 0) {
                Glide.with(context).load(Uri.parse(map.get(keys[position]).first_pic_path)).centerCrop().into(((MypageHolder)holder).postItem);
                int count_picture = Integer.valueOf(map.get(keys[position]).count_picture);
                if (count_picture != 1) {
                    ((MypageHolder)holder).item_tv.setText("+" + (count_picture - 1));
                }
                ((MypageHolder)holder).post_id = keys[position];
            }
        } else if (holder instanceof NoPostHolder) {

        }
    }

    @Override
    public int getItemCount() {
        if(map.size() == 0)
            return  1;
        else
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

    class NoPostHolder extends RecyclerView.ViewHolder {

        public NoPostHolder(View itemView) {
            super(itemView);
        }
    }
}