package com.openclassroom.alice.go4lunch.Model;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alice on 16 January 2019.
 */
public class NotificationRestaurant implements Serializable {

    private String restaurantName;
    private String restaurantAddress;
    private List<String> workmatesJoining;
    private static final String TAG = NotificationRestaurant.class.getSimpleName();

    public NotificationRestaurant() {
        this.restaurantName = "";
        this.restaurantAddress = "";
        workmatesJoining=new ArrayList<>();
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        Log.d(TAG, "setRestaurantName: "+restaurantName);
        this.restaurantName = restaurantName;
    }

    public String getRestaurantAddress() {
        return restaurantAddress;
    }

    public void setRestaurantAddress(String restaurantAddress) {
        Log.d(TAG, "setRestaurantAddress: "+restaurantAddress);
        this.restaurantAddress = restaurantAddress;
    }

    public List<String> getWorkmatesJoining() {
        return workmatesJoining;
    }

    public void setWorkmatesJoining(List<String> workmatesJoining) {
        Log.d(TAG, "setWorkmatesJoining: ");
        this.workmatesJoining = workmatesJoining;
    }

    public String getStringWorkmatesJoining() {
        StringBuilder result= new StringBuilder();
        if (workmatesJoining.size()==0){
            result = new StringBuilder("nobody");
        } else {
            for (int i=0;i<workmatesJoining.size();i++){
                result.append(workmatesJoining.get(i));
                if (i==workmatesJoining.size()-2) {
                    result.append(" and ");
                } else {
                    if (i!=workmatesJoining.size()-1){
                        result.append(", ");
                    }
                }
            }
        }
        return result.toString();
    }

}
