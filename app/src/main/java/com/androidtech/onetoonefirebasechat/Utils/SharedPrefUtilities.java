package com.androidtech.onetoonefirebasechat.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Ahmed Donkl on 21/4/16.
 */

/**
 * this is helper  class to communicate with  shared preference
 */
public class SharedPrefUtilities {

    //constants
    private static String USER_ID = "userID";
    private static String USER_NAME = "userName";
    private static String FULL_NAME = "fullName";
    private static String USER_IMAGE_THUMBNAIL = "userImageThumbnail";
    private static String USER_EMAIL = "userEmail";
    private static String USER_AGE = "userAge";
    private static String USER_GENDER = "userGender";
    private static String USER_RELIGION = "userReligion";
    private static String USER_RACE = "userRace";
    private static String USER_TALL_FEET = "userFeet";
    private static String USER_TALL_INCHES = "userInches";
    private static String USER_ABOUT_ME = "userAboutMe";
    private static String USER_LAT = "userLat";
    private static String USER_LNG = "userLng";
    private static String USER_LOCATION= "userLocation";
    private static String USER_CREATED_AT = "userCreatedAt";

    private static String USER_Ethnicity = "USER_Ethnicity";
    private static String USER_relationship  = "USER_relationship";
    private static String USER_BODY  = "USER_BODY";
    private static String USER_education   = "USER_education";
    private static String USER_occupation  = "USER_occupation";
    private static String USER_languages   = "USER_languages ";
    private static String USER_salary  = "USER_salary";
    private static String USER_smoke = "USER_smoke";
    private static String USER_drink  = "USER_drink";
    private static String USER_kids = "USER_kids";
    private static String USER_Diet  = "USER_Diet";
    private static String USER_sports = "USER_sports";
    private static String USER_often_exercise  = "USER_often_exercise";
    private static String USER_pets  = "USER_pets";
    private static String USER_political   = "USER_political";

    SharedPreferences preferences;

    public SharedPrefUtilities(Context context){
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    /**
     * clear data from shared pref
     */
    public void clearData(){
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }

    /**
     *
     * @param userID
     * add user id to shared preference
     */
    public void setUserID(String userID) {
        preferences.edit().putString(USER_ID, userID).apply();
    }

    /**
     * @return user id from shared preference
     */
    public String getUserID() {
        return preferences.getString(USER_ID, null);
    }

    /**
     *
     * @param userName
     * add user name to shared preference
     */
    public void setUserName(String userName) {
        preferences.edit().putString(USER_NAME, userName).apply();
    }

    /**
     * @return user name from shared preference
     */
    public String getUserName() {
        return preferences.getString(USER_NAME, null);
    }

    /**
     *
     * @param fullName
     * add fullName to shared preference
     */
    public void setFullName(String fullName) {
        preferences.edit().putString(FULL_NAME, fullName).apply();
    }

    /**
     * @return Full name from shared preference
     */
    public String getFullName() {
        return preferences.getString(FULL_NAME, null);
    }

    /**
     *
     * @param userEmail
     * add user email to shared preference
     */
    public void setUserEmail(String userEmail) {
        preferences.edit().putString(USER_EMAIL, userEmail).apply();
    }

    /**
     * @return user email from shared preference
     */
    public String getUserEmail() {
        return preferences.getString(USER_EMAIL, null);
    }

    /**************************/

    public void setUserImageThumbnail(String userImageThumbnail) {
        preferences.edit().putString(USER_IMAGE_THUMBNAIL, userImageThumbnail).apply();
    }

    public String getUserImageThumbnail() {
        return preferences.getString(USER_IMAGE_THUMBNAIL, null);
    }

    /**************************/
    public void setUserCreatedAt(long userCreatedAT) {
        preferences.edit().putLong(USER_CREATED_AT, userCreatedAT).apply();
    }


    public long getUserCeatedAt() {
        return preferences.getLong(USER_CREATED_AT, 0);
    }
    /**************************/
    public void setUserLat(double lat) {
        preferences.edit().putString(USER_LAT, String.valueOf(lat)).apply();
    }


    public double getUserLat() {
        String lat =  preferences.getString(USER_LAT, null);
        if(lat!=null)
            return Double.valueOf(lat);
        return 0;
    }

    /**************************/
    public void setUserLng(double lng) {
        preferences.edit().putString(USER_LNG, String.valueOf(lng)).apply();
    }


    public double getUserLng() {
        String lng =  preferences.getString(USER_LNG, null);
        if(lng!=null)
            return Double.valueOf(lng);
        return 0;
    }
    /**************************/
    public void setUserAge(String userAge) {
        preferences.edit().putString(USER_AGE, userAge).apply();
    }

    public String getUserAge() {
        return preferences.getString(USER_AGE, null);
    }

    public void removeAge(){
        preferences.edit().remove(USER_AGE).apply();
    }
    /**************************/
    public void setUserGender(String userGender) {
        preferences.edit().putString(USER_GENDER, userGender).apply();
    }

    public String getUserGender() {
        return preferences.getString(USER_GENDER, null);
    }

    public void removeGender(){
        preferences.edit().remove(USER_GENDER).apply();
    }

    /**************************/
    public void setUserRace(String userRace) {
        preferences.edit().putString(USER_RACE, userRace).apply();
    }

    public String getUserRace() {
        return preferences.getString(USER_RACE, null);
    }

    public void removeRace(){
        preferences.edit().remove(USER_RACE).apply();
    }
    /**************************/

    public void setUserReligion(String userReligion) {
        preferences.edit().putString(USER_RELIGION, userReligion).apply();
    }

    public String getUserReligion() {
        return preferences.getString(USER_RELIGION, null);
    }

    public void removeReligion(){
        preferences.edit().remove(USER_RELIGION).apply();
    }
    /**************************/
    public void setUserAboutMe(String userAboutMe) {
        preferences.edit().putString(USER_ABOUT_ME ,userAboutMe).apply();
    }

    public String getUserAboutMe() {
        return preferences.getString(USER_ABOUT_ME, null);
    }

    public void removeAboutMe(){
        preferences.edit().remove(USER_ABOUT_ME).apply();
    }
    /**************************/
    public void setUserLocation(String userLocation) {
        preferences.edit().putString(USER_LOCATION,userLocation).apply();
    }

    public String getUserLocation() {
        return preferences.getString(USER_LOCATION, null);
    }
    /**************************/

    public void removeLocation(){
        preferences.edit().remove(USER_LOCATION).apply();
        preferences.edit().remove(USER_LAT).apply();
        preferences.edit().remove(USER_LNG).apply();
    }

    /**************************/
    public void setUserTallFeet(String userTallFeet) {
        preferences.edit().putString(USER_TALL_FEET, userTallFeet).apply();
    }

    public String getUserTallFeet() {
        return preferences.getString(USER_TALL_FEET, null);
    }

    public void removeFeet(){
        preferences.edit().remove(USER_TALL_FEET).apply();
    }
    /**************************/

    /**************************/
    public void setUserTallInches(String userTallInches) {
        preferences.edit().putString(USER_TALL_INCHES, userTallInches).apply();
    }

    public String getUserTallInches() {
        return preferences.getString(USER_TALL_INCHES, null);
    }

    public void removeInches(){
        preferences.edit().remove(USER_TALL_INCHES).apply();
    }
    /**************************/

    /**************************/
    public void setUserEthnicity(String userTallInches) {
        preferences.edit().putString(USER_Ethnicity, userTallInches).apply();
    }

    public String getUserEthnicity() {
        return preferences.getString(USER_Ethnicity, null);
    }

    public void removeEthnicity(){
        preferences.edit().remove(USER_Ethnicity).apply();
    }
    /**************************/

    /**************************/
    public void setUSER_relationship(String userRelation) {
        preferences.edit().putString(USER_relationship, userRelation).apply();
    }

    public String getUSER_relationship() {
        return preferences.getString(USER_relationship, null);
    }

    public void removeRealtions(){
        preferences.edit().remove(USER_relationship).apply();
    }
    /**************************/

    /**************************/
    public void setUSERBody(String userRelation) {
        preferences.edit().putString(USER_BODY, userRelation).apply();
    }

    public String getUSERBody() {
        return preferences.getString(USER_BODY, null);
    }

    public void removeBody(){
        preferences.edit().remove(USER_BODY).apply();
    }
    /**************************/

    /**************************/
    public void setUSEReducation(String userRelation) {
        preferences.edit().putString(USER_education, userRelation).apply();
    }

    public String getUSEReducation() {
        return preferences.getString(USER_education, null);
    }

    /**************************/


    /**************************/
    public void setUSERoccupation(String userRelation) {
        preferences.edit().putString(USER_occupation, userRelation).apply();
    }

    public String getUSERoccupation() {
        return preferences.getString(USER_occupation, null);
    }

    /**************************/


    /**************************/
    public void setUSERlanguages(String userRelation) {
        preferences.edit().putString(USER_languages, userRelation).apply();
    }

    public String getUSERlanguages() {
        return preferences.getString(USER_languages, null);
    }

    public void removelanguages(){
        preferences.edit().remove(USER_languages).apply();
    }
    /**************************/


    /**************************/
    public void setUSERsalary(String userRelation) {
        preferences.edit().putString(USER_salary, userRelation).apply();
    }

    public String getUSERsalary() {
        return preferences.getString(USER_salary, null);
    }

    /**************************/


    /**************************/
    public void setUSERsmoke(String userRelation) {
        preferences.edit().putString(USER_smoke, userRelation).apply();
    }

    public String getUSERsmoke() {
        return preferences.getString(USER_smoke, null);
    }

    /**************************/


    /**************************/
    public void setUSERdrink(String userRelation) {
        preferences.edit().putString(USER_drink, userRelation).apply();
    }

    public String getUSERdrink() {
        return preferences.getString(USER_drink, null);
    }

    /**************************/


    /**************************/
    public void setUSERkids(String userRelation) {
        preferences.edit().putString( USER_kids, userRelation).apply();
    }

    public String getUSERkids() {
        return preferences.getString( USER_kids, null);
    }

    /**************************/


    /**************************/
    public void setUSERDiet(String userRelation) {
        preferences.edit().putString(USER_Diet, userRelation).apply();
    }

    public String getUSERDiet() {
        return preferences.getString(USER_Diet, null);
    }

    /**************************/


    /**************************/
    public void setUSERsports(String userRelation) {
        preferences.edit().putString(USER_sports, userRelation).apply();
    }

    public String getUSERsports() {
        return preferences.getString(USER_sports, null);
    }

    /**************************/


    /**************************/
    public void setUSERoften_exercise(String userRelation) {
        preferences.edit().putString(USER_often_exercise, userRelation).apply();
    }

    public String getUSERoften_exercise() {
        return preferences.getString(USER_often_exercise, null);
    }

    /**************************/


    /**************************/
    public void setUSERpets(String userRelation) {
        preferences.edit().putString(USER_pets, userRelation).apply();
    }

    public String getUSERpets() {
        return preferences.getString(USER_pets, null);
    }

    /**************************/


    /**************************/
    public void setUSERpolitical(String userRelation) {
        preferences.edit().putString(USER_political, userRelation).apply();
    }

    public String getUSERpolitical() {
        return preferences.getString(USER_political, null);
    }

    /**************************/


}
