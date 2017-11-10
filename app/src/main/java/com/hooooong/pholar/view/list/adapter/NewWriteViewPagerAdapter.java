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
 * 새로운 글을 보여주는 Viewpager의 Adapter
 */

public class NewWriteViewPagerAdapter extends PagerAdapter {
    private final String TAG = getClass().getSimpleName();

    private Context context;
    private List<String> data;

    public NewWriteViewPagerAdapter(Context context, List<String> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(context)
                                  .inflate(R.layout.each_new_write, null);

        String title = data.get(position);

        TextView textTitle = view.findViewById(R.id.each_new_write_title);

        textTitle.setText(title);

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
        container.removeView((View)object);
    }
}


