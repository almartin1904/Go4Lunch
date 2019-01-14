package com.openclassroom.alice.go4lunch.Model;

import android.support.annotation.Nullable;

/**
 * Created by Alice on 12 January 2019.
 */
public class Workmate {
    private String uid;
    private String name;
    private String mail;
    private String restaurantPlaceId;
    private String nameOfRestaurant;
    @Nullable private String urlPicture;

    public Workmate() { }

    public Workmate(String uid, String name, String mail, @Nullable String urlPicture) {
        this.uid = uid;
        this.name = name;
        this.mail = mail;
        this.urlPicture = urlPicture;
        this.restaurantPlaceId="";
        this.nameOfRestaurant="";
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public String getFirstName() {
        return name.substring(0, name.indexOf(' '));
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getRestaurantPlaceId() {
        return restaurantPlaceId;
    }

    public void setRestaurantPlaceId(String restaurantPlaceId) {
        this.restaurantPlaceId = restaurantPlaceId;
    }

    @Nullable
    public String getUrlPicture() {
        return urlPicture;
    }

    public void setUrlPicture(@Nullable String urlPicture) {
        this.urlPicture = urlPicture;
    }

    public String getNameOfRestaurant() {
        return nameOfRestaurant;
    }

    public void setNameOfRestaurant(String nameOfRestaurant) {
        this.nameOfRestaurant = nameOfRestaurant;
    }
}
