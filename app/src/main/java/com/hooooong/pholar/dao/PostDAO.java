package com.hooooong.pholar.dao;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hooooong.pholar.model.Comment;
import com.hooooong.pholar.model.Like;
import com.hooooong.pholar.model.Photo;
import com.hooooong.pholar.model.Post;
import com.hooooong.pholar.util.FirebaseUtil;

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
        postRef.setValue(info);
    }

    public List<Post> readALL () {
        List<Post> tmp = new ArrayList<>();

        return tmp;
    }

    // 최신글 100개 글을 읽어오는 메소드
    public void read(final ICallback callback) {
        final List<Post> data = new ArrayList<>();

        Query getTopNewPost = postRef.limitToFirst(DEFAULT_GET_NEWPOST);
        getTopNewPost.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Post item = snapshot.getValue(Post.class);

                    setInnerObject(dataSnapshot, item);

                    data.add(item);
                    Log.d(TAG, "read: " + item);
                }

                callback.getPostFromFirebaseDB(data);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    // 글 ID를 통해 글 읽어오는 메소드
    public void readByPostId (final ICallback callback, String post_id) {

        Query getSinglePost = postRef.child(post_id);

        getSinglePost.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Post item = dataSnapshot.getValue(Post.class);
//                    callback.getSinglePostFromFirebaseDB(item);

                    Log.d(TAG, "readByPostId: " + item.toString());

                    setInnerObject(dataSnapshot, item);

                    callback.getSinglePostFromFirebaseDB(item);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setInnerObject(DataSnapshot dataSnapshot, Post item) {
        if (dataSnapshot.hasChild("photoList")) {
            DataSnapshot photoSnapshot = dataSnapshot.child("photoList");

            List<Photo> list = new ArrayList<>();

            for(DataSnapshot data : photoSnapshot.getChildren()){
                Photo photo = data.getValue(Photo.class);
                Log.e("heepie", "IN");
                list.add(photo);
            }

            item.setPhoto(list);
        } else if (dataSnapshot.hasChild("comment")) {
            DataSnapshot photoSnapshot = dataSnapshot.child("comment");

            List<Comment> list = new ArrayList<>();

            for(DataSnapshot data : photoSnapshot.getChildren()){
                Comment comment = data.getValue(Comment.class);
                Log.e("heepie", "IN");
                list.add(comment);
            }

            item.setComment(list);
        } else if (dataSnapshot.hasChild("like")) {
            DataSnapshot photoSnapshot = dataSnapshot.child("like");

            List<Like> list = new ArrayList<>();

            for(DataSnapshot data : photoSnapshot.getChildren()){
                Like like = data.getValue(Like.class);
                Log.e("heepie", "IN");
                list.add(like);
            }

            item.setLike(list);
        }
    }

    // Firebase에서 Read한 결과를 리턴해주는 Interface
    public interface ICallback {
        void getPostFromFirebaseDB (List<Post> data);
        void getSinglePostFromFirebaseDB (Post item);
    }
}
