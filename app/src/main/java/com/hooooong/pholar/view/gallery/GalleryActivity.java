package com.hooooong.pholar.view.gallery;

import android.Manifest;
import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.hooooong.pholar.R;
import com.hooooong.pholar.model.Photo;
import com.hooooong.pholar.util.GalleryUtil;
import com.hooooong.pholar.view.gallery.adapter.GalleryAdapter;
import com.hooooong.pholar.view.gallery.divider.GridDividerDecoration;
import com.hooooong.pholar.view.gallery.listener.PhotoClickListener;

import java.util.List;

public class GalleryActivity extends BaseActivity implements PhotoClickListener {

    private Menu menu;
    private Toolbar toolbar;
    private RecyclerView recyclerGallery;
    private GalleryAdapter galleryAdapter;
    private ProgressBar progressBar;

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

    @SuppressLint("StaticFieldLeak")
    private void setGallery() {
        new AsyncTask<String, Void, List<Photo>>() {
            @Override
            protected void onPreExecute() {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected List<Photo> doInBackground(String... strings) {
                return GalleryUtil.fetchAllImages(GalleryActivity.this);
            }

            @Override
            protected void onPostExecute(List<Photo> result) {
                progressBar.setVisibility(View.GONE);
                galleryAdapter.setPhotoList(result);
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
            case R.menu.menu_gallery:

                if (true) {
                    // 선택된 사진이 있으면 다음 Intent 로 이동한다.
                    List<Photo> selectedPhotoList = galleryAdapter.getSelectPhotoList();
                    for (int i = 0; i < selectedPhotoList.size(); i++) {
                        Log.i("", ">>> selectedPhotoList   :  " + selectedPhotoList.get(i).getImgPath());
                    }
                } else {
                    // 선택된 사진이 없으면 이동하지 않는다.

                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void PhotoClick(GalleryAdapter.PhotoViewHolder photoViewHolder, int position) {
        Photo photo = galleryAdapter.getPhotoList().get(position);
        List<Photo> selectPhootoList = galleryAdapter.getSelectPhotoList();
        if (selectPhootoList.contains(photo)) {
            galleryAdapter.removeSelectPhotoList(photo);
        } else {
            galleryAdapter.addSelectPhotoList(photo);
        }
    }

    private void changeMenu(int id, int iconRes) {
        MenuItem item = menu.findItem(id);
        item.setIcon(iconRes);
    }
}
