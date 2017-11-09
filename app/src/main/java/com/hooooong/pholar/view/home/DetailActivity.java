package com.hooooong.pholar.view.home;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hooooong.pholar.R;
import com.hooooong.pholar.dao.PostDAO;
import com.hooooong.pholar.dao.UserDAO;
import com.hooooong.pholar.model.Photo;
import com.hooooong.pholar.model.Post;
import com.hooooong.pholar.model.User;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity implements PostDAO.ICallback, UserDAO.ICallback {
    private PostDAO postDAO;
    private UserDAO userDAO;
    private String post_id;
    private Post post;
    private User user;
    private ImageView detailProfile;
    private TextView detailId;
    private TextView detailTime;
    private TextView detailContent;

    private RelativeLayout detailPicLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

//        post_id = getIntent().getStringExtra("post_id");

        // 테스트
        post_id = "-KyVcailcSYuomJdjcEU";


        initView();
        init();
    }

    private void init() {
        postDAO = PostDAO.getInstance();
        //showProgressBar();
        postDAO.readByPostId(this, post_id);

        userDAO = UserDAO.getInstance();
    }

    private void showProgressBar() {
    }


    @Override
    public void getPostFromFirebaseDB(List<Post> data) {

    }

    @Override
    public void getSinglePostFromFirebaseDB(Post item) {
        Log.e("heepie1", "getSinglePostFromFirebaseDB: " + item.post_id);
        post = item;
        String user_id = "YCd1Xok8M3T295cAIErJUy6iuI72";
        userDAO.readByUserId(DetailActivity.this, user_id);
    }

    private void setDataToScreen(Post post, User user) {
        String content = post.post_content;
        String date = post.date;
        List<Photo> photoList = post.photo;

        String nickname = user.nickname;
        String profile_path = user.profile_path;


        Glide.with(this)
                .load(profile_path)
                .into(detailProfile);

        detailId.setText(nickname);

        for(int i=0; i<photoList.size(); i++){
            View view = LayoutInflater.from(this).inflate(R.layout.item_write_photo, null);
            ImageView photoView = view.findViewById(R.id.photoView);
            Log.e("heepie1", photoList.get(i).getImgPath()+"");
            Glide.with(this)
                    .load(photoList.get(i).storage_path)
                    .into(photoView);
            detailPicLayout.addView(view);
        }


        detailContent.setText(content);
        detailTime.setText(date);

    }

    @Override
    public void getUserFromFirebaseDB(List<User> data) {

    }

    @Override
    public void getSingleUserFromFirebaseDB(User item) {
        user = item;
        Log.e("heepie1", "getSinglePostFromFirebaseDB: " + user.nickname);
        //hideProgressbar();
        setDataToScreen(post, user);
    }

    private void initView() {
        detailProfile = findViewById(R.id.detail_profile);
        detailId = findViewById(R.id.detail_id);
        detailTime = findViewById(R.id.detail_time);
        detailContent = findViewById(R.id.detail_content);
        detailPicLayout = findViewById(R.id.detail_pic_layout);
    }

}
