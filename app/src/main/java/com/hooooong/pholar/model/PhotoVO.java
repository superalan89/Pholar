package com.hooooong.pholar.model;

/**
 * Created by Android Hong on 2017-11-07.
 */

public class PhotoVO {

    private String imgPath;
    private String thumPath;

    public PhotoVO(String imgPath, String thumPath) {
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
