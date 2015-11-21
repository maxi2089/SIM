package com.example.maxi.sim;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by yamila on 04/11/2015.
 */
public class ListaVisitasFragment extends Fragment {
    private fragmentActivo fragActivo;
    private View rootView;
    private String URL;
    private Sesion sesion;
    private PacienteActivo pacienteActivo;
    private ArrayList<Visita> ListaVisitas;
    private ImageButton btnNuevaVisita;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        rootView = inflater.inflate(R.layout.fragment_lista_visitas, container, false);
        pacienteActivo = PacienteActivo.getInstance();
        ListaVisitas = new ArrayList<Visita>();
        fragActivo = fragmentActivo.getInstance();

        fragActivo.setData("PACIENTE_VISITAS");

        TextView txtPaciente = (TextView) rootView.findViewById(R.id.txtPaciente);

        txtPaciente.setText(pacienteActivo.getPaciente().getNombre() + " " + pacienteActivo.getPaciente().getApellido());

        //Calendar fechaActual = Calendar.getInstance();


        Visita visita = new Visita("10/10/2015 15:15","observacion 1 del pacietne chupa verga");
        Visita visita1 = new Visita("10/10/2015 15:15","observacion 2 del pacietne chupa verga");
        Visita visita2 = new Visita("10/10/2015 15:15","observacion 3 del pacietne chupa verga");

        ListaVisitas.add(visita);
        ListaVisitas.add(visita1);
        ListaVisitas.add(visita2);


        //Creamos el adpatator para la lista de eventos
        ArrayAdapter<Visita> adaptador = new visitasAdapter(rootView.getContext());
        //Configuramos la lista de eventos
        ListView ListVisitas = (ListView) rootView.findViewById(R.id.listVisitas);
        //Asignamos el adaptador a al lista de eventos
        ListVisitas.setAdapter(adaptador);
        ListVisitas.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("Psocion de la lsita "+ position);
                Fragment fragment = new VisitaFragment();

                if (fragment != null) {
                    Bundle datos = new Bundle();

                    datos.putString("FECHA",ListaVisitas.get(position).getFechaVisita().toString());
                    datos.putString("ANOTACION", ListaVisitas.get(position).getObservacion());

                    fragment.setArguments(datos);
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                } else {
                    //Si el fragment es nulo mostramos un mensaje de error.
                    Toast toast = Toast.makeText(rootView.getContext(),"No se pudo mostrar pantalla de modificar visita",Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
        ImageButton btnNuevaVisita = (ImageButton) rootView.findViewById(R.id.btnGuardar);

        btnNuevaVisita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment;
                fragment = new ProgramarVisitasFragment();
                if (fragment != null) {
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                } else {
                    Log.e("Error  ", "Crear Visita");
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
            System.out.println("POSITION "+position);
            Visita currentVisita = ListaVisitas.get(position);

            TextView txtFechaHora = (TextView)itemView.findViewById(R.id.txtFechaHora);
            txtFechaHora.setText(currentVisita.getFechaVisita().toString());

            TextView txtObservacion = (TextView)itemView.findViewById(R.id.txtObservacion);
            txtObservacion.setText(currentVisita.getObservacion());

            return itemView;
        }

    }
}
