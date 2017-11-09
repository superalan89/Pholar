package com.hooooong.pholar.view.gallery;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hooooong.pholar.R;
import com.hooooong.pholar.model.Const;
import com.hooooong.pholar.model.Photo;
import com.hooooong.pholar.util.GalleryUtil;
import com.hooooong.pholar.view.gallery.adapter.GalleryAdapter;
import com.hooooong.pholar.view.gallery.divider.GridDividerDecoration;
import com.hooooong.pholar.view.gallery.listener.GalleryListener;
import com.hooooong.pholar.view.write.WriteActivity;

import java.util.ArrayList;
import java.util.List;

public class GalleryActivity extends BaseActivity implements GalleryListener {

    private Menu menu;
    private Toolbar toolbar;
    private RecyclerView recyclerGallery;
    private GalleryAdapter galleryAdapter;
    private ProgressBar progressBar;
    private LinearLayout countLayout;
    private TextView textTitle, textCount;

    public GalleryActivity() {
        super(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE});
    }

    @Override
    public void init() {
        setContentView(R.layout.activity_gallery);

        initLayout();
        initGalleryAdapter();
        setGallery();
    }

    /*
   private void checkIntent() {
        ArrayList<Photo> selectPhotoList = (ArrayList<Photo>) getIntent().getSerializableExtra(Const.INTENT_PHOTO_LIST);
        if(selectPhotoList != null && selectPhotoList.size() > 0){
            galleryAdapter.setSelectPhotoList(selectPhotoList);
        }
    }
    */

    /**
     * Layout 초기화
     */
    private void initLayout() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        recyclerGallery = findViewById(R.id.recyclerGallery);
        progressBar = findViewById(R.id.progressBar);
        countLayout = findViewById(R.id.countLayout);
        textTitle = findViewById(R.id.textTitle);
        textCount = findViewById(R.id.textCount);
    }

    /**
     * Gallery 초기화
     */
    private void initGalleryAdapter() {
        galleryAdapter = new GalleryAdapter(this);
        recyclerGallery.setAdapter(galleryAdapter);
        recyclerGallery.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        recyclerGallery.setItemAnimator(new DefaultItemAnimator());
        recyclerGallery.addItemDecoration(new GridDividerDecoration(getResources(), R.drawable.divider_recycler_gallery));
    }


    /**
     * Gallery 사진 불러오기
     * <p>
     * Thread 로 돌린다.
     */
    @SuppressLint("StaticFieldLeak")
    private void setGallery() {
        new AsyncTask<String, Void, List<Photo>>() {
            @Override
            protected void onPreExecute() {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected List<Photo> doInBackground(String... strings) {
                return GalleryUtil.getAllPhotoPathList(GalleryActivity.this);
            }

            @Override
            protected void onPostExecute(List<Photo> result) {
                progressBar.setVisibility(View.GONE);
                galleryAdapter.setPhotoList(result);
                //checkIntent();
            }
        }.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_gallery, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_next:
                Intent intent = item.getIntent();
                if (intent != null) {
                    // 선택된 사진이 있으면 다음 Intent 로 이동한다.
                    ArrayList<Photo> selectPhotoList = galleryAdapter.getSelectPhotoList();
                    intent.putExtra(Const.INTENT_PHOTO_LIST, selectPhotoList);
                    finish();
                }
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void PhotoClick(int position) {
        Photo photo = galleryAdapter.getPhotoList().get(position);
        List<Photo> selectPhotoList = galleryAdapter.getSelectPhotoList();
        if (selectPhotoList.contains(photo)) {
            galleryAdapter.removeSelectPhotoList(photo);
        } else {
            galleryAdapter.addSelectPhotoList(photo);
        }
    }

    @Override
    public void changeView(int count) {
        if (count == 0) {
            textTitle.setVisibility(View.VISIBLE);
            countLayout.setVisibility(View.GONE);
            // icon 변경 및 intent 설정
            changeMenu(R.id.action_next, R.drawable.ic_keyboard_arrow_right_false, false);
        } else {
            textTitle.setVisibility(View.GONE);
            countLayout.setVisibility(View.VISIBLE);
            // icon 변경 및 intent 설정
            changeMenu(R.id.action_next, R.drawable.ic_keyboard_arrow_right_true, true);

            String cnt = Integer.toString(count);
            textCount.setText(cnt);
        }
    }

    @Override
    public void selectError() {
        Toast.makeText(this, "사진은 10장까지만 선택하실 수 있어요", Toast.LENGTH_SHORT).show();
    }

    /**
     * Menu Icon 이미지 변경 및 Intent 설정
     *
     * @param id      menu id 값
     * @param iconRes 변경할 Resource 값
     * @param check   intent 변경 여부
     */
    private void changeMenu(int id, int iconRes, boolean check) {
        MenuItem item = menu.findItem(id);
        item.setIcon(iconRes);
        if (check) {
            if (item.getIntent() == null) {
                item.setIntent(new Intent(GalleryActivity.this, WriteActivity.class));
            }
        } else {
            if (item.getIntent() != null) {
                item.setIntent(null);
            }
        }
    }


}
