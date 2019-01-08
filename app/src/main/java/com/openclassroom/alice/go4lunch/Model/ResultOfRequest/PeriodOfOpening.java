package com.openclassroom.alice.go4lunch.Model.ResultOfRequest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Alice on 08 January 2019.
 */
public class PeriodOfOpening {
    @SerializedName("close")
    @Expose
    private Hour close;
    @SerializedName("open")
    @Expose
    private Hour open;

    public Hour getClose() {
        return close;
    }

    public void setClose(Hour close) {
        this.close = close;
    }

    public Hour getOpen() {
        return open;
    }

    public void setOpen(Hour open) {
        this.open = open;
    }
}
