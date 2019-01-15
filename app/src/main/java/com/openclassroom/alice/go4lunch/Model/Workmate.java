package com.openclassroom.alice.go4lunch.Model;

import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alice on 12 January 2019.
 */
public class Workmate {
    private String mUid;
    private String mName;
    private String mMail;
    private String mRestaurantPlaceId;
    private String mNameOfRestaurant;
    private List<String> mRestaurantLikedPlaceId;
    @Nullable private String mUrlPicture;

    public Workmate() { }

    public Workmate(String uid, String name, String mail, @Nullable String urlPicture) {
        this.mUid = uid;
        this.mName = name;
        this.mMail = mail;
        this.mUrlPicture = urlPicture;
        this.mRestaurantPlaceId="";
        this.mNameOfRestaurant="";
        this.mRestaurantLikedPlaceId=new ArrayList<>();
    }

    public String getUid() {
        return mUid;
    }

    public void setUid(String uid) {
        this.mUid = uid;
    }

    public String getName() {
        return mName;
    }

    public String getFirstName() {
        return mName.substring(0, mName.indexOf(' '));
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getMail() {
        return mMail;
    }

    public void setMail(String mail) {
        this.mMail = mail;
    }

    public String getRestaurantPlaceId() {
        return mRestaurantPlaceId;
    }

    public void setRestaurantPlaceId(String restaurantPlaceId) {
        this.mRestaurantPlaceId = restaurantPlaceId;
    }

    @Nullable
    public String getUrlPicture() {
        return mUrlPicture;
    }

    public void setUrlPicture(@Nullable String urlPicture) {
        this.mUrlPicture = urlPicture;
    }

    public String getNameOfRestaurant() {
        return mNameOfRestaurant;
    }

    public void setNameOfRestaurant(String nameOfRestaurant) {
        this.mNameOfRestaurant = nameOfRestaurant;
    }

    public List<String> getRestaurantLikedPlaceId() {
        return mRestaurantLikedPlaceId;
    }

    public void setRestaurantLikedPlaceId(List<String> restaurantLikedPlaceId) {
        mRestaurantLikedPlaceId = restaurantLikedPlaceId;
    }
}
