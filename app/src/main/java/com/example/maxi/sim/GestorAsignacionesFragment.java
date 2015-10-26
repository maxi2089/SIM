package com.example.maxi.sim;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

/**
 * Created by yamila on 21/10/2015.
 */
public class GestorAsignacionesFragment extends Fragment {

    private PacienteActivo pacienteActivo;
    private ArrayList<String> ListaResponsables;
    private Paciente paciente;
    private Button btnGuardar;
    private ListView ListaResponsableView;
    private ArrayAdapter<String> adapter;
    private TextView txtPaciente;
    private ArrayList<Integer> ListaResponsablesAsignados;
    private ArrayList<Integer> ListaResponsablesNoAsignados;
    private static final String URL = "http://192.168.0.3:8080/simWebService/resources/";


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        View rootView = inflater.inflate(R.layout.fragment_gestionar_asignaciones, container, false);

        //Guardar la pantalla que estoy para poder armar el mapa de navegacion
        fragmentActivo fragActivo =  fragmentActivo.getInstance();
        pacienteActivo = PacienteActivo.getInstance();
        fragActivo.setData("ASIGNAR_REPONSABLE");
        //pacienteActivo = (Paciente)getArguments().getSerializable("PACIENTE");


        ListaResponsablesAsignados = new  ArrayList<Integer>();
        ListaResponsablesNoAsignados = new  ArrayList<Integer>();
        //Definicion de la lista de responsables
        ListaResponsableView = (ListView) rootView.findViewById(R.id.listaPacienteAsignacion);

        //Button para guardar las asignaciones
        btnGuardar = (Button)rootView.findViewById(R.id.btnGuardar);

        //Paciente Seleccionado
        txtPaciente = (TextView)rootView.findViewById(R.id.txtPaciente);
        txtPaciente.setText(pacienteActivo.getPaciente().getNombre()+" "+pacienteActivo.getPaciente().getApellido());

        ListaResponsables = new ArrayList<String>();
        getResponsables(rootView.getContext());

        adapter = new ArrayAdapter<String>(rootView.getContext(),android.R.layout.simple_list_item_multiple_choice, ListaResponsables);
        ListaResponsableView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        ListaResponsableView.setAdapter(adapter);

        //Cheack los repsonables que ya estan asignados
        for (int i = 0; i < 2; i++) {
            ListaResponsableView.setItemChecked(i,true);
        }


        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SparseBooleanArray checked = ListaResponsableView.getCheckedItemPositions();
                ArrayList<String> responsableAsignar = new ArrayList<String>();
                ArrayList<String> responsableBorrar = new ArrayList<String>();

                int asignado;

                //POST - ASIGNADOS
                for (int i = 0; i < checked.size(); i++) {
                    // Item position in adapter
                    int position = checked.keyAt(i);
                    // Add sport if it is checked i.e.) == TRUE!
                    if (checked.valueAt(i)) {

                        String responsable[] = adapter.getItem(position).split("-");
                        asignado = ListaResponsablesAsignados.indexOf(Integer.parseInt(responsable[0]));

                        if(asignado == -1) {
                            responsableAsignar.add(responsable[0]);
                            System.out.println("Responsable a Asignar "+ adapter.getItem(position));
                        }
                    }
                }

                //DELETE - DES ASIGNADOS
                for(int i = 0; i < ListaResponsablesAsignados.size();i++){
                    asignado = 0;
                    for (int j = 0; j < checked.size(); j++) {
                        // Item position in adapter
                        int posicion = checked.keyAt(j);
                        if (checked.valueAt(j)) {
                            String responsable[] = adapter.getItem(posicion).split("-");
                            if(ListaResponsablesAsignados.get(i) == Integer.parseInt(responsable[0])){
                                j = checked.size();
                                asignado = 1;
                            }
                        }
                    }
                    if(asignado == 0){
                     responsableBorrar.add(ListaResponsablesAsignados.get(i).toString());
                        System.out.println("Responsable a Borra " + ListaResponsablesAsignados.get(i).toString());

                    }
                }






            }

        });
        return rootView;
    }

     public void getResponsables(Context context){

         SimWebService service = new SimWebService();
         if(service.validarConexion(context)){
             System.out.println("Red disponible");

             service.configurarMetodo("GET");
             service.configurarUrl(URL+"PacienteResource/asignacion?id=1"/*+pacienteActivo.getPaciente().getIdPaciente()*/);

             if(service.conectar(context,0)) {
                 String datos;
                 datos = service.get();
                 Gson gson = new GsonBuilder()
                         .setDateFormat("yyyy-MM-dd")
                         .create();
                 System.out.println("GEEET " + datos);

                 paciente  = gson.fromJson(datos, Paciente.class);

                 System.out.println(paciente.getNombre() + " " + paciente.getApellido());


                 paciente.setNombre("Juan");
                 ListaResponsables.add("1- " + paciente.getNombre() + " " + paciente.getApellido());
                 paciente.setNombre("Ernesto");
                 ListaResponsables.add("2- " + paciente.getNombre() + " " + paciente.getApellido());
                 paciente.setNombre("Miguel");
                 ListaResponsables.add("3-" + paciente.getNombre() + " " + paciente.getApellido());

                 paciente.setNombre("Juan");
                 ListaResponsablesAsignados.add(1);
                 paciente.setNombre("Ernesto");
                 ListaResponsablesAsignados.add(2);

                 paciente.setNombre("Miguel");
                 ListaResponsablesNoAsignados.add(3);
             }
             else {
                 // ListaPaciente.setLista(paciente);

                 //Listapaciente.add(paciente);

             }
         }
         else{
             System.out.println("Red No disponible");
             //  ListaPaciente.setLista(paciente);

             // Listapaciente.add(paciente);


         }
     }
}
