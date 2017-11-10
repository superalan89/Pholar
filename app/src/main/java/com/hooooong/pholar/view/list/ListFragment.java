package com.hooooong.pholar.view.list;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hooooong.pholar.R;
import com.hooooong.pholar.dao.PostDAO;
import com.hooooong.pholar.model.Const;
import com.hooooong.pholar.model.Post;
import com.hooooong.pholar.view.list.adapter.NewPostAdapter;
import com.hooooong.pholar.view.list.adapter.PostAdapter;

import java.util.List;

/**
 * Created by Android Hong on 2017-11-06.
 */

public class ListFragment extends Fragment implements PostDAO.ICallback {
    private final String TAG = getClass().getSimpleName();

    // 새로운 글을 보여주는 ViewPager와 Adapter
    private ViewPager newPostViewPager;
    private NewPostAdapter newPostAdapter;

    // 게시글을 랜덤으로 보여주는 ViewPager와 Adapter
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
        newPostViewPager = view.findViewById(R.id.newWriteViewPager);
        newPostViewPager.setClipToPadding(false);
        newPostViewPager.setPadding(200, 40, 200, 0);
        newPostViewPager.setPageMargin(100);

        postRecyclerView = view.findViewById(R.id.postRecyclerView);
    }

    private void setPostAdapter() {
        // Adapter 생성
        postAdapter = new PostAdapter();
        postRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        postRecyclerView.setAdapter(postAdapter);
        loadPostData();
    }

    private void setNewPostAdapter(List<Post> data) {
        newPostAdapter = new NewPostAdapter(context, data);
        newPostViewPager.setAdapter(newPostAdapter);
        newPostViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position < Const.NEW_PHOTO_COUNT)        //1번째 아이템에서 마지막 아이템으로 이동하면
                    newPostViewPager.setCurrentItem(position + Const.NEW_PHOTO_COUNT, false); //이동 애니메이션을 제거 해야 한다
                else if (position >= Const.NEW_PHOTO_COUNT * 2)     //마지막 아이템에서 1번째 아이템으로 이동하면
                    newPostViewPager.setCurrentItem(position - Const.NEW_PHOTO_COUNT, false);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        newPostViewPager.setCurrentItem(Const.NEW_PHOTO_COUNT);
    }

    private void loadPostData() {
        PostDAO.getInstance().read(this);
    }


    @Override
    public void getPostFromFirebaseDB(List<Post> data) {
        postAdapter.setDataAndRefresh(data);
        setNewPostAdapter(data);
    }

    @Override
    public void getSinglePostFromFirebaseDB(Post item) {

    }
}