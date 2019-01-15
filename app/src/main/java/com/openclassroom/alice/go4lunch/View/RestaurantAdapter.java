package com.openclassroom.alice.go4lunch.View;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.RequestManager;
import com.openclassroom.alice.go4lunch.Model.ResultOfRequest.Restaurant;
import com.openclassroom.alice.go4lunch.R;

import java.util.List;

/**
 * Created by Alice on 08 January 2019.
 */
public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantViewHolder> {
    // FOR DATA
    private List<Restaurant> mRestaurants;
    private RequestManager mGlide;

    // CONSTRUCTOR
    public RestaurantAdapter(List<Restaurant> restaurants, RequestManager glide) {
        this.mRestaurants = restaurants;
        mGlide=glide;
    }

    @NonNull
    @Override
    public RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // CREATE VIEW HOLDER AND INFLATING ITS XML LAYOUT
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fragment_restaurant_item, parent, false);

        return new RestaurantViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantViewHolder viewHolder, int position) {
        viewHolder.updateWithRestaurants(this.mRestaurants.get(position), mGlide);
    }

    // RETURN THE TOTAL COUNT OF ITEMS IN THE LIST
    @Override
    public int getItemCount() {
        return this.mRestaurants.size();
    }

    public Restaurant getRestaurant(int position){
        return this.mRestaurants.get(position);
    }
}
