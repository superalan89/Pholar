package com.hooooong.pholar.view.list.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.hooooong.pholar.model.Photo;
import com.hooooong.pholar.view.list.PostPhotoView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android Hong on 2017-11-09.
 */

public class PostPhotoAdapter extends PagerAdapter {

    private List<View> views;

    public PostPhotoAdapter(Context context, List<Photo> photoList) {
        views = new ArrayList<>(photoList.size());
        for(Photo photo : photoList){
            View view = new PostPhotoView(context, photo);
            views.add(view);
        }
    }

    @Override
    public int getCount() {
        return views.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = views.get(position);
        container.addView(view);
        return view;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
        container.removeView((View)object);
    }
}
