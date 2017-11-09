package com.hooooong.pholar.model;

/**
 * Created by Heepie on 2017. 11. 8..
 */

public class PostThumbnail {
    public String post_id;
    public String first_pic_path;
    public String count_picture;

    @Override
    public String toString() {
        return "PostThumbnail{" +
                "post_id='" + post_id + '\'' +
                ", first_pic_path='" + first_pic_path + '\'' +
                ", count_picture='" + count_picture + '\'' +
                '}';
    }
}
