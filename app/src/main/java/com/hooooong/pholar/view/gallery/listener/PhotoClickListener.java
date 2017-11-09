package com.hooooong.pholar.view.gallery.listener;

import com.hooooong.pholar.view.gallery.adapter.GalleryAdapter;

/**
 * Created by Android Hong on 2017-11-07.
 */

public interface PhotoClickListener {
    void PhotoClick(GalleryAdapter.PhotoViewHolder photoViewHolder, int position);
}
