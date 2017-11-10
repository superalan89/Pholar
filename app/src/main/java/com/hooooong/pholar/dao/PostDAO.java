package com.hooooong.pholar.dao;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hooooong.pholar.model.Comment;
import com.hooooong.pholar.model.Photo;
import com.hooooong.pholar.model.Post;
import com.hooooong.pholar.model.PostThumbnail;
import com.hooooong.pholar.model.User;
import com.hooooong.pholar.noti.SendNotification;
import com.hooooong.pholar.util.DateUtil;
import com.hooooong.pholar.view.write.listener.WriteListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Heepie on 2017. 11. 8..
 * Post 객체와 Firebase를 연결하는 클래스
 */

public class PostDAO {
    private final String TAG = getClass().getSimpleName();
    private final int DEFAULT_GET_NEWPOST = 100;
    // 좋아요 상태
    private boolean likeStatus;

    private int count = 0;

    // For Singleton Pattern
    private static PostDAO instance;

    private FirebaseDatabase database;
    private DatabaseReference postRef;
    private DatabaseReference userRef;
    private StorageReference storageRef;

    public static PostDAO getInstance() {
        if (instance == null)
            instance = new PostDAO();
        return instance;
    }

    private PostDAO() {
        database = FirebaseDatabase.getInstance();
        postRef = database.getReference("post");
        userRef = database.getReference("user");
        storageRef = FirebaseStorage.getInstance().getReference();
    }

    /**
     * 글 작성
     * 1차적으로 Storage 에 사진 정보를 넣는다.
     *
     * @param listener
     * @param path
     * @param uid
     * @param info
     */
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
                            info.getPhoto().get(j).storage_path = taskSnapshot.getDownloadUrl().toString();
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
     * 실질적인 POST 정보들을 Database 에 넣는다.
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

        PostThumbnail postThumbnail = new PostThumbnail();
        postThumbnail.count_picture = Integer.toString(info.getPhoto().size());
        postThumbnail.first_pic_path = info.getPhoto().get(0).storage_path;

        // User 에 자기 글 올리기
        userRef.child(info.user.user_id).child("post_thumbnail").child(postKey).setValue(postThumbnail);

    }

    /**
     * 좋아요 누를 시
     *
     * @param post_id
     * @param user
     */
    public void onLikeClick(String post_id, final FirebaseUser user) {

        postRef.child(post_id).runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Post post = mutableData.getValue(Post.class);
                if (post == null) {
                    return Transaction.success(mutableData);
                }

                if(post.like != null){
                    // 좋아요가 있고,
                    if(post.like.containsKey(user.getUid())){

                        // 자신이 누른게 있으면
                        post.like.remove(user.getUid());
                    }else{
                        // 자신이 누른게 없으면
                        likeStatus = true;
                        post.like.put(user.getUid(), true);
                    }
                }else{
                    // 좋아요가 없고
                    likeStatus = true;
                    Map<String, Boolean> likeMap = new HashMap<>();
                    likeMap.put(user.getUid(), true);
                    post.like = likeMap;
                }

               // Set value and report transaction success
                mutableData.setValue(post);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
                if(likeStatus){
                    Post post = dataSnapshot.getValue(Post.class);
                    getUserToken(post, user.getDisplayName());
                    likeStatus = false;
                }
                // Notification 을 날려준다.
            }
        });
    }

    private void getUserToken(final Post post, final String nickName){
        // 1. UserId 의 Token 값을 가져온다.
        userRef.child(post.user.user_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                String token = user.token;

                // Like Notification 보내기
                SendNotification.sendLikeNotification(post.getPhoto().get(0).storage_path, nickName, token);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // 실패
            }
        });
    }

    // 최신글 100개 글을 읽어오는 메소드
    public void read(final ICallback callback) {
        final List<Post> data = new ArrayList<>();
        Query getTopNewPost = postRef.limitToFirst(DEFAULT_GET_NEWPOST);

        getTopNewPost.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                data.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Post item = snapshot.getValue(Post.class);
                    setInnerObject(dataSnapshot, item);
                    data.add(item);
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
                list.add(photo);
            }
            item.setPhoto(list);
        } else if (dataSnapshot.hasChild("comment")) {
            DataSnapshot photoSnapshot = dataSnapshot.child("comment");
            List<Comment> list = new ArrayList<>();

            for(DataSnapshot data : photoSnapshot.getChildren()){
                Comment comment = data.getValue(Comment.class);
                list.add(comment);
            }

            item.setComment(list);
        }
    }

    // Firebase에서 Read한 결과를 리턴해주는 Interface
    public interface ICallback {
        void getPostFromFirebaseDB(List<Post> data);

        void getSinglePostFromFirebaseDB(Post item);
    }
}
