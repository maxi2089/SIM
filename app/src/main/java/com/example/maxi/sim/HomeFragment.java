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
import android.widget.TextView;

import org.w3c.dom.Text;


public class HomeFragment extends Fragment {


    private View rootView;
    private Sesion sesion;
    private ImageButton btnModificar;
    private TextView txtRol;
    private TextView txtUser;

    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        rootView = inflater.inflate(R.layout.fragment_home, container, false);

        sesion = Sesion.getInstance();
        btnModificar = (ImageButton)rootView.findViewById(R.id.btnModificar);
        txtRol = (TextView)rootView.findViewById(R.id.txtRol);
        txtUser = (TextView)rootView.findViewById(R.id.titulo);

        txtRol.setText(sesion.getUser().getRol().getNombreRol());
        txtUser.setText(sesion.getUser().getNombre());

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
