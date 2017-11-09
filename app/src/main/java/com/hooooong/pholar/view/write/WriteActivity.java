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

import com.bumptech.glide.Glide;
import com.hooooong.pholar.R;
import com.hooooong.pholar.model.Const;
import com.hooooong.pholar.model.Photo;
import com.hooooong.pholar.util.DialogUtil;

import java.util.ArrayList;
import java.util.List;

public class WriteActivity extends AppCompatActivity {

    private List<View> photoViewList;
    private LinearLayout photoLayout;
    private EditText editContent;
    private Toolbar toolbar;
    private ArrayList<Photo> photoList;

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
                // 글 작성시 넘기기
                // storage 에 upload 하고 database 업로드까지 해야 한다.
                break;
            case android.R.id.home:
                DialogUtil.exitDialog("포스팅을 중단하시겠어요?", "현재 작성하고 계신 포스트는 다시 작성해야 할지도 몰라요.",this,true );
                break;
        }
       return super.onOptionsItemSelected(item);
    }

    /**
     * 뒤로가기 버튼 재정의
     *
     */
    @Override
    public void onBackPressed() {
        DialogUtil.exitDialog("포스팅을 중단하시겠어요?", "현재 작성하고 계신 포스트는 다시 작성해야 할지도 몰라요.",this,true );
    }


}
