package com.hooooong.pholar.model;

/**
 * Created by Heepie on 2017. 11. 8..
 */

public class Comment {
    public String comment_id;
    public String user_id;
    public String comment_content;

    @Override
    public String toString() {
        return "Comment{" +
                "comment_id='" + comment_id + '\'' +
                ", user_id='" + user_id + '\'' +
                ", comment_content='" + comment_content + '\'' +
                '}';
    }
}
