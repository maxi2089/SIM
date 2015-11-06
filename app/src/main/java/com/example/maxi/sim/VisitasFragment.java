package com.example.maxi.sim;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by yamila on 04/11/2015.
 */
public class VisitasFragment extends Fragment {
    private fragmentActivo fragActivo;
    private View rootView;
    private String URL;
    private Sesion sesion;
    private PacienteActivo pacienteActivo;
    private ArrayList<Visita> ListaVisitas;
    private ImageButton btnNuevaVisita;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        rootView = inflater.inflate(R.layout.fragment_visitas, container, false);
        pacienteActivo = PacienteActivo.getInstance();
        ListaVisitas = new ArrayList<Visita>();
        fragActivo = fragmentActivo.getInstance();

        fragActivo.setData("PACIENTE_VISITAS");

        TextView txtPaciente = (TextView) rootView.findViewById(R.id.txtPaciente);

        txtPaciente.setText(pacienteActivo.getPaciente().getNombre() + " " + pacienteActivo.getPaciente().getApellido());
        //Creamos el adpatator para la lista de eventos
        ArrayAdapter<Visita> adaptador = new visitasAdapter(rootView.getContext());
        //Configuramos la lista de eventos
        ListView ListVisitas = (ListView) rootView.findViewById(R.id.listVisitas);
        //Asignamos el adaptador a al lista de eventos
        ListVisitas.setAdapter(adaptador);

        ImageButton btnNuevaVisita = (ImageButton) rootView.findViewById(R.id.btnCrearVisita);

        btnNuevaVisita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment;
                fragment = new ProgramarVisitasFragment();
                if (fragment != null) {
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                } else {
                    Log.e("Error  ", "Crear Libro Report");
                }
            }
        });
        return rootView;
    }

    public class visitasAdapter extends ArrayAdapter<Visita> {

        public visitasAdapter(Context rootView){
            super(rootView,R.layout.visita_layout, ListaVisitas);
        }

        public View getView(int position, View convertView, ViewGroup parent){
            View itemView = convertView;

            if(itemView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                itemView = inflater.inflate(R.layout.visita_layout,null);
            }
            Visita currentVisita = ListaVisitas.get(position);

            TextView txtFechaHora = (TextView)itemView.findViewById(R.id.txtFecha);
            txtFechaHora.setText(currentVisita.getFechaVisita().toString());

            TextView txtObservacion = (TextView)itemView.findViewById(R.id.txtObservacion);
            txtObservacion.setText(currentVisita.getObservacion());

            return itemView;
        }

    }
}
