package com.openclassroom.alice.go4lunch.View;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.RequestManager;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.openclassroom.alice.go4lunch.Model.Workmate;
import com.openclassroom.alice.go4lunch.R;

/**
 * Created by Alice on 12 January 2019.
 */
public class WorkmateAdapter extends FirestoreRecyclerAdapter<Workmate, WorkmateViewHolder> {


    private static final String TAG = WorkmateAdapter.class.getSimpleName();
    private Context mContext;
    
    public interface Listener {
        void onDataChanged();
    }

    //FOR DATA
    private final RequestManager glide;
    private int mOrigin;

    //FOR COMMUNICATION
    private Listener callback;

    public WorkmateAdapter(@NonNull FirestoreRecyclerOptions<Workmate> options, RequestManager glide, Listener callback, Context context, int origin) {
        super(options);
        Log.d(TAG, "WorkmateAdapter: "+options.toString());
        this.glide = glide;
        this.callback = callback;
        this.mContext = context;
        this.mOrigin=origin;
    }

    @Override
    protected void onBindViewHolder(@NonNull WorkmateViewHolder holder, int position, @NonNull Workmate model) {
        Log.d(TAG, "onBindViewHolder: "+position);
        holder.updateWithWorkmate(model, this.glide, mOrigin);
    }

    @Override
    public WorkmateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: ");
        return new WorkmateViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_workmate_item, parent, false), mContext);
    }

    @Override
    public void onDataChanged() {
        Log.d(TAG, "onDataChanged: ");
        super.onDataChanged();
        this.callback.onDataChanged();
    }
}
