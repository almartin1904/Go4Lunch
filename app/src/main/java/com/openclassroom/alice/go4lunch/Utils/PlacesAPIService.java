package com.openclassroom.alice.go4lunch.Utils;

import com.openclassroom.alice.go4lunch.Model.ResultOfRequest.DistanceResult;
import com.openclassroom.alice.go4lunch.Model.ResultOfRequest.RequestResult;


import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Alice on 08 January 2019.
 */
public interface PlacesAPIService {

    String mAPIKey="AIzaSyB0bbKRXlGkEbvEFjxXyACgyAJrZLGS42w";

    @GET("maps/api/place/nearbysearch/json?location=50.633258,3.020537&radius=500&type=restaurant&key="+mAPIKey)
    Observable<RequestResult> getRestaurantPlaces();

    @GET("maps/api/distancematrix/json?units=metric&origins=50.633258,3.020537&key="+mAPIKey)
    Observable<DistanceResult> getDistanceBetweenTwoPoints(@Query("destinations") String placeID);

    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build();


}
