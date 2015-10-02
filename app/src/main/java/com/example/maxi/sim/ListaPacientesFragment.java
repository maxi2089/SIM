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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by yamila on 04/09/2015.
 */
public class ListaPacientesFragment  extends Fragment {
   // private ArrayList<Paciente> ListaPaciente;
   private ListaPacientes ListaPaciente;

    private  String origen;



    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
         View rootView = inflater.inflate(R.layout.fragment_lista_paciente, container, false);

        origen = (String) getArguments().getString("ORIGEN");
        //ListaPaciente = (ArrayList<Paciente>) getArguments().getSerializable("LISTA");
        ListaPaciente = ListaPacientes.getInstance();


        ArrayAdapter<Paciente> adaptador = new pacienteAdapter(rootView.getContext());
        ListView listaPaciente = (ListView) rootView.findViewById(R.id.listView);
        listaPaciente.setAdapter(adaptador);

        listaPaciente.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                Fragment fragment = new LibroReportFragment();
                if (fragment != null) {
                    System.out.println("ESTOY EN LISTA" + ListaPaciente.getLista().get(position).getApellido());

                    Bundle  datos = new Bundle();

                    datos.putSerializable("PACIENTE", ListaPaciente.getLista().get(position));
                    datos.putSerializable("LISTA", ListaPaciente.getLista());
                    datos.putString("ORIGEN", "ListaPacientesFragment");

                    fragment.setArguments(datos);

                    FragmentManager fragmentManager = getFragmentManager();

                    fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                } else {
                    //Si el fragment es nulo mostramos un mensaje de error.
                    Log.e("Error  ", "MostrarFragment" + position);
                }
            }

        });
        return rootView;
    }

        public class pacienteAdapter extends ArrayAdapter<Paciente> {

            public pacienteAdapter(Context rootView){
                super(rootView,R.layout.paciente_layout,ListaPaciente.getLista());
            }

            public View getView(int position, View convertView, ViewGroup parent){
                View itemView = convertView;

                if(itemView == null) {
                    LayoutInflater inflater = LayoutInflater.from(getContext());
                    itemView = inflater.inflate(R.layout.paciente_layout,null);
                }


                Paciente currentPaciente = ListaPaciente.getLista().get(position);

                ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
                //imageView.setImageResource(currentPaciente.getImagen());

                TextView nombreText = (TextView)itemView.findViewById(R.id.nombre);
                nombreText.setText(currentPaciente.getNombre()+" "+currentPaciente.getApellido());

                TextView diagnosticoText = (TextView)itemView.findViewById(R.id.diagnostico);

                //diagnosticoText.setText(currentPaciente.getDiagnostico());
                diagnosticoText.setText(currentPaciente.getApellido());
                return itemView;


            }


        }
}
