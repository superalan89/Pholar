package com.hooooong.pholar.view.gallery.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hooooong.pholar.R;
import com.hooooong.pholar.model.Photo;
import com.hooooong.pholar.view.gallery.listener.GalleryListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by woong on 2015. 10. 20..
 */
public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.PhotoViewHolder> {

    private Context context;
    private List<Photo> photoList = new ArrayList<>();
    private ArrayList<Photo> selectPhotoList;
    private GalleryListener galleryListener;

    /**
     * PhotoList 반환
     *
     * @return
     */
    public List<Photo> getPhotoList() {
        return photoList;
    }

    /**
     * 선택된 PhotoList 반환
     *
     * @return
     */
    public ArrayList<Photo> getSelectPhotoList() {
        return selectPhotoList;
    }


    /**
     * 선택한 Photo 추가하기
     *
     * @param photo
     */
    public void addSelectPhotoList(Photo photo) {
        if (selectPhotoList.size() < 10) {
            // 10 보다 작을 때
            selectPhotoList.add(photo);
            notifyItemChanged(photoList.indexOf(photo));
            galleryListener.changeView(selectPhotoList.size());
        } else {
            // 10보다 클 때
            galleryListener.selectError();
        }
    }

    /**
     * 선택한 Photo 지우기
     *
     * @param photo
     */
    public void removeSelectPhotoList(Photo photo) {
        selectPhotoList.remove(photo);
        notifyDataSetChanged();
        galleryListener.changeView(selectPhotoList.size());

    }

    /**
     * 선택한 Photo Setting
     *
     * @param selectPhotoList
     */
    /*
    public void setSelectPhotoList(ArrayList<Photo> selectPhotoList) {
        this.selectPhotoList = selectPhotoList;
        notifyDataSetChanged();
        //galleryListener.changeView(selectPhotoList.size());
    }
    */

    /**
     * 생성자
     *
     * @param context
     */
    public GalleryAdapter(Context context) {
        this.context = context;
        this.selectPhotoList = new ArrayList<>();
        this.galleryListener = (GalleryListener) context;
    }

    public void setPhotoList(List<Photo> photoList) {
        this.photoList = photoList;
        notifyDataSetChanged();
    }

    /**
     * 레이아웃을 만들어서 Holer에 저장
     *
     * @param viewGroup
     * @param viewType
     * @return
     */
    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_gallery_photo, viewGroup, false);
        return new PhotoViewHolder(view);
    }

    /**
     * listView getView 를 대체
     * 넘겨 받은 데이터를 화면에 출력하는 역할
     *
     * @param viewHolder
     * @param position
     */
    @Override
    public void onBindViewHolder(PhotoViewHolder viewHolder, int position) {
        Photo photo = photoList.get(position);

        viewHolder.setImageView(photo.getImgPath());
        viewHolder.setPosition(position);

        if (selectPhotoList.contains(photo)) {
            viewHolder.setLayout(View.VISIBLE);
            viewHolder.setTextNumber(selectPhotoList.indexOf(photo) + 1);
        } else {
            viewHolder.setLayout(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return photoList.size();
    }


    /**
     * 뷰 재활용을 위한 viewHolder
     */
    public class PhotoViewHolder extends RecyclerView.ViewHolder {
        private int position;
        private ImageView imgPhoto;
        private RelativeLayout layoutSelect;
        private TextView textNumber;

        PhotoViewHolder(View itemView) {
            super(itemView);
            imgPhoto = itemView.findViewById(R.id.imgPhoto);
            layoutSelect = itemView.findViewById(R.id.layoutSelect);
            textNumber = itemView.findViewById(R.id.textNumber);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (galleryListener != null) {
                        galleryListener.PhotoClick(position);
                    }
                }
            });
        }

        void setTextNumber(int number) {
            textNumber.setText(number + "");
        }

        void setImageView(String path) {
            Glide.with(context)
                    .load(path)
                    .centerCrop()
                    .crossFade()
                    .into(imgPhoto);
        }

        void setLayout(int layout) {
            if (layout == View.VISIBLE) {
                layoutSelect.setVisibility(View.VISIBLE);
            } else {
                layoutSelect.setVisibility(View.INVISIBLE);
            }
        }

        void setPosition(int position) {
            this.position = position;
        }
    }
}

