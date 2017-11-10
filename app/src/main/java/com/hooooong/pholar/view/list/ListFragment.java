package com.hooooong.pholar.view.list;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hooooong.pholar.R;
import com.hooooong.pholar.dao.PostDAO;
import com.hooooong.pholar.model.Post;
import com.hooooong.pholar.view.list.adapter.PostAdapter;
import com.hooooong.pholar.view.list.listener.ListListener;

import java.util.List;

/**
 * Created by Android Hong on 2017-11-06.
 */

public class ListFragment extends Fragment implements PostDAO.ICallback {

    // 게시글을 랜덤으로 보여주는 ViewPager와 Adapter
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView postRecyclerView;
    private PostAdapter postAdapter;
    private Context context;


    public ListFragment() {
        // Required empty public constructor
    }

    // Fragment 의 경우 onCreate 에서 View 의 작업을 하면 안되고
    // onCreateView 에서 View 의 작업을 해줘야 한다.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list, container, false);
        this.context = view.getContext();
        initLayout(view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void initLayout(View view) {
        initView(view);
        setPostAdapter();
    }

    private void initView(View view) {
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        postRecyclerView = view.findViewById(R.id.postRecyclerView);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadPostData();
            }
        });
    }

    private void setPostAdapter() {
        // Adapter 생성
        postAdapter = new PostAdapter(this.getActivity());
        postRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        postRecyclerView.setAdapter(postAdapter);
        loadPostData();
    }
    private void loadPostData() {
        PostDAO.getInstance().read(this);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void getPostFromFirebaseDB(List<Post> data) {
        postAdapter.setDataAndRefresh(data);
    }

    @Override
    public void getSinglePostFromFirebaseDB(Post item) {

    }

}