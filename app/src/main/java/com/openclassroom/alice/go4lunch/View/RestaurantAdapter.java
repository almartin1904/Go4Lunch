package com.openclassroom.alice.go4lunch.View;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.openclassroom.alice.go4lunch.Model.ResultOfRequest.Restaurant;
import com.openclassroom.alice.go4lunch.R;

import java.util.List;

/**
 * Created by Alice on 08 January 2019.
 */
public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantViewHolder> {
    // FOR DATA
    private List<Restaurant> mRestaurants;

    // CONSTRUCTOR
    public RestaurantAdapter(List<Restaurant> restaurants) {
        this.mRestaurants = restaurants;
    }

    @Override
    public RestaurantViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // CREATE VIEW HOLDER AND INFLATING ITS XML LAYOUT
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fragment_restaurant_item, parent, false);

        return new RestaurantViewHolder(view);
    }

    // UPDATE VIEW HOLDER WITH A GITHUBUSER
    @Override
    public void onBindViewHolder(RestaurantViewHolder viewHolder, int position) {
        viewHolder.updateWithGithubUser(this.mRestaurants.get(position));
    }

    // RETURN THE TOTAL COUNT OF ITEMS IN THE LIST
    @Override
    public int getItemCount() {
        return this.mRestaurants.size();
    }
}
