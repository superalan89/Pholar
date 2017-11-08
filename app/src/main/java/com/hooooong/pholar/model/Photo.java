package com.hooooong.pholar.model;

/**
 * Created by Android Hong on 2017-11-07.
 */

public class Photo {
    public String photo_id;
    public String explain;
    public String storage_path;

    public String imgPath;
    public String thumPath;

    public Photo(String imgPath, String thumPath) {
        this.imgPath = imgPath;
        this.thumPath = thumPath;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getThumPath() {
        return thumPath;
    }

    public void setThumPath(String thumPath) {
        this.thumPath = thumPath;
    }
}
