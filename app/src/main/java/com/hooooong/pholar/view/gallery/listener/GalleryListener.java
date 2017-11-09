package com.hooooong.pholar.view.gallery.listener;

/**
 * Created by Android Hong on 2017-11-07.
 */

public interface GalleryListener {
    /**
     * ImageView 클릭 시 발생하는 Listener
     *
     * @param position
     */
    void PhotoClick(int position);

    /**
     * ImageView 클릭 시 GalleryActivity View 변환하는 Listener
     */
    void changeView(int count);

    /**
     * ImageView 클릭 시 10개가 넘어갔을 때 Error 를 보여주는 Listener
     */
    void selectError();
}
