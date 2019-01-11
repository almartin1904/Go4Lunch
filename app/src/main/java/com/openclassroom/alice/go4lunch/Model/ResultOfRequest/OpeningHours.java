package com.openclassroom.alice.go4lunch.Model.ResultOfRequest;

import android.content.res.Resources;
import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.openclassroom.alice.go4lunch.R;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Alice on 08 January 2019.
 */
public class OpeningHours {
    private static final String TAG = OpeningHours.class.getSimpleName();
    @SerializedName("open_now")
    @Expose
    private Boolean openNow;
    @SerializedName("periods")
    @Expose
    private List<PeriodOfOpening> periods = null;
    @SerializedName("weekday_text")
    @Expose
    private List<String> weekdayText = null;

    public void OpeningHours(){

    }

    public String getOpenNowString(int currentDayOfWeek, int currentHour, int currentMinute) {
        if (openNow){
            return "Open";
        } else {
            return "Closed";
        }
    }

    private boolean closingSoon(String time, int currentHour, int currentMinute) {
        int hour=Integer.parseInt(time.substring(0,2));
        int minute=Integer.parseInt(time.substring(2,4));
        return currentHour == hour || (currentHour == hour - 1 && minute <= currentMinute);
    }


    private String getHourWithFormat(String time) {
        int hour=Integer.parseInt(time.substring(0,2));
        int minute=Integer.parseInt(time.substring(2,4));
        if (hour>12) {
            hour=hour-12;
            return hour + "." + minute + "pm";
        } else {
            return hour + "." + minute + "am";
        }
    }

    public boolean compareHours(String time, int currentHour, int currentMinute) {
        int hour=Integer.parseInt(time.substring(0,2));
        int minute=Integer.parseInt(time.substring(2,4));
        System.out.println(String.valueOf(minute));
        if (hour<=currentHour){
            return true;
        } else {
            return minute > currentMinute;
        }
    }

    public Boolean getOpenNow() {
        return openNow;
    }

    public void setOpenNow(Boolean openNow) {
        this.openNow = openNow;
    }

    public List<PeriodOfOpening> getPeriods() {
        return periods;
    }

    public void setPeriods(List<PeriodOfOpening> periods) {
        this.periods = periods;
    }

    public List<String> getWeekdayText() {
        return weekdayText;
    }

    public void setWeekdayText(List<String> weekdayText) {
        this.weekdayText = weekdayText;
    }
}
