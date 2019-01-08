package com.openclassroom.alice.go4lunch.Model.ResultOfRequest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Alice on 08 January 2019.
 */
public class OpeningHours {
    @SerializedName("open_now")
    @Expose
    private Boolean openNow;

    public Boolean getOpenNow() {
        return openNow;
    }

    public String getOpenNowString() {
        if (openNow){
            return "Open";
        } else {
            return "Closed";
        }
    }

    public void setOpenNow(Boolean openNow) {
        this.openNow = openNow;
    }
}
