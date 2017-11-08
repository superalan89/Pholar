package com.hooooong.pholar.view.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hooooong.pholar.R;
import com.hooooong.pholar.dao.PostDAO;
import com.hooooong.pholar.dao.UserDAO;
import com.hooooong.pholar.model.Post;
import com.hooooong.pholar.model.User;

import java.util.List;

/**
 * Created by Android Hong on 2017-11-06.
 */

public class SearchFragment extends android.support.v4.app.Fragment implements PostDAO.ICallback, UserDAO.ICallback {
    private final String TAG = getClass().getSimpleName();

    private PostDAO postDAO;
    private UserDAO userDAO;

    public SearchFragment() {
        // Required empty public constructor
    }

    // Fragment 의 경우 onCreate 에서 View 의 작업을 하면 안되고
    // onCreateView 에서 View 의 작업을 해줘야 한다.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        init();


        initDataFromFirebaseDB();

        // 테스트: post_id로 검색
//        getSinglePostFromFirebaseDB("post_adfewerwer");
        postDAO.readByPostId(this, "post_adfewerwer");
        userDAO.readByUserId(this, "user_qerdfsers");

        Toast.makeText(view.getContext(), "Test", Toast.LENGTH_SHORT).show();
        return view;
    }

    private void init() {
        postDAO = PostDAO.getInstance();
        userDAO = UserDAO.getInstance();
    }


    private void initDataFromFirebaseDB() {
        postDAO.read(this);
        userDAO.read(this);

    }

    public void getSinglePostFromFirebaseDB(String post_id) {
        postDAO.readByPostId(this, post_id);
    }



    @Override
    public void getPostFromFirebaseDB(List<Post> data) {
        // Screen에 반영

        for (Post item : data) {
            Log.d(TAG, "getPostFromFirebaseDB: " + item.toString());
        }
    }

    @Override
    public void getSinglePostFromFirebaseDB(Post item) {
        // Screen에 반영
        Log.e(TAG, "Single getPostFromFirebaseDB: " + item.toString());

        // For Test
        Log.d("heepie", item.getPhotoList().get(0).toString());
        Log.d("heepie", item.getCommentList().get(0).toString());
        Log.d("heepie", item.getLikeList().get(0).toString());
    }

    @Override
    public void getUserFromFirebaseDB(List<User> data) {
        for (User item : data) {
            Log.d(TAG, "getUserFromFirebaseDB: " + item.toString());
        }
    }

    @Override
    public void getSingleUserFromFirebaseDB(User item) {
        // Screen에 반영
        Log.e(TAG, "Single getUserFromFirebaseDB: " + item.toString());
    }
}