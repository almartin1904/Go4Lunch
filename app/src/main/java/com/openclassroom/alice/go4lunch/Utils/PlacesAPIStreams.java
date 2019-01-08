package com.openclassroom.alice.go4lunch.Utils;

import com.openclassroom.alice.go4lunch.Model.ResultOfRequest.PhotoUrl;
import com.openclassroom.alice.go4lunch.Model.ResultOfRequest.RequestResult;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Alice on 08 January 2019.
 */
public class PlacesAPIStreams {
    public static Observable<RequestResult> streamFetchRestaurants() {
        PlacesAPIService restaurantService = PlacesAPIService.retrofit.create(PlacesAPIService.class);
        return restaurantService.getRestaurantPlaces()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);
    }

}
