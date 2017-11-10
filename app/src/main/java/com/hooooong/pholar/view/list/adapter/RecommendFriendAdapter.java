package com.hooooong.pholar.view.list.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hooooong.pholar.R;

import java.util.List;

/**
 * Created by Heepie on 2017. 11. 7..
 */

public class RecommendFriendAdapter extends PagerAdapter {
    private final String TAG = getClass().getSimpleName();

    Context context;
    List<String> data;

    public RecommendFriendAdapter(Context context, List<String> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(context)
                                  .inflate(R.layout.each_recommend_friend, null);

        TextView textId = view.findViewById(R.id.each_recommend_friend_id);
        textId.setText(data.get(position));

        container.addView(view);

        return view;
    }

    @Override
    public int getCount() {
        if (data == null)
            return 0;
        return data.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {


    }
}
