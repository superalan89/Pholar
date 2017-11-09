package com.hooooong.pholar.model;

import java.util.List;
import java.util.Map;

/**
 * Created by Heepie on 2017. 11. 8..
 *
 * User 정보 테이블
 */

public class User {
    public String user_id;
    public String nickname;
    public String status_msg;
    public String phone_number;
    public String profile_path;
    public Map<String, Map<String, String>> post_thumbnail;

    private List<PostThumbnail> postThumbnailList;

    public List<PostThumbnail> getPostThumbnailList() {
        return postThumbnailList;
    }

    public void setPostThumbnailList(List<PostThumbnail> postThumbnailList) {
        this.postThumbnailList = postThumbnailList;
    }

    @Override
    public String toString() {
        return "User{" +
                "user_id='" + user_id + '\'' +
                ", nickname='" + nickname + '\'' +
                ", status_msg='" + status_msg + '\'' +
                ", phone_number='" + phone_number + '\'' +
                ", profile_path='" + profile_path + '\'' +
                '}';
    }
}
