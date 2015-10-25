package com.example.maxi.sim;

import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;


public class HomeFragment extends Fragment {

    private ImageButton btnModificar;
    private View rootView;

    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        rootView = inflater.inflate(R.layout.fragment_home,container,false);

        btnModificar = (ImageButton)rootView.findViewById(R.id.btnModificar);

        btnModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment;

                fragment = new ModificarUsuarioFragment();

                if (fragment != null) {

                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                } else {
                    Log.e("Error  ", "Modificar Usuario");
                }
            }});

        return rootView;

    }
}
