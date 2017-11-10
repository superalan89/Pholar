package com.hooooong.pholar.view.list.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hooooong.pholar.R;
import com.hooooong.pholar.model.Const;
import com.hooooong.pholar.model.Post;

import java.util.List;

/**
 * Created by Heepie on 2017. 11. 7..
 * 새로운 글을 보여주는 Viewpager의 Adapter
 */

public class NewPostAdapter extends PagerAdapter {
    private final String TAG = getClass().getSimpleName();

    private Context context;
    private List<Post> data;

    public NewPostAdapter(Context context, List<Post> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        position %= Const.NEW_PHOTO_COUNT;
        View view = LayoutInflater.from(context).inflate(R.layout.item_post_newphoto, null);
        ImageView imgNewPhoto = view.findViewById(R.id.imgNewPhoto);
        TextView textPhotoComment = view.findViewById(R.id.textPhotoComment);
        TextView textPostWriter = view.findViewById(R.id.textPostWriter);

        Post post = data.get(position);

        Glide.with(context).load( post.getPhoto().get(0).storage_path).centerCrop().into(imgNewPhoto);

        if(post.getPhoto().get(0).photo_explain != null && post.getPhoto().get(0).photo_explain.length() != 0){
            textPhotoComment.setText(post.getPhoto().get(0).photo_explain);
        }else{
            textPhotoComment.setText(post.user.nickname+" 님의 사진");
        }
        textPostWriter.setText(post.user.nickname);
        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        if (data == null)
            return 0;
        return Const.NEW_PHOTO_COUNT*3;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }
}
