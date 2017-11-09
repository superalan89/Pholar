package com.hooooong.pholar.model;

import java.io.Serializable;
/**
 * Created by Android Hong on 2017-11-07.
 */

public class Photo implements Serializable{
    public String photo_id;
    public String photo_explain;
    public String storage_path;

    public String imgPath;
    public String thumPath;

    public Photo() { }
    
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
    public String toString() {
        return "Photo{" +
                "photo_id='" + photo_id + '\'' +
                ", photo_explain='" + photo_explain + '\'' +
                ", storage_path='" + storage_path + '\'' +
                ", imgPath='" + imgPath + '\'' +
                ", thumPath='" + thumPath + '\'' +
                '}';
    }
  
    public boolean equals(Object obj) {
        if(!(obj instanceof Photo))
            return false;
        return  imgPath.equals(((Photo)obj).getImgPath()) ;
    }
}
