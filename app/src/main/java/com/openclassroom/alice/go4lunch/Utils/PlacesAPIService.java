package com.openclassroom.alice.go4lunch.Utils;

import com.openclassroom.alice.go4lunch.BuildConfig;
import com.openclassroom.alice.go4lunch.Model.ResultOfRequest.DistanceResult;
import com.openclassroom.alice.go4lunch.Model.ResultOfRequest.PlaceDetailResult;
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

    @GET("maps/api/place/nearbysearch/json?location=50.633258,3.020537&rankby=distance&type=restaurant&key="+BuildConfig.GOOGLE_API_KEY)
    Observable<RequestResult> getRestaurantPlaces();

    @GET("maps/api/distancematrix/json?units=metric&origins=50.633258,3.020537&key="+BuildConfig.GOOGLE_API_KEY)
    Observable<DistanceResult> getDistanceBetweenTwoPoints(@Query("destinations") String placeID);

    @GET("maps/api/place/details/json?fields=name,rating,formatted_phone_number,opening_hours,formatted_address,website,photo&key="+BuildConfig.GOOGLE_API_KEY)
    Observable<PlaceDetailResult> getDetailsOfPlace(@Query("placeid") String placeID);

    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build();


}
