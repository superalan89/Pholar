package com.hooooong.pholar.model;

import java.io.Serializable;

/**
 * Created by Android Hong on 2017-11-07.
 */

public class Photo implements Serializable{

    private String imgPath;
    private String thumPath;

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

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Photo))
            return false;
        return  imgPath.equals(((Photo)obj).getImgPath()) ;
    }
}
