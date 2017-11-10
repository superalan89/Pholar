package com.hooooong.pholar.view.list;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hooooong.pholar.R;
import com.hooooong.pholar.model.Photo;

/**
 * Created by Android Hong on 2017-11-09.
 */

public class PostPhotoView extends FrameLayout {

    private Context context;
    private ImageView imgPostPhoto;
    private TextView textPhotoComment;
    private Photo photo;

    public PostPhotoView(@NonNull Context context, Photo photo) {
        super(context);
        this.context = context;
        this.photo = photo;
        initView();
    }

    private void initView(){
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_post_photo, null);
        imgPostPhoto = view.findViewById(R.id.imgPostPhoto);
        textPhotoComment = view.findViewById(R.id.textPhotoComment);
        // 로직 처리
        process();
        addView(view);
    }

    private void process(){
        Glide.with(context)
                .load(photo.storage_path)
                .centerCrop()
                .into(imgPostPhoto);

        textPhotoComment.setText(photo.photo_explain);
    }
}