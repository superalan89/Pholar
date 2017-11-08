package com.hooooong.pholar.model;

import java.util.List;
import java.util.Map;

/**
 * Created by Heepie on 2017. 11. 8..
 *
 * User가 작성한 글의 테이블
 */

public class Post {
    public String post_id;
    public String date;
    public String post_content;
    public String writer;

    public Map<String, Map<String, String>> photo;
    public Map<String, Map<String, String>> comment;
    public Map<String, Map<String, String>> like;

    private List<Photo> photoList;
    private List<Comment> commentList;
    private List<Like> likeList;

    public List<Photo> getPhotoList() {
        return photoList;
    }

    public List<Comment> getCommentList() {
        return commentList;
    }

    public List<Like> getLikeList() {
        return likeList;
    }

    public void setPhotoList(List<Photo> photoList) {
        this.photoList = photoList;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }

    public void setLikeList(List<Like> likeList) {
        this.likeList = likeList;
    }
}


