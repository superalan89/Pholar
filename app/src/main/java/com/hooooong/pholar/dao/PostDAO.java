package com.hooooong.pholar.dao;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import com.hooooong.pholar.model.User;
import com.hooooong.pholar.util.FirebaseUtil;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.hooooong.pholar.model.Post;
import com.hooooong.pholar.util.DateUtil;
import com.hooooong.pholar.view.write.listener.WriteListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Heepie on 2017. 11. 8..
 * Post 객체와 Firebase를 연결하는 클래스
 */

public class PostDAO {
    private final String TAG = getClass().getSimpleName();
    private final int DEFAULT_GET_NEWPOST = 100;

    private int count = 0;

    // For Singleton Pattern
    private static PostDAO instance;

    private FirebaseDatabase database;
    private DatabaseReference postRef;
    private StorageReference storageRef;

    public static PostDAO getInstance() {
        if (instance == null)
            instance = new PostDAO();
        return instance;
    }

    private PostDAO() {
        database = FirebaseDatabase.getInstance();
        postRef = database.getReference("post");
        storageRef = FirebaseStorage.getInstance().getReference();
    }

    public void create(final WriteListener listener, String path, String uid, final Post info) {
        for (int i = 0; i < info.getPhoto().size(); i++) {
            final int j = i;
            // 파일 업로드 경로
            Uri uri = Uri.fromFile(new File(info.getPhoto().get(i).getImgPath()));

            storageRef.child(path)                                                                  // photo
                    .child(uid)                                                                     //  ㄴ  개인 ID
                    .child(DateUtil.currentYMDDate())                                               //       ㄴ  20171212
                    .child(System.currentTimeMillis()+"")                                           //              ㄴ currentSystem.png
                    .putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            info.getPhoto().get(j).storage_path = taskSnapshot.getDownloadUrl().getPath();
                            count++;
                            if(count == info.getPhoto().size()){
                                uploadDataBase(listener, info);
                                Log.e("PostDAO", "create() 작업 성공");
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // 실패작업
                            Log.e("PostDAO", "create() 작업 실패");
                            listener.failPost();
                        }
                    });
        }
    }

    /**
     * 실질적인 POSt 정보들을 Database 에 넣는다.
     *
     * @param listener
     * @param info
     */
    private void uploadDataBase(final WriteListener listener, Post info) {
        String postKey = postRef.push().getKey();
        info.post_id = postKey;

        postRef.child(postKey).setValue(info)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // 성공작업
                        listener.successPost();
                        count = 0;
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // 실패작업
                        listener.failPost();
                    }
                });
    }


    public List<Post> readALL() {
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
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Post item = snapshot.getValue(Post.class);

                    setInnerObject(dataSnapshot, item);

                    data.add(item);
                    Log.d(TAG, "read: " + item);
                }

                Collections.reverse(data);
                callback.getPostFromFirebaseDB(data);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    // 글 ID를 통해 글 읽어오는 메소드
    public void readByPostId(final ICallback callback, String post_id) {

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
        if (dataSnapshot.hasChild("photo")) {
            DataSnapshot photoSnapshot = dataSnapshot.child("photo");

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
        } else if (dataSnapshot.hasChild("user")) {
            User user = dataSnapshot.getValue(User.class);
            item.user = user;
        }
    }

    // Firebase에서 Read한 결과를 리턴해주는 Interface
    public interface ICallback {
        void getPostFromFirebaseDB(List<Post> data);

        void getSinglePostFromFirebaseDB(Post item);
    }
}
