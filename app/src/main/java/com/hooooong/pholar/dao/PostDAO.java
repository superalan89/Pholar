package com.hooooong.pholar.dao;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hooooong.pholar.model.Post;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Heepie on 2017. 11. 8..
 * Post 객체와 Firebase를 연결하는 클래스
 */

public class PostDAO {
    private final String TAG = getClass().getSimpleName();
    private final int DEFAULT_GET_NEWPOST = 100;

    // For Singleton Pattern
    private static PostDAO instance;

    private FirebaseDatabase database;
    private DatabaseReference postRef;

    public static PostDAO getInstance() {
        if (instance == null)
            instance = new PostDAO();
        return instance;
    }

    private PostDAO() {
        database = FirebaseDatabase.getInstance();
        postRef = database.getReference("post");
    }

    public void create(Post info) {
        // insert the Data

    }

    public List<Post> readALL () {
        List<Post> tmp = new ArrayList<>();

        return tmp;
    }

    public List<Post> read() {
        final List<Post> data = new ArrayList<>();

        Query getTopNewPost = postRef.limitToFirst(DEFAULT_GET_NEWPOST);
        getTopNewPost.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Post item = snapshot.getValue(Post.class);
                    data.add(item);
                    Log.d(TAG, "onDataChange: " + item.toString());
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return data;
    }
}
