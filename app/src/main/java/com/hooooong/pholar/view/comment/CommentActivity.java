package com.hooooong.pholar.view.comment;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hooooong.pholar.R;
import com.hooooong.pholar.dao.PostDAO;
import com.hooooong.pholar.model.Comment;
import com.hooooong.pholar.util.DateUtil;
import com.hooooong.pholar.view.comment.adapter.CommentRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentActivity extends AppCompatActivity {
    private LinearLayout detailCommentLayout;
    private EditText commentContent;
    private FirebaseUser mUser;
    private PostDAO postDAO;
    private String post_id;
    private int commentLength;
    private ArrayList<Comment> commentArrayList;

    private RecyclerView commentRecyclerView;
    private CommentRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        postDAO = PostDAO.getInstance();
        initView();
//        ArrayList<String> commentList = getIntent();
        commentArrayList = getIntent().getParcelableArrayListExtra("commentList");
        commentLength = commentArrayList.size();
        post_id = getIntent().getStringExtra("post_id");

//        setCommentToView(commentArrayList);

        adapter = new CommentRecyclerViewAdapter();
        adapter.setDataAndRefresh(commentArrayList);

        commentRecyclerView.setAdapter(adapter);
        commentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setCommentToView (ArrayList<Comment> commentList) {
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

    private void addCommentToView (Comment comment) {
        View view = LayoutInflater.from(this).inflate(R.layout.item_read_comment, null);
        CircleImageView imageCommentProfile = view.findViewById(R.id.comment_writer_profile);
        TextView textCommentId = view.findViewById(R.id.comment_writer_id);
        TextView textCommentContext = view.findViewById(R.id.comment_content);
        TextView textCommentDate = view.findViewById(R.id.comment_date);


        textCommentId.setText(comment.nickname);
        textCommentContext.setText(comment.comment_content);
        textCommentDate.setText(comment.comment_date);
        Glide.with(this)
                .load(comment.profile_path)
                .into(imageCommentProfile);
        detailCommentLayout.addView(view);
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

            postDAO.updateComment(post_id,commentLength++, comment);

            commentArrayList.add(comment);
            adapter.setDataAndRefresh(commentArrayList);
            commentRecyclerView.scrollToPosition(commentArrayList.size()-1);
            commentContent.setText(" ");
        }
    }

    private void initView() {
        commentContent = findViewById(R.id.comment_content);
//        detailCommentLayout = findViewById(R.id.detail_comment_layout);
        commentRecyclerView = findViewById(R.id.commentRecyclerView);
    }
}
