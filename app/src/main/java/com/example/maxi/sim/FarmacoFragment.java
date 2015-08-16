package com.example.maxi.sim;

import android.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


public class FarmacoFragment extends Fragment {

    public FarmacoFragment() {
    }

    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        View rootView = inflater.inflate(R.layout.fragment_farmaco,container,false);

        return rootView;

    }
}
