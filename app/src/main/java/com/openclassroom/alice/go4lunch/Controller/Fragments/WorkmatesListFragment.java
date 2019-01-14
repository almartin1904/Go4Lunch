package com.openclassroom.alice.go4lunch.Controller.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.openclassroom.alice.go4lunch.Controller.Activities.MainActivity;
import com.openclassroom.alice.go4lunch.Model.Workmate;
import com.openclassroom.alice.go4lunch.Model.WorkmateHelper;
import com.openclassroom.alice.go4lunch.R;
import com.openclassroom.alice.go4lunch.View.WorkmateAdapter;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class WorkmatesListFragment extends Fragment implements WorkmateAdapter.Listener{

    @BindView(R.id.fragment_workmates_recyclerview) RecyclerView mRecyclerView;

    private WorkmateAdapter mWorkmateAdapter;
    private static final String TAG = WorkmatesListFragment.class.getSimpleName();

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
        View view = inflater.inflate(R.layout.fragment_workmates_list, container, false);
        ButterKnife.bind(this, view);
        
        this.configureRecyclerView();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mWorkmateAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mWorkmateAdapter.stopListening();
    }

    private void configureRecyclerView() {

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            this.mWorkmateAdapter =
                    new WorkmateAdapter(generateOptionsForAdapter(WorkmateHelper.getAllWorkMates()),
                            Glide.with(this),
                            this,
                            getContext());
            mWorkmateAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                @Override
                public void onItemRangeInserted(int positionStart, int itemCount) {
                    mRecyclerView.smoothScrollToPosition(mWorkmateAdapter.getItemCount());
                }

            });
        }
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(this.mWorkmateAdapter);

    }

    // 6 - Create options for RecyclerView from a Query
    private FirestoreRecyclerOptions<Workmate> generateOptionsForAdapter(Query query){
        return new FirestoreRecyclerOptions.Builder<Workmate>()
                .setQuery(query, Workmate.class)
                .setLifecycleOwner(this)
                .build();
    }

    // --------------------
    // CALLBACK
    // --------------------

    @Override
    public void onDataChanged() {
        // 7 - Show TextView in case RecyclerView is empty
        if (mWorkmateAdapter.getItemCount()==0){
            mRecyclerView.setVisibility(View.GONE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
        }
        //mRecyclerView.setVisibility(this.mWorkmateAdapter.getItemCount() == 0 ? View.VISIBLE : View.VISIBLE);
    }

}
