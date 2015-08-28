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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class FarmacoFragment extends Fragment {
    private ArrayList<Paciente> ListaPaciente;
    private Spinner spinnerPaciente;
    private Paciente pacienteActivo;
    private EditText volumen;
    private EditText tiempo;
    private TextView resultadoVolumen;
    private TextView resultadoGotas;
    private ImageView macroGotas;
    private ImageView microGotas;
    private Switch factorGoteo;
    private Button btnCalcular;
    private Button btnEnviar;
    private Double factorG;


    public FarmacoFragment() {}



    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        View rootView = inflater.inflate(R.layout.fragment_farmaco,container,false);


       ListaPaciente = (ArrayList<Paciente>)getArguments().getSerializable("LISTA");

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
        microGotas = (ImageView)rootView.findViewById(R.id.imageMicroGota);
        macroGotas = (ImageView)rootView.findViewById(R.id.imageMacroGota);

        factorGoteo = (Switch) rootView.findViewById(R.id.factorGoteo);

        factorGoteo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if(isChecked){
                    //MicroGota
                    macroGotas.setVisibility(View.INVISIBLE);
                    microGotas.setVisibility(View.VISIBLE);
                    factorG=60.0;
                }else{
                    //MacroGota
                    macroGotas.setVisibility(View.VISIBLE);
                    microGotas.setVisibility(View.INVISIBLE);
                    factorG=20.0;
                }

            }
        });

        volumen   = (EditText) rootView.findViewById(R.id.TxtVolumen);
        tiempo    = (EditText) rootView.findViewById(R.id.TxtTiempo);
        resultadoVolumen = (TextView) rootView.findViewById(R.id.TxtResultadoVolumen);
        resultadoGotas = (TextView) rootView.findViewById(R.id.TxtResultadoGotas);

        btnCalcular =  (Button) rootView.findViewById(R.id.btnCalcular);
        btnEnviar   =  (Button) rootView.findViewById(R.id.btnEnviar);

        btnCalcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Double nVol,nTiempo,nResultado;

                nVol = Double.parseDouble(volumen.getText().toString());
                nTiempo = Double.parseDouble(tiempo.getText().toString());
                nResultado = nVol/(nTiempo/60);
                resultadoVolumen.setText(nResultado.toString()+" ml/Hr");
                System.out.println("Resultado " + nResultado);
                nResultado= (nVol * factorG)/(nTiempo*60);
                resultadoGotas.setText(nResultado.toString()+" Gotas/min");

            }
        });

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Conectarse con el web service para que envie el msj al enfermero a cargo
                //Se postea en el libro report
                String msj = new String();

                msj = "Administrar:xxxxxx \n"
                        +"Factor Goteo(ml x h): "+resultadoVolumen.getText()+"\n"
                        +"Factor Goteo(gotas x min): "+resultadoGotas.getText()+"\n"
                ;

            }
        });

        return rootView;

    }
}
