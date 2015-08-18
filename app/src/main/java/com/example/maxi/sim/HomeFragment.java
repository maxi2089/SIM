package com.example.maxi.sim;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class HomeFragment extends Fragment {

    public HomeFragment() {
    }

    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        View rootView = inflater.inflate(R.layout.fragment_home,container,false);

        return rootView;

    }
}
