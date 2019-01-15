package com.openclassroom.alice.go4lunch.Model;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.openclassroom.alice.go4lunch.Controller.Fragments.RestaurantsListFragment;
import com.openclassroom.alice.go4lunch.Controller.Fragments.RestaurantsMapsFragment;
import com.openclassroom.alice.go4lunch.Controller.Fragments.WorkmatesListFragment;
import com.openclassroom.alice.go4lunch.R;

/**
 * Created by Alice on 05 January 2019.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;

    //Default Constructor
    public ViewPagerAdapter(FragmentManager mgr, Context context) {
        super(mgr);
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return(3);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: //Page number 1
                return RestaurantsMapsFragment.newInstance();
            case 1: //Page number 2
                return RestaurantsListFragment.newInstance();
            case 2: //Page number 3
                return WorkmatesListFragment.newInstance();
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0: //Page number 1
                return mContext.getResources().getString(R.string.tablayout_maps);
            case 1: //Page number 2
                return mContext.getResources().getString(R.string.tablayout_list);
            case 2: //Page number 3
                return mContext.getResources().getString(R.string.tablayout_workmates);
            default:
                return null;
        }
    }


}
