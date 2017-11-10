package com.hooooong.pholar.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Heepie on 2017. 11. 8..
 *
 * User가 작성한 Post의 테이블
 */

public class Post {
    public String post_id;
    public String date;
    public String post_content;
    public User user;

    public List<Photo> photo;
    public List<Comment> comment;
    public Map<String, Boolean> like;

    public Post() {
        photo = new ArrayList<>();
        comment = new ArrayList<>();
    }

    public List<Photo> getPhoto() {
        return photo;
    }

    public void setPhoto(List<Photo> photo) {
        this.photo = photo;
    }

    public List<Comment> getComment() {
        return comment;
    }

    public void setComment(List<Comment> comment) {
        this.comment = comment;
    }
/*
    public List<Like> getLike() {
        return like;
    }

    public void setLike(List<Like> like) {
        this.like = like;
    }*/
}


