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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by yamila on 21/10/2015.
 */
public class GestorAsignacionesFragment extends Fragment {

    private PacienteActivo pacienteActivo;
    private ArrayList<String> ListaResponsables;
    private Paciente paciente;
    private ImageButton btnGuardar;
    private ListView ListaResponsableView;
    private ArrayAdapter<String> adapter;
    private TextView txtPaciente;
    private ArrayList<Integer> ListaResponsablesAsignados;
    private ArrayList<Integer> ListaResponsablesNoAsignados;
    private ArrayList<String> responsableAsignar;
    private ArrayList<String> responsableBorrar;
    private int cantAsignados=0;
    //private static final String URL = "http://192.168.0.3:8080/simWebService/resources/";
    private View rootView;
    private String URL;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        rootView = inflater.inflate(R.layout.fragment_gestionar_asignaciones, container, false);

        //Guardar la pantalla que estoy para poder armar el mapa de navegacion
        fragmentActivo fragActivo =  fragmentActivo.getInstance();
        pacienteActivo = PacienteActivo.getInstance();
        fragActivo.setData("ASIGNAR_REPONSABLE");

        Url urlServer = Url.getInstance();
        URL = urlServer.getUrl();


        ListaResponsablesAsignados = new  ArrayList<Integer>();
        ListaResponsablesNoAsignados = new  ArrayList<Integer>();
        //Definicion de la lista de responsables
        ListaResponsableView = (ListView) rootView.findViewById(R.id.listaPacienteAsignacion);

        //Button para guardar las asignaciones
        btnGuardar = (ImageButton)rootView.findViewById(R.id.btnGuardar);

        //Paciente Seleccionado
        txtPaciente = (TextView)rootView.findViewById(R.id.txtPaciente);
        txtPaciente.setText(pacienteActivo.getPaciente().getNombre()+" "+pacienteActivo.getPaciente().getApellido());

        ListaResponsables = new ArrayList<String>();
        getResponsables(rootView.getContext());

        adapter = new ArrayAdapter<String>(rootView.getContext(),android.R.layout.simple_list_item_multiple_choice, ListaResponsables);
        ListaResponsableView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        ListaResponsableView.setAdapter(adapter);

        //Cheack los repsonables que ya estan asignados
        for (int i = 0; i < cantAsignados; i++) {
            ListaResponsableView.setItemChecked(i,true);
        }


        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SparseBooleanArray checked = ListaResponsableView.getCheckedItemPositions();
                responsableAsignar = new ArrayList<String>();
                responsableBorrar = new ArrayList<String>();

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
                        System.out.println("Responsable a Borrar " + ListaResponsablesAsignados.get(i).toString());

                    }
                }
                int noHuboCambio = 0;
                if(!responsableAsignar.isEmpty()) {
                    try {
                        asignarUsuario();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    noHuboCambio = 1;
                }

                if(!responsableBorrar.isEmpty()) {
                      //  try {
                           // eliminarAsignacion();
                            System.out.println("BORRANDO");
                        //} catch (IOException e) {
                          //  e.printStackTrace();
                        //}
                 }else{
                    if(noHuboCambio==1){
                        Toast toast = Toast.makeText(rootView.getContext(),"No se registraron cambios de asignacion",Toast.LENGTH_LONG);
                        toast.show();
                    }
                }


            }

        });
        return rootView;
    }
    public void asignarUsuario() throws IOException {

        StringBuilder datosJson = new StringBuilder();

        datosJson.append("{"+"\""+"idPaciente"+"\""+":"+pacienteActivo.getPaciente().getIdPaciente());
        datosJson.append(","+"\""+"idsUsuario"+"\""+":[");

        for(int i=0 ; i < responsableAsignar.size();i++) {
            datosJson.append(responsableAsignar.get(i).toString());
            if(i < responsableAsignar.size()-1)
                datosJson.append(",");
        }

        datosJson.append("]}");

        postAsignacion(rootView.getContext(),datosJson);

    }

    public void eliminarAsignacion() throws IOException {
        for(int i=0 ; i < responsableBorrar.size();i++) {
            deleteAsignacion(rootView.getContext(),responsableBorrar.get(i));
        }
    }
    public void getResponsables(Context context){

         SimWebService service = new SimWebService();
         if(service.validarConexion(context)){
             System.out.println("Red disponible");

             service.configurarMetodo("GET");
             service.configurarUrl(URL+"PacienteResource/asignacion?id="+pacienteActivo.getPaciente().getIdPaciente());

             if(service.conectar(context,0)) {
                 String datos;
                 datos = service.get();
                 Gson gson = new GsonBuilder()
                         .setDateFormat("yyyy-MM-dd")
                         .create();
                 System.out.println("GET " + datos);

                 AsignacionPaciente asignacionPaciente = gson.fromJson(datos, AsignacionPaciente.class);

                 Iterator<Usuario> it = asignacionPaciente.getUsuariosAsignados().iterator();

                 //Usuarios Asignados
                 while(it.hasNext()){
                     Usuario usuarioAsignado = (Usuario)it.next();
                     ListaResponsables.add(usuarioAsignado.getIdUsuario().toString()+"-"+usuarioAsignado.getNombre());
                     ListaResponsablesAsignados.add(usuarioAsignado.getIdUsuario());
                     System.out.println("usuario asginado : "+usuarioAsignado.getIdUsuario());
                 }
                 if(!ListaResponsables.isEmpty()) {
                     cantAsignados = ListaResponsables.size();
                 }

                 Iterator<Usuario> it2 = asignacionPaciente.getUsuariosNoAsignados().iterator();
                 //Usuarios No Asignados
                 while(it2.hasNext()){
                     Usuario usuarioNoAsignado = (Usuario)it2.next();
                     ListaResponsables.add(usuarioNoAsignado.getIdUsuario().toString()+"-"+usuarioNoAsignado.getNombre());
                     ListaResponsablesNoAsignados.add(usuarioNoAsignado.getIdUsuario());
                     System.out.println("usuario no asginado : " + usuarioNoAsignado.getIdUsuario());

                 }

             }

         }

     }

    private void postAsignacion(Context Context, StringBuilder datos) throws IOException {

        SimWebService service = new SimWebService();

        if (service.validarConexion(Context)) {
            System.out.println("Red disponible");

            service.configurarMetodo("POST");
            service.configurarUrl(URL+"PacienteResource/asignacion");

            if (service.conectar(Context,datos.toString().getBytes().length)) {
                System.out.println("Datos "+"\n"+datos);
                service.post(datos.toString());
                System.out.println("-------------");

            }
        }
        else{
            System.out.println("Red No disponible");
            Toast toast = Toast.makeText(Context, "Red No Disponible", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    private void deleteAsignacion(Context Context, String idUsuario) throws IOException {

        SimWebService service = new SimWebService();

        if (service.validarConexion(Context)) {
            System.out.println("Red disponible");

            service.configurarMetodo("DELETE");
            service.configurarUrl(URL+"PacienteResource/asignacion?IdPaciente="
                    +pacienteActivo.getPaciente().getIdPaciente()
                    +"&idUsuario="+idUsuario);

            if (service.conectar(Context,1)) {
                System.out.println("Borrar "+"\n"+idUsuario);
                service.delete();
                System.out.println("-------------");

            }
        }
        else{
            System.out.println("Red No disponible");
            Toast toast = Toast.makeText(Context, "Red No Disponible", Toast.LENGTH_LONG);
            toast.show();
        }
    }
}
