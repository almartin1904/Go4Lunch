package com.openclassroom.alice.go4lunch.Model.ResultOfRequest;

import android.content.res.Resources;
import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.openclassroom.alice.go4lunch.R;

import java.util.Calendar;
import java.util.List;

import static com.openclassroom.alice.go4lunch.Constantes.CLOSED;
import static com.openclassroom.alice.go4lunch.Constantes.CLOSING_SOON;
import static com.openclassroom.alice.go4lunch.Constantes.OPEN;
import static com.openclassroom.alice.go4lunch.Constantes.OPEN_24_7;

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

    public int getOpenNowString(int currentDayOfWeek, int currentHour, int currentMinute) {
        int closingStatus;
        if (openNow){
            if (closingSoon(getClosingHour(currentDayOfWeek, currentHour), currentHour, currentMinute)){
                return CLOSING_SOON;
            } else {
                return OPEN;
            }
            //"Open until "+ getHourWithFormat(getClosingHour(currentDayOfWeek, currentHour, currentMinute));
        } else {
            return CLOSED;
        }
    }

    public String getClosingHour(int currentDayOfWeek, int currentHour) {
        int i=0;
        while (periods.get(i).getClose().getDay()<currentDayOfWeek){
            i=i+1;
        }
        if (Integer.parseInt(periods.get(i).getClose().getTime().substring(0,2))<currentHour){
            i=i+1;
        }
        return periods.get(i).getClose().getTime();
    }

    private boolean closingSoon(String time, int currentHour, int currentMinute) {
        int hour=Integer.parseInt(time.substring(0,2));
        int minute=Integer.parseInt(time.substring(2,4));
        return currentHour == hour || (currentHour == hour - 1 && minute <= currentMinute);
    }


    public String getHourWithFormat(String time) {
        int hour=Integer.parseInt(time.substring(0,2));
        String minute=time.substring(2,4);
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
