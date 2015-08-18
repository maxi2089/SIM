package com.example.maxi.sim;

import android.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;




public class FarmacoFragment extends Fragment {
    private ArrayList<Paciente> ListaPaciente;


    public FarmacoFragment() {}



    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        View rootView = inflater.inflate(R.layout.fragment_farmaco,container,false);




       ListaPaciente = (ArrayList<Paciente>)getArguments().getSerializable("LISTA");
       System.out.println("Apellido " + ListaPaciente.get(0).getApellido());
        //TextView txtSaludo = (TextView)this.getView().findViewById(R.id.txtSaludo);
       // txtSaludo.setText("GILADA");
       //txtSaludo.setText(ListaPaciente.get(0).getApellido());

        return rootView;

    }
}
