package com.example.maxi.sim;

import android.app.Fragment;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class FarmacoFragment extends Fragment {
    private ArrayList<Paciente> ListaPaciente;
    private Spinner spinnerPaciente;
    private Paciente pacienteActivo;
    private EditText volumen;
    private EditText tiempo;
    private TextView resultado;
    private Button btnCalcular;
    private Button btnEnviar;


    public FarmacoFragment() {}



    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        View rootView = inflater.inflate(R.layout.fragment_farmaco,container,false);


       ListaPaciente = (ArrayList<Paciente>)getArguments().getSerializable("LISTA");
       TextView txtSaludo = (TextView)rootView.findViewById(R.id.txtSaludo);
       txtSaludo.setText(ListaPaciente.get(0).getApellido());

       spinnerPaciente = (Spinner)rootView.findViewById(R.id.listaPaciente);

        List<String> pacientes = new  ArrayList<String>();

        pacientes.add("Pacientes Asigandos");

        for(int i=0;i<ListaPaciente.size();i++){
            pacientes.add(ListaPaciente.get(i).getID()+" - "+ListaPaciente.get(i).getNombre()+' '+ListaPaciente.get(i).getApellido());
        }

       ArrayAdapter<String> adaptador = new ArrayAdapter<String>(rootView.getContext(),android.R.layout.simple_spinner_item,pacientes);

        spinnerPaciente.setAdapter(adaptador);



        spinnerPaciente.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                       long arg3) {
                if (arg2 != 0) {
                    //Posicion del spinner debe coincidir con la posicion de la lista de pacientes..
                    pacienteActivo = ListaPaciente.get(arg2 - 1);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                //optionally do something here
            }
        });

        volumen = (EditText) rootView.findViewById(R.id.TxtResultado);
        tiempo =  (EditText) rootView.findViewById(R.id.TxtTiempo);
        resultado = (TextView) rootView.findViewById(R.id.TxtResultado);

        btnCalcular =  (Button) rootView.findViewById(R.id.btnCalcular);
        btnEnviar   =  (Button) rootView.findViewById(R.id.btnEnviar);

        btnCalcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              //resultado.setText((volumen*60));
            }
        });

        return rootView;

    }
}
