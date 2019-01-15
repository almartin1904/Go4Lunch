package com.openclassroom.alice.go4lunch.Controller.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.openclassroom.alice.go4lunch.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RestaurantsMapsFragment extends Fragment {


    public RestaurantsMapsFragment() {
        // Required empty public constructor
    }

    public static RestaurantsMapsFragment newInstance() {
        return (new RestaurantsMapsFragment());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_restaurants_maps, container, false);
    }

}
