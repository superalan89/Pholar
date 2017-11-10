package com.hooooong.pholar.view.home;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hooooong.pholar.R;
import com.hooooong.pholar.dao.PostDAO;
import com.hooooong.pholar.dao.UserDAO;
import com.hooooong.pholar.model.Photo;
import com.hooooong.pholar.model.Post;
import com.hooooong.pholar.model.User;

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

    private LinearLayout detailPicLayout;
    private RelativeLayout progressBarLayout;
    private TextView commentWriterId;

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

//        isShowProgressBar(true);
        postDAO.readByPostId(this, post_id);

        userDAO = UserDAO.getInstance();
    }

    private void isShowProgressBar(boolean isShow) {
        if (isShow)
            progressBarLayout.setVisibility(View.VISIBLE);
        else
            progressBarLayout.setVisibility(View.GONE);

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
        detailContent.setText(content);
        detailTime.setText(date);

        for (int i = 0; i < photoList.size(); i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.item_read_photo, null);
            ImageView photoView = view.findViewById(R.id.photoView);
            TextView textComment = view.findViewById(R.id.textPhotoComment);

            Log.e("heepie1", photoList.get(i).getImgPath() + "");

            Glide.with(this)
                    .load(photoList.get(i).storage_path)
                    .into(photoView);

            if (!"".equals(photoList.get(i).photo_explain)) {
                textComment.setVisibility(View.VISIBLE);
                textComment.setText(photoList.get(i).photo_explain);
            }

            detailPicLayout.addView(view);
        }

        // 현재 사용자의 정보
        commentWriterId.setText("현재 사용자 Id");
    }

    @Override
    public void getUserFromFirebaseDB(List<User> data) {

    }

    @Override
    public void getSingleUserFromFirebaseDB(User item) {
        user = item;
        Log.e("heepie1", "getSinglePostFromFirebaseDB: " + user.nickname);

        setDataToScreen(post, user);
//        isShowProgressBar(false);
    }

    private void initView() {
        detailProfile = findViewById(R.id.detail_profile);
        detailId = findViewById(R.id.detail_id);
        detailTime = findViewById(R.id.detail_time);
        detailContent = findViewById(R.id.detail_content);
        detailPicLayout = findViewById(R.id.detail_pic_layout);
        progressBarLayout = findViewById(R.id.progressBarLayout);
        commentWriterId = findViewById(R.id.comment_writer_id);
    }

}
