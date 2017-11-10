package com.hooooong.pholar.view.comment;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hooooong.pholar.R;
import com.hooooong.pholar.model.Comment;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentActivity extends AppCompatActivity {
    private LinearLayout detailCommentLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        detailCommentLayout = findViewById(R.id.detail_comment_layout);

//        ArrayList<String> commentList = getIntent();
        ArrayList<Comment> commentArrayList = getIntent().getParcelableArrayListExtra("commentList");
        setCommentToView(commentArrayList);
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
}
