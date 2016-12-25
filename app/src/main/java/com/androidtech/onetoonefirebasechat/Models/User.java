/*
 * Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.androidtech.onetoonefirebasechat.Models;

import org.parceler.Parcel;

@Parcel
public class User {
    private String full_name;
    private String user_name;
    private String profile_picture_thumbnail;
    private long created_at;
    private long updated_at;
    private String email;
    private String gender;
    private String race;
    private String age;
    private String religion;
    private String about_me;
    private String location;
    private String user_id;
    private double lat;
    private double lng;

    private String last_message;
    private String last_message_sender;
    private long last_message_timestamp;
    private boolean last_message_seen;

    private String tall_feet;
    private String tall_inches;
    private String ethnicity;
    private String relationship_status;
    private String userBody;
    private String userEducation;
    private String userOccupation;
    private String userSalary;
    private String userSmoke;
    private String userDrink;
    private String userKids;
    private String userDiet;
    private String userSports;
    private String userOftenExercise;
    private String userPets;
    private String userPolitical;
    private String userlanguages;

    public User() {

    }

    public String getTall_feet() {
        return tall_feet;
    }

    public String getTall_inches() {
        return tall_inches;
    }

    public String getEthnicity() {
        return ethnicity;
    }

    public String getRelationship_status() {
        return relationship_status;
    }

    public String getUserBody() {
        return userBody;
    }

    public String getUserEducation() {
        return userEducation;
    }

    public String getUserOccupation() {
        return userOccupation;
    }

    public String getUserSalary() {
        return userSalary;
    }

    public String getUserSmoke() {
        return userSmoke;
    }

    public String getUserDrink() {
        return userDrink;
    }

    public String getUserKids() {
        return userKids;
    }

    public String getUserDiet() {
        return userDiet;
    }

    public String getUserSports() {
        return userSports;
    }

    public String getUserOftenExercise() {
        return userOftenExercise;
    }

    public String getUserPets() {
        return userPets;
    }

    public String getUserPolitical() {
        return userPolitical;
    }

    public String getUserlanguages() {
        return userlanguages;
    }

    public String getLast_message_sender() {
        return last_message_sender;
    }

    public void setLast_message_sender(String last_message_sender) {
        this.last_message_sender = last_message_sender;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getLast_message() {
        return last_message;
    }

    public long getLast_message_timestamp() {
        return last_message_timestamp;
    }

    public boolean isLast_message_seen() {
        return last_message_seen;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAbout_me() {
        return about_me;
    }

    public void setAbout_me(String about_me) {
        this.about_me = about_me;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getFull_name() {
        return full_name;
    }

    public String getProfile_picture_thumbnail() {
        return profile_picture_thumbnail;
    }

    public long getCreated_at() {
        return created_at;
    }

    public long getUpdated_at() {
        return updated_at;
    }

    public String getEmail() {
        return email;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public void setProfile_picture_thumbnail(String profile_picture_thumbnail) {
        this.profile_picture_thumbnail = profile_picture_thumbnail;
    }

    public void setCreated_at(long created_at) {
        this.created_at = created_at;
    }

    public void setUpdated_at(long updated_at) {
        this.updated_at = updated_at;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setLast_message(String last_message) {
        this.last_message = last_message;
    }

    public void setLast_message_timestamp(long last_message_timestamp) {
        this.last_message_timestamp = last_message_timestamp;
    }

    public void setLast_message_seen(boolean last_message_seen) {
        this.last_message_seen = last_message_seen;
    }
}
