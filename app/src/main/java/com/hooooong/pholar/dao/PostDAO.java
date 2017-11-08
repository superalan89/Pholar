package com.hooooong.pholar.dao;

import android.content.Intent;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hooooong.pholar.MainActivity;
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

    public void read(final ICallback callback) {
        final List<Post> data = new ArrayList<>();

        Query getTopNewPost = postRef.limitToFirst(DEFAULT_GET_NEWPOST);
        getTopNewPost.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Post item = snapshot.getValue(Post.class);
                    data.add(item);
                    Log.d(TAG, "read: " + item.toString());
                }

                callback.getPostFromFirebaseDB(data);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void readByPostId (final ICallback callback, String post_id) {

        Query getSinglePost = postRef.child(post_id);

        getSinglePost.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Post item = dataSnapshot.getValue(Post.class);
                    callback.getSinglePostFromFirebaseDB(item);
                    Log.d(TAG, "readByPostId: " + item.toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    // Firebase에서 Read한 결과를 리턴해주는 Interface
    public interface ICallback {
        void getPostFromFirebaseDB (List<Post> data);
        void getSinglePostFromFirebaseDB (Post item);
    }
}
