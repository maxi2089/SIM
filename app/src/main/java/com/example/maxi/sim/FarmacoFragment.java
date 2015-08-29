package com.example.maxi.sim;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
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
import android.widget.Toast;

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
    private ImageButton btnReport;
    private Integer libroReportFlag;
    private String org;
    private TextView pruebaT;
    public FarmacoFragment() {}



    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        View rootView = inflater.inflate(R.layout.fragment_farmaco,container,false);


        ListaPaciente = (ArrayList<Paciente>)getArguments().getSerializable("LISTA");

        spinnerPaciente = (Spinner)rootView.findViewById(R.id.listaPaciente);
        btnReport = (ImageButton) rootView.findViewById(R.id.libroReport);
        List<String> pacientes = new  ArrayList<String>();

        pacientes.add("Pacientes Asigandos");

        for(int i=0;i<ListaPaciente.size();i++){
            pacientes.add(ListaPaciente.get(i).getID()+" - "+ListaPaciente.get(i).getNombre()+' '+ListaPaciente.get(i).getApellido());
        }

        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(rootView.getContext(),android.R.layout.simple_spinner_item,pacientes);

        spinnerPaciente.setAdapter(adaptador);

        pruebaT = (TextView)rootView.findViewById(R.id.pruebaT);

        org = (String)getArguments().getString("ORG");

        if(org.compareTo("LIBRO_REPORT")==0){
            pacienteActivo = (Paciente)getArguments().getSerializable("PACIENTE");
            pruebaT.setText(pacienteActivo.getNombre());
        }

        spinnerPaciente.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                       long arg3) {
                if (arg2 != 0) {
                    //Posicion del spinner debe coincidir con la posicion de la lista de pacientes..
                    pacienteActivo = ListaPaciente.get(arg2 - 1);
                    pruebaT.setText(pacienteActivo.getNombre());

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

                //Obtenemos los valores ingresados de volumen y tiempo
                nVol = Double.parseDouble(volumen.getText().toString());
                nTiempo = Double.parseDouble(tiempo.getText().toString());

                //Calculamos el resultado en  ml/hr
                nResultado = nVol/(nTiempo/60);
                resultadoVolumen.setText(nResultado.toString()+" ml/Hr");
                System.out.println("Resultado " + nResultado);

                //Calculamos el resultado en  gotas/min
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

        btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              MostrarFragment();
            }
        });
        return rootView;

    }





    private void MostrarFragment() {
        // update the main content by replacing fragments
        Fragment fragment =  new LibroReportFragment();
        if(pacienteActivo != null) {

            //Validamos si el fragment no es nulo
            if (fragment != null) {

                //Creamos  Bundle que guardara los datos para enviar al fragment de paciente
                Bundle  datos = new Bundle();

                datos.putSerializable("PACIENTE", pacienteActivo);
                datos.putSerializable("LISTA", ListaPaciente);
                datos.putString("ORIGEN", "FarmacoFragment");

                fragment.setArguments(datos);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
            } else {
                //Si el fragment es nulo mostramos un mensaje de error.
                Log.e("Error  ", "No se puedo encontrara el fragmento LibroReportFragment");
            }
        }
        else{
            Log.e("Error  ", "No seleecion paciente");

        }
    }
}
