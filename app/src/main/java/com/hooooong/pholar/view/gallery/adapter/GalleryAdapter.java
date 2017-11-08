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
import com.hooooong.pholar.view.gallery.listener.PhotoClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by woong on 2015. 10. 20..
 */
public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.PhotoViewHolder> {

    private Context context;
    private List<Photo> photoList = new ArrayList<>();
    private List<Photo> selectPhotoList;
    private PhotoClickListener onItemClickListener;

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
    public List<Photo> getSelectPhotoList() {
        return selectPhotoList;
    }


    /**
     * 선택한 Photo 지우기
     *
     * @param photo
     */
    public void removeSelectPhotoList(Photo photo) {
        selectPhotoList.remove(photo);
        notifyDataSetChanged();
    }

    /**
     * 선택한 Photo 추가하기
     *
     * @param photo
     */
    public void addSelectPhotoList(Photo photo) {
        selectPhotoList.add(photo);
        notifyDataSetChanged();
    }

    /**
     * 생성자
     *
     * @param context
     */
    public GalleryAdapter(Context context) {
        this.context = context;
        this.selectPhotoList = new ArrayList<>();
        this.onItemClickListener = (PhotoClickListener) context;
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
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_photo, viewGroup, false);
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
    public void onBindViewHolder(final PhotoViewHolder viewHolder, final int position) {
        final Photo photo = photoList.get(position);

        viewHolder.setImageView(photo.getImgPath());

        if (selectPhotoList.contains(photo)) {
            viewHolder.setLayout(View.VISIBLE);
            viewHolder.setTextNumber(selectPhotoList.indexOf(photo) + 1);
        } else {
            viewHolder.setLayout(View.INVISIBLE);
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.PhotoClick(viewHolder, position);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return photoList.size();
    }


    /**
     * 뷰 재활용을 위한 viewHolder
     */
    public class PhotoViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgPhoto;
        private RelativeLayout layoutSelect;
        private TextView textNumber;

        PhotoViewHolder(View itemView) {
            super(itemView);
            imgPhoto = itemView.findViewById(R.id.imgPhoto);
            layoutSelect = itemView.findViewById(R.id.layoutSelect);
            textNumber = itemView.findViewById(R.id.textNumber);
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
    }
}

