package com.hooooong.pholar.view.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hooooong.pholar.R;
import com.hooooong.pholar.dao.PostDAO;
import com.hooooong.pholar.model.Comment;
import com.hooooong.pholar.model.Photo;
import com.hooooong.pholar.model.Post;
import com.hooooong.pholar.util.DateUtil;
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

    private FirebaseUser mUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        /*RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.invalidate();
        recyclerView.setAdapter(new CustomRecyclerViewAdapter());*/

        post_id = getIntent().getStringExtra("post_id");
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        initView();
        init();
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
//        Log.e("heepie1", "getSinglePostFromFirebaseDB: " + user_id);
//        userDAO.readByUserId(DetailActivity.this, user_id);
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

        setPhotoToView(photoList);
        setCommentToView(commentList);

        commentWriterId.setText(mUser.getDisplayName());
        Glide.with(this)
                .load(mUser.getPhotoUrl())
                .into(detailCommenterProfile);
    }

    private void setPhotoToView (List<Photo> photoList) {
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

    private void setCommentToView (List<Comment> commentList) {
        for (int j=0; j<commentList.size(); j=j+1) {
            View view = LayoutInflater.from(this).inflate(R.layout.item_read_comment, null);
            CircleImageView imageCommentProfile = view.findViewById(R.id.comment_writer_profile);
            TextView textCommentId = view.findViewById(R.id.comment_writer_id);
            TextView textCommentContext = view.findViewById(R.id.comment_content);
            TextView textCommentDate = view.findViewById(R.id.comment_date);

            textCommentId.setText(commentList.get(j).nickname);
            textCommentContext.setText(commentList.get(j).comment_content);
            textCommentDate.setText(commentList.get(j).comment_date);

            Glide.with(this)
                    .load(commentList.get(j).profile_path)
                    .into(imageCommentProfile);
            detailCommentLayout.addView(view);
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
    }

    public void clickedRegisterComment(View view) {
        String comment_content = commentContent.getText().toString();

        if("".equals(comment_content))
            Toast.makeText(this, "댓글을 입력해주세요.", Toast.LENGTH_SHORT).show();
        else {
            Comment comment = new Comment();
            comment.nickname = mUser.getDisplayName();
            comment.profile_path = mUser.getPhotoUrl().toString();
            comment.comment_content = comment_content;
            comment.comment_date = DateUtil.currentYMDHMSDate();
            post.getComment().add(comment);
            postDAO.updatePost(post);
            Toast.makeText(this, "id: " + post.post_id, Toast.LENGTH_SHORT).show();
        }


    }

    public void test(View view) {
        Intent intent = new Intent(this, CommentActivity.class);
        intent.putParcelableArrayListExtra("commentList",(ArrayList<Comment>)post.getComment());

        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }
}