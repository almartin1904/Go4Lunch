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
public class WorkmatesListFragment extends Fragment {


    public WorkmatesListFragment() {
        // Required empty public constructor
    }

    public static WorkmatesListFragment newInstance() {
        return (new WorkmatesListFragment());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_workmates_list, container, false);
    }

}
