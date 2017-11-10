package com.hooooong.pholar.view.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hooooong.pholar.R;
import com.hooooong.pholar.dao.PostDAO;
import com.hooooong.pholar.model.Comment;
import com.hooooong.pholar.model.Photo;
import com.hooooong.pholar.model.Post;
import com.hooooong.pholar.view.comment.CommentActivity;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetailActivity extends AppCompatActivity implements PostDAO.ICallback {
    private PostDAO postDAO;
    private String post_id;
    private CircleImageView detailProfile;
    private CircleImageView detailCommenterProfile;
    private TextView detailId;
    private TextView detailTime;
    private TextView detailContent;

    private LinearLayout detailPicLayout;
    private LinearLayout detailCommentLayout;

    private RelativeLayout progressBarLayout;
    private TextView commentWriterId;

    private Post post;
    private EditText commentContent;
    private Toolbar toolbar;
    private TextView commentDate;
    private String flag;

    private FirebaseUser mUser;
    private TextView textLikeCount;
    private TextView textCommentCount;
    private ImageView imgLike;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        /*RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.invalidate();
        recyclerView.setAdapter(new CustomRecyclerViewAdapter());*/

        post_id = getIntent().getStringExtra("post_id");
        flag = getIntent().getStringExtra("flag");

        if ("comment".equals(flag)) {
            goComment();
        } else {
            mUser = FirebaseAuth.getInstance().getCurrentUser();
            initView();
            init();
            initListener();
        }
        initView();
    }

    private void initListener() {
        imgLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostDAO.getInstance().onLikeClick(post_id, FirebaseAuth.getInstance().getCurrentUser());

            }
        });
    }


    private void init() {
        postDAO = PostDAO.getInstance();

//        isShowProgressBar(true);
        postDAO.readByPostId(this, post_id);
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

        setDataToScreen(post);

        String user_id = post.user.user_id;
    }

    private void setDataToScreen(Post post) {
        String content = post.post_content;
        String date = post.date;
        List<Photo> photoList = post.photo;
        List<Comment> commentList = post.comment;

        String nickname = post.user.nickname;
        String profile_path = post.user.profile_path;


        Glide.with(this)
                .load(profile_path)
                .into(detailProfile);

        detailId.setText(nickname);
        detailContent.setText(content);
        detailTime.setText(date);

//        Log.e("heepie3", post.comment.size() + "  " + post.like.size());

        if (post.comment == null)
            textCommentCount.setText(0 + "");
        else
            textCommentCount.setText(post.comment.size() + "");
        if (post.like == null) {
            textLikeCount.setText(0 + "");
            imgLike.setImageResource(R.drawable.ic_favorite_border);
        }
        else {
            textLikeCount.setText(post.like.size() + "");
            // Like 가 있으면
            // 현재 Login 한 사람의 ID
            String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
            if(post.like.containsKey(user_id)){
                imgLike.setImageResource(R.drawable.ic_favorite);
            }else{
                imgLike.setImageResource(R.drawable.ic_favorite_border);
            }
        }


        setPhotoToView(photoList);
    }

    private void setPhotoToView(List<Photo> photoList) {
        if(detailPicLayout.getChildCount() != photoList.size()) {
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
        }
    }

    private void initView() {
        detailProfile = findViewById(R.id.detail_profile);
        detailId = findViewById(R.id.detail_id);
        detailTime = findViewById(R.id.detail_time);
        detailContent = findViewById(R.id.detail_content);
        detailPicLayout = findViewById(R.id.detail_pic_layout);
        progressBarLayout = findViewById(R.id.progressBarLayout);
        commentWriterId = findViewById(R.id.comment_writer_id);
        detailCommenterProfile = findViewById(R.id.comment_writer_profile);
        commentContent = findViewById(R.id.comment_content);
        detailCommentLayout = findViewById(R.id.detail_pic_layout);
        commentDate = findViewById(R.id.comment_date);
        textLikeCount = findViewById(R.id.detail_textLikeCount);
        textCommentCount = findViewById(R.id.detail_textCommentCount);
        imgLike = (ImageView) findViewById(R.id.imgLike);
    }

    public void goCommentActivity(View view) {
        goComment();
    }

    private void goComment() {
        Intent intent = new Intent(this, CommentActivity.class);
        intent.putParcelableArrayListExtra("commentList", new ArrayList<Comment>(post.getComment()));
        intent.putExtra("post_id", post_id);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }
}
