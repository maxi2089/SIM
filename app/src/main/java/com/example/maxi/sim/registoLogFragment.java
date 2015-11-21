package com.example.maxi.sim;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by yamila on 20/11/2015.
 */
public class registoLogFragment extends Fragment {
    private View rootView;
    private String URL;
    private fragmentActivo fragActivo;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        rootView = inflater.inflate(R.layout.fragment_programar_visita, container, false);
        Url urlServer = Url.getInstance();
        URL = urlServer.getUrl();

        fragActivo = fragmentActivo.getInstance();
        fragActivo.setData("LOG");
        return rootView;
    }
}
