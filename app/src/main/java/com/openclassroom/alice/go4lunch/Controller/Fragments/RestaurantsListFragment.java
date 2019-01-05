package com.openclassroom.alice.go4lunch.Controller.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.openclassroom.alice.go4lunch.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RestaurantsListFragment extends Fragment {


    public RestaurantsListFragment() {
        // Required empty public constructor
    }

    public static RestaurantsListFragment newInstance() {
        return (new RestaurantsListFragment());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_restaurants_list, container, false);
    }

}
