package com.androidtech.onetoonefirebasechat.Models;

/**
 * Created by Ahmed Donkl on 11/30/2016.
 */

public class ProfileImage {
    private String profile_picture_thumbnail;
    private long updated_at;

    public ProfileImage( String profile_picture_thumbnail, long updated_at) {
        this.profile_picture_thumbnail = profile_picture_thumbnail;
        this.updated_at = updated_at;
    }

    public ProfileImage(){}

    public String getProfile_picture_thumbnail() {
        return profile_picture_thumbnail;
    }

    public void setProfile_picture_thumbnail(String profile_picture_thumbnail) {
        this.profile_picture_thumbnail = profile_picture_thumbnail;
    }

    public long getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(long updated_at) {
        this.updated_at = updated_at;
    }
}
