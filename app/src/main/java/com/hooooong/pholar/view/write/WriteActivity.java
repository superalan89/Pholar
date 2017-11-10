package com.hooooong.pholar.view.write;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hooooong.pholar.R;
import com.hooooong.pholar.dao.PostDAO;
import com.hooooong.pholar.model.Const;
import com.hooooong.pholar.model.Photo;
import com.hooooong.pholar.model.Post;
import com.hooooong.pholar.util.DateUtil;
import com.hooooong.pholar.util.DialogUtil;
import com.hooooong.pholar.view.write.listener.WriteListener;

import java.util.ArrayList;
import java.util.List;

public class WriteActivity extends AppCompatActivity implements WriteListener {

    private List<View> photoViewList;
    private LinearLayout photoLayout;
    private RelativeLayout progressBarLayout;
    private EditText editContent;
    private Toolbar toolbar;
    private ArrayList<Photo> photoList;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        initIntent();
        initLayout();
        setImageLayout();
    }

    /**
     * 값(ImagePath) 받아오기
     */
    private void initIntent() {
        Intent intent = getIntent();
        photoList = (ArrayList<Photo>) intent.getSerializableExtra(Const.INTENT_PHOTO_LIST);
    }

    /**
     * Layout Setting
     */
    private void initLayout() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        progressBarLayout = findViewById(R.id.progressBarLayout);
        photoLayout = findViewById(R.id.photoLayout);
        editContent = findViewById(R.id.editContent);
    }


    /**
     * photoLayout 설정
     */
    private void setImageLayout() {
        photoViewList = new ArrayList<>(photoList.size());
        for(int i = 0 ; i<photoList.size(); i++){
            View view = LayoutInflater.from(this).inflate(R.layout.item_write_photo, null);
            ImageView photoView = view.findViewById(R.id.photoView);

            Glide.with(this)
                    .load(photoList.get(i).getImgPath())
                    .into(photoView);
            photoLayout.addView(view);
            photoViewList.add(view);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_ok, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_ok:
                // POST 작성
                postWrite();
                break;
            case android.R.id.home:
                DialogUtil.exitDialog("포스팅을 중단하시겠어요?", "현재 작성하고 계신 포스트는 다시 작성해야 할지도 몰라요.",this,true );
                break;
        }
       return super.onOptionsItemSelected(item);
    }

    /**
     * POST 작성
     */
    private void postWrite() {
        progressBarLayout.setVisibility(View.VISIBLE);

        /**
         * Post 정보 생성
         */
        FirebaseUser user = mAuth.getCurrentUser();
        Post info = new Post();
        info.date = DateUtil.currentYMDHMSDate();
        info.post_content = editContent.getText().toString();

        // 회원 이름 넣기 (수정해야한다.)
        info.writer = "이흥기";

        /**
         * Photo Comment 담기
         */
        for(int i = 0 ; i< photoViewList.size(); i++){
            EditText editPhotoComment = photoViewList.get(i).findViewById(R.id.editPhotoComment);
            String photoComment = editPhotoComment.getText().toString();
            photoList.get(i).photo_explain = photoComment;
        }
        /**
         * PhotoList post 에 설정
         */
        info.setPhoto(photoList);

        /**
         * 글 작성
         */
        PostDAO.getInstance().create(this,"photo",user.getUid(), info);
    }

    /**
     * 뒤로가기 버튼 재정의
     *
     */
    @Override
    public void onBackPressed() {
        DialogUtil.exitDialog("포스팅을 중단하시겠어요?", "현재 작성하고 계신 포스트는 다시 작성해야 할지도 몰라요.",this,true );
    }


    @Override
    public void successPost() {
        progressBarLayout.setVisibility(View.GONE);
        Toast.makeText(this, "축하해요. 포스팅이 완료되었어요.", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void failPost() {
        progressBarLayout.setVisibility(View.GONE);
        Toast.makeText(this, "미안해요. 포스팅이 실패하였어요.", Toast.LENGTH_SHORT).show();

    }
}
