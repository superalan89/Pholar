package com.hooooong.pholar.view.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hooooong.pholar.R;
import com.hooooong.pholar.dao.PostDAO;
import com.hooooong.pholar.model.Post;

import java.util.List;

/**
 * Created by Android Hong on 2017-11-06.
 */

public class SearchFragment extends android.support.v4.app.Fragment {
    private final String TAG = getClass().getSimpleName();

    private PostDAO postDAO;

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
        getPostFromFirebaseDB();

        Toast.makeText(view.getContext(), "Test", Toast.LENGTH_SHORT).show();
        return view;
    }

    private void init() {
        postDAO = PostDAO.getInstance();
    }


    private void getPostFromFirebaseDB() {
        List<Post> data = postDAO.read();

        Log.e("heepie", data + "");
        for (Post item : data) {
            Log.d(TAG, "getPostFromFirebaseDB: " + item.toString());
        }
    }
}