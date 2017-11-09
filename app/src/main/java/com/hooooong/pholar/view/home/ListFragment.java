package com.hooooong.pholar.view.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
    private final String TAG = getClass().getSimpleName();

    // 새로운 글을 보여주는 ViewPager와 Adapter
    private ViewPager newWriteViewPager;
    private NewWriteViewPagerAdapter newWriteViewPagerAdapter;

    // 친구를 추천해주는 ViewPager와 Adapter
    private ViewPager recommendFriendViewPager;
    private RecommendFriendViewPagerAdapter recommendFriendViewPagerAdapter;

    // 게시글을 랜덤으로 보여주는 ViewPager와 Adapter
    private RecyclerView randomWriteViewPager;
    private RandomWriteRecyclerViewAdapter randomWriteRecyclerViewAdapter;


    public ListFragment() {
        // Required empty public constructor
    }

    // Fragment 의 경우 onCreate 에서 View 의 작업을 하면 안되고
    // onCreateView 에서 View 의 작업을 해줘야 한다.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list, container, false);
        initView(view);


        // Inflate the layout for this fragment
        return view;

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void initView(View view) {
        List<String> data = new ArrayList<>();

        for (int i = 0; i < 10; i = i + 1) {
            data.add("Happy Day " + i);
        }

        newWriteViewPager = view.findViewById(R.id.newWriteViewPager);
        recommendFriendViewPager = view.findViewById(R.id.recommedFriendViewPager);
        randomWriteViewPager = view.findViewById(R.id.randomWriteViewPager);

        // Adapter 생성
        newWriteViewPagerAdapter = new NewWriteViewPagerAdapter(view.getContext(), data);
        recommendFriendViewPagerAdapter = new RecommendFriendViewPagerAdapter(view.getContext(), data);
        randomWriteRecyclerViewAdapter = new RandomWriteRecyclerViewAdapter();
        randomWriteRecyclerViewAdapter.setDataAndRefresh(data);

        // ViewPager와 Adapter 연결
        newWriteViewPager.setAdapter(newWriteViewPagerAdapter);
        recommendFriendViewPager.setAdapter(recommendFriendViewPagerAdapter);
        randomWriteViewPager.setAdapter(randomWriteRecyclerViewAdapter);
        randomWriteViewPager.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }
}