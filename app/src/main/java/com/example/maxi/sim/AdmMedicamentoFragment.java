package com.example.maxi.sim;

import android.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


public class AdmMedicamentoFragment extends Fragment {

    public AdmMedicamentoFragment() {
    }

    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        View rootView = inflater.inflate(R.layout.adm_fragment,container,false);

        return rootView;

    }
}
