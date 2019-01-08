package com.openclassroom.alice.go4lunch.Model.ResultOfRequest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Alice on 08 January 2019.
 */
public class Result {
    @SerializedName("formatted_phone_number")
    @Expose
    private String formattedPhoneNumber;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("opening_hours")
    @Expose
    private OpeningHours openingHours;
    @SerializedName("rating")
    @Expose
    private Double rating;

    public String getFormattedPhoneNumber() {
        return formattedPhoneNumber;
    }

    public void setFormattedPhoneNumber(String formattedPhoneNumber) {
        this.formattedPhoneNumber = formattedPhoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OpeningHours getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(OpeningHours openingHours) {
        this.openingHours = openingHours;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }
}
