package com.hooooong.pholar.model;

/**
 * Created by Heepie on 2017. 11. 8..
 */

public class Like {
    public String like_id;
    public String user_id;

    @Override
    public String toString() {
        return "Like{" +
                "like_id='" + like_id + '\'' +
                ", user_id='" + user_id + '\'' +
                '}';
    }
}
