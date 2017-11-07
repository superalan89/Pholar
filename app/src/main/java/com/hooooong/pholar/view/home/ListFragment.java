package com.hooooong.pholar.view.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hooooong.pholar.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android Hong on 2017-11-06.
 */

public class ListFragment extends Fragment {

    private ViewPager newWriteViewPager;
    private NewWriteViewPagerAdapter adapter;

    public ListFragment() {
        // Required empty public constructor
    }

    // Fragment 의 경우 onCreate 에서 View 의 작업을 하면 안되고
    // onCreateView 에서 View 의 작업을 해줘야 한다.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list, container, false);

        List<String> data = new ArrayList<>();

        for (int i=0; i<10; i=i+1) {
            data.add("Happy Day " + i);
        }

        newWriteViewPager = view.findViewById(R.id.newWriteViewPager);
        adapter = new NewWriteViewPagerAdapter(view.getContext(), data);

        newWriteViewPager.setAdapter(adapter);
        // Inflate the layout for this fragment
        return view;

    }

    @Override
    public void onStart() {
        super.onStart();
    }
}