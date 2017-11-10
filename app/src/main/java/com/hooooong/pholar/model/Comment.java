package com.hooooong.pholar.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Heepie on 2017. 11. 8..
 */

public class Comment implements Parcelable {
    public String comment_id;
    public String user_id;
    public String comment_content;

    public String nickname;
    public String profile_path;
    public String comment_date;

    public Comment() {

    }

    protected Comment(Parcel in) {
        comment_id = in.readString();
        user_id = in.readString();
        comment_content = in.readString();
        nickname = in.readString();
        profile_path = in.readString();
    }

    public static final Creator<Comment> CREATOR = new Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel in) {
            return new Comment(in);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };

    @Override
    public String toString() {
        return "Comment{" +
                "comment_id='" + comment_id + '\'' +
                ", user_id='" + user_id + '\'' +
                ", comment_content='" + comment_content + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(comment_id);
        dest.writeString(user_id);
        dest.writeString(comment_content);
        dest.writeString(nickname);
        dest.writeString(profile_path);
    }
}
