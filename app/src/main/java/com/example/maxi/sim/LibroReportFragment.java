package com.example.maxi.sim;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**texto
 * Created by yamila on 27/08/2015.
 */
public class LibroReportFragment extends Fragment {
    private TextView tex;
    private  Paciente pacienteReport;
    private  String origen;
    private Button volverOrigen;
    private ArrayList<Paciente> ListaPaciente;



    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        View rootView = inflater.inflate(R.layout.fragment_report, container, false);
        tex = (TextView)rootView.findViewById(R.id.texto);

        pacienteReport = (Paciente)getArguments().getSerializable("PACIENTE");
        origen = (String) getArguments().getString("ORIGEN");
        ListaPaciente = (ArrayList<Paciente>) getArguments().getSerializable("LISTA");

        if(origen.compareTo("FarmacoFragment")== 0){

            volverOrigen = (Button) rootView.findViewById(R.id.btnVolver);
            volverOrigen.setVisibility(View.VISIBLE);

            volverOrigen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment fragment = null;
                    fragment = new FarmacoFragment();

                    if (fragment != null) {
                        Bundle pacientes = new Bundle();
                        pacientes.putSerializable("LISTA",ListaPaciente);
                        pacientes.putSerializable("PACIENTE",pacienteReport);
                        pacientes.putString("ORG","LIBRO_REPORT");

                        fragment.setArguments(pacientes);
                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

                    } else {
                        //Si el fragment es nulo mostramos un mensaje de error.
                        Log.e("Error  ", "No se puedo volver al fragment");
                    }
                }
            });


        }
        if(pacienteReport != null){
            tex.setText(pacienteReport.getNombre());
        }
        else{
            tex.setText("Holaaaaa");
        }

        return rootView;
    }
}