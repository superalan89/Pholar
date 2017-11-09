package com.hooooong.pholar.dao;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.hooooong.pholar.model.Comment;
import com.hooooong.pholar.model.Like;
import com.hooooong.pholar.model.Photo;
import com.hooooong.pholar.model.Post;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

    // 최신글 100개 글을 읽어오는 메소드
    public void read(final ICallback callback) {
        final List<Post> data = new ArrayList<>();

        Query getTopNewPost = postRef.limitToFirst(DEFAULT_GET_NEWPOST);
        getTopNewPost.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Post item = snapshot.getValue(Post.class);
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

                    // For Test
                    List<Photo> photoList = getNestedClass(item.photo, Photo.class);
                    List<Comment> commentList = getNestedClass(item.comment, Comment.class);
                    List<Like> likeList = getNestedClass(item.like, Like.class);

//                    Log.d("heepie", photoList.get(0).toString());
//                    Log.d("heepie", commentList.get(0).toString());
//                    Log.d("heepie", likeList.get(0).toString());

                    item.setPhotoList(photoList);
                    item.setCommentList(commentList);
                    item.setLikeList(likeList);

                    callback.getSinglePostFromFirebaseDB(item);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    // Firebase Object -> Json -> Java Object
    private <U> List<U> getNestedClass(Map<String, Map<String, String>> target, Class<U> CLASS) {
        List<U> ret = new ArrayList<>();

        HashMap<String, String> hashMap = new HashMap<>();
        Gson gson = new Gson();
        JSONObject jsonObject;

        Iterator<String> ids = target.keySet().iterator();

        while(ids.hasNext()) {
            String id = ids.next();
            Map<String, String> map2 = target.get(id);
            Iterator<String> keys = map2.keySet().iterator();

            while(keys.hasNext()) {
                String key = keys.next();
                Log.e(TAG, "\t\tKey: " + key + " Value: " + map2.get(key));
                hashMap.put(key, map2.get(key));
            }

            jsonObject = getJsonStringFromMap(hashMap);
            Log.d("heepie", jsonObject.toString());
            ret.add(gson.fromJson(jsonObject.toString(), CLASS));

        }

        return ret;
    }

    // Map -> JSON 객체로 변경해주는 메소드
    public JSONObject getJsonStringFromMap(Map<String, String> hashMap) {

        JSONObject json = new JSONObject();
        for( Map.Entry<String, String> entry : hashMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            try {
                json.put(key, value);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return json;
    }

    // Firebase에서 Read한 결과를 리턴해주는 Interface
    public interface ICallback {
        void getPostFromFirebaseDB (List<Post> data);
        void getSinglePostFromFirebaseDB (Post item);
    }
}
