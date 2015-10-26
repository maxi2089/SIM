package com.example.maxi.sim;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by yamila on 27/08/2015.
 */
public class LibroReportFragment extends Fragment {
    private Paciente pacienteReport;
    private String origen;
    private Button volverOrigen;
    //private ArrayList<Paciente> ListaPaciente;

    private TextView txtNombre;
    private TextView txtDNI;
    private TextView txtAltura;
    private TextView txtPeso;
    private TextView txtEdad;
    private TextView txtDiagnostico;
    private ArrayList<Evento> ListaEvento;
    private ImageButton btnAsignarReponsable;
    private ImageButton btnEliminar;
    private ImageButton btnModificar;
    private Sesion SesionUsuario;
    private PacienteActivo pacienteActivo;
    private  fragmentActivo fragActivo;
    private View rootView;

    private ListaPacientes ListaPaciente;

    private static final String URL = "http://192.168.0.3:8080/simWebService/resources/";

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        rootView = inflater.inflate(R.layout.fragment_report, container, false);

        //Instanciamos el objeto Lista de eventos
        ListaEvento = new ArrayList<Evento>();

        fragActivo =  fragmentActivo.getInstance();
        fragActivo.setData("REPORT");
        pacienteActivo = PacienteActivo.getInstance();



        getEventos(rootView.getContext());

        //Origen del fragment
         origen = (String) getArguments().getString("ORIGEN");

        //Configuramos los elementos visuales del report
        txtNombre = (TextView)rootView.findViewById(R.id.txtNombre);
        txtDNI = (TextView)rootView.findViewById(R.id.txtDNI);
        txtAltura = (TextView)rootView.findViewById(R.id.txtAltura);
        txtPeso = (TextView)rootView.findViewById(R.id.txtPeso);
        txtEdad = (TextView)rootView.findViewById(R.id.txtEdad);
        txtDiagnostico = (TextView)rootView.findViewById(R.id.txtDiagnostico);

        //Completamos los elementos visuales
        txtNombre.setText(pacienteActivo.getPaciente().getNombre()+" "+pacienteActivo.getPaciente().getApellido());
        txtDNI.setText("DNI: "+pacienteActivo.getPaciente().getDni());
        txtAltura.setText("Altura: "+pacienteActivo.getPaciente().getAltura().toString()+"m");
        txtPeso.setText("Peso: " +"90"+" Kg.");
        txtEdad.setText("Edad: "+"1.8");

       //txtPeso.setText("Peso: " + pacienteActivo.getPaciente().getAltura().toString()+" Kg.");
        //txtEdad.setText("Edad: "+pacienteActivo.getPaciente().getEdad().toString());

       //txtDiagnostico.setText("Diagnostico: "+pacienteActivo.getPaciente().getDiagnostico());
        txtDiagnostico.setText("Diagnostico: "+pacienteActivo.getPaciente().getNombre());

        //Creamos el adpatator para la lista de eventos
        ArrayAdapter<Evento> adaptador = new reportAdapter(rootView.getContext());
        //Configuramos la lista de eventos
        ListView listaEventos = (ListView) rootView.findViewById(R.id.listEventos);
        //Asignamos el adaptador a al lista de eventos
        listaEventos.setAdapter(adaptador);
        listaEventos.setOnItemClickListener((new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                Fragment fragment;

                fragment = new ModificarLibroReportFragment();

                if (fragment != null) {

                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                } else {
                    Log.e("Error  ", "Modificar Libro Report");
                }
            }
        }));



        SesionUsuario = Sesion.getInstance(1, null, null);

        if (fragActivo.getData().compareTo("REPORT") == 0
                /* && SesionUsuario.getUser().getRol().compareTo("ADMINISTRADOR")==0*/) {

            btnAsignarReponsable = (ImageButton) rootView.findViewById(R.id.btnAsignarReponsable);
            btnAsignarReponsable.setVisibility(View.VISIBLE);

            btnAsignarReponsable.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment fragment;

                    fragment = new GestorAsignacionesFragment();

                     if (fragment != null) {

                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                    } else {
                        Log.e("Error  ", "Asignar Reponsable");
                    }
                }
            });
        }

        btnEliminar = (ImageButton)rootView.findViewById(R.id.btnEliminar);

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EliminarReport eliminarReport = new EliminarReport();
                eliminarReport.show(getFragmentManager(), "Eliminar");
            }});

        btnModificar = (ImageButton)rootView.findViewById(R.id.btnModificar);

        btnModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment;

                fragment = new ModificarLibroReportFragment();

                if (fragment != null) {

                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                } else {
                    Log.e("Error  ", "Asignar Reponsable");
                }
            }});

        //Si el libro report fue abirto del menu de farmaco se crea un boton volver a farmaco
        if(origen.compareTo("FARMACO")== 0){
            fragActivo.setData("FARMACO_REPORT");
        }
        return rootView;
    }

    public class reportAdapter extends ArrayAdapter<Evento> {

        public reportAdapter(Context rootView){
            super(rootView,R.layout.evento_layout,ListaEvento);
        }

        public View getView(int position, View convertView, ViewGroup parent){
            View itemView = convertView;

            if(itemView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                itemView = inflater.inflate(R.layout.evento_layout,null);
            }


            Evento currentEvento = ListaEvento.get(position);

            TextView txtFecha = (TextView)itemView.findViewById(R.id.txtFecha);
            txtFecha.setText(currentEvento.getFecha().toString());

            TextView txtRegistro = (TextView)itemView.findViewById(R.id.txtRegistro);
            txtRegistro.setText(currentEvento.getRegistro());

            TextView txtResponsable = (TextView)itemView.findViewById(R.id.txtResponsable);
            txtResponsable.setText(currentEvento.getResponsable());

            return itemView;


        }


    }

    public void getEventos(Context context) {

        Evento evento;

       SimWebService service = new SimWebService();

        if (service.validarConexion(context)) {
            System.out.println("Red disponible");

            service.configurarMetodo("GET");
            service.configurarUrl(URL+"LibroReportResource?id="+pacienteActivo.getPaciente().getIdPaciente());

          if (service.conectar(context,1)) {
              String datos;
            //  datos = service.get();
            //  System.out.println("Datos: " + datos);

             // datos = "{"+"\""+"idLibroReport"+"\":"+"5"+","+"\""+"fechaAlta"+"\":"+"\""+"2015-10-11"+"\""+","+"\""+"estado"+"\":"+"\""+"ACTIVO"+"\""+","+"\""+"paciente"+"\":"+"{"+"\""+"idPaciente"+"\":"+"5"+","+"\""+"dni"+"\":"+"34809913"+","+"\""+"nombre"+"\":"+"\""+"Maxi"+"\""+","+"\""+"apellido"+"\":"+"\""+"Akike"+"\""+","+"\""+"edad"+"\":"+"26"+","+"\""+"altura"+"\":"+"1.75"+","+"\""+"peso"+"\""+":"+"1.8"+"}"+","+"\""+"medicions"+"\""+":"+"["+"{"+"\""+"fecha"+"\""+":"+"\""+"2015-10-11"+"\""+","+"\""+"descripcion"+"\""+":"+"\""+"oxigeno"+"\""+","+"\""+"oxigenoEnSangre"+"\""+":"+88.0+"}"+"]"+"}";
              datos = "{"+"\""+"idLibroReport"+"\":"+"5"+","+"\""+"fechaAlta"+"\":"+"\""+"2015-10-11"+"\""+","+"\""+"estado"+"\":"+"\""+"ACTIVO"+"\""+","+"\""+"paciente"+"\":"+"{"+"\""+"idPaciente"+"\":"+"5"+","+"\""+"dni"+"\":"+"34809913"+","+"\""+"nombre"+"\":"+"\""+"Maxi"+"\""+","+"\""+"apellido"+"\":"+"\""+"Akike"+"\""+","+"\""+"edad"+"\":"+"26"+","+"\""+"altura"+"\":"+"1.75"+","+"\""+"peso"+"\""+":"+"1.8"+"}"+","+"\""+"medicions"+"\""+":"+"["+"{"+"\""+"fecha"+"\""+":"+"\""+"2015-10-11"+"\""+","+"\""+"descripcion"+"\""+":"+"\""+"oxigeno"+"\""+","+"\""+"oxigenoEnSangre"+"\""+":"+88.0+"}"+","+"{"+"\""+"fecha"+"\""+":"+"\""+"2015-10-11"+"\""+","+"\""+"descripcion"+"\""+":"+"\""+"oxigeno"+"\""+","+"\""+"oxigenoEnSangre"+"\""+":"+88.0+"}"+","+"{"+"\""+"fecha"+"\""+":"+"\""+"2015-10-11"+"\""+","+"\""+"descripcion"+"\""+":"+"\""+"oxigeno"+"\""+","+"\""+"oxigenoEnSangre"+"\""+":"+88.0+"}"+"]"+"}";
              System.out.println("Datos: " + datos);

              Gson gson = new GsonBuilder()
                      .setDateFormat("yyyy-MM-dd")
                      .create();

              LibroReport libroreport = gson.fromJson(datos, LibroReport.class);
              System.out.println("Paciente: " + libroreport.getPaciente().getApellido());

              if (!libroreport.getMedicions().isEmpty()) {

                  for (Iterator iterador = libroreport.getMedicions().iterator(); iterador.hasNext(); ) {
                      evento = new Evento();
                      MedicionDto medDto = (MedicionDto) iterador.next();

                      if (medDto.getTemperatura() != null) {

                          evento.setFecha(medDto.getFecha().toString());
                          evento.setRegistro("Temperatura: " + medDto.getTemperatura() + " ºC");
                          evento.setResponsable(medDto.getDescripcion());

                      } else {
                          if ((medDto.getGlucosa() != null) && !(medDto.getGlucosa().isEmpty())) {
                              evento.setFecha(medDto.getFecha().toString());
                              evento.setRegistro("Nivel de Glucosa " + medDto.getGlucosa());
                              evento.setResponsable(medDto.getDescripcion());
                          } else {
                              if ((medDto.getFreceunciaRespiratoria() != null) && !(medDto.getFreceunciaRespiratoria().isEmpty())) {
                                  evento.setFecha(medDto.getFecha().toString());
                                  evento.setRegistro("Frecuencia Respiratoria: " + medDto.getFreceunciaRespiratoria() + " Respiraciones/Min");
                                  evento.setResponsable(medDto.getDescripcion());

                              } else {

                                  if (medDto.getOxigenoEnSangre() != null) {

                                      evento.setFecha(medDto.getFecha().toString());
                                      evento.setRegistro("Saturometria: " + medDto.getOxigenoEnSangre() + " SpO2");
                                      evento.setResponsable(medDto.getDescripcion());
                                      System.out.println("Oxigeno: " + medDto.getDescripcion());


                                  } else if (medDto.getTensionArterial() != null) {
                                      evento.setFecha(medDto.getFecha().toString());
                                      evento.setRegistro("Tension Arterial: " + medDto.getTensionArterial());
                                      evento.setResponsable(medDto.getDescripcion());
                                  }

                              }

                          }
                      }
                      ListaEvento.add(evento);
                  }

              }else{
                  evento = new Evento();
                  evento.setFecha("No hay datos");
                  evento.setRegistro("No hay datos");
                  evento.setResponsable("No hay datos");
                  ListaEvento.add(evento);
              }

          }else{
              evento = new Evento();
              evento.setFecha("No hay datos");
              evento.setRegistro("No hay datos");
              evento.setResponsable("No hay datos");
              ListaEvento.add(evento);
            }
        } else {

            evento = new Evento();
            evento.setFecha("No hay datos");
            evento.setRegistro("No hay datos");
            evento.setResponsable("No hay datos");
            ListaEvento.add(evento);
            System.out.println("Red No disponible");

        }
    }

    private boolean deleteLibroReport(Context context){
        SimWebService service = new SimWebService();

        if (service.validarConexion(context)) {
            System.out.println("Red disponible");

            service.configurarMetodo("DELETE");
            service.configurarUrl(URL+"LibroReportResource?id="+pacienteActivo.getPaciente().getIdPaciente());

            if (service.conectar(context, 1)) {
                service.delete();
            }else
                return false;
        }else
            return false;
        return true;
    }


    @SuppressLint("ValidFragment")
    public class EliminarReport extends DialogFragment {
        private  Context context;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            AlertDialog.Builder builder =
                    new AlertDialog.Builder(getActivity());
            String mensaje  ="Desea eliminar el Libro Report del paciente "
                    + pacienteActivo.getPaciente().getNombre()
                    + " " + pacienteActivo.getPaciente().getApellido() + "?";
            context = rootView.getContext();

            builder.setMessage(mensaje)
                    .setTitle( "Eliminar Libro Report")
                    .setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Log.i("Dialogos", "Confirmacion Aceptada.");

                            Fragment fragment;
                            fragment = new ListaPacientesFragment();
                            if (fragment != null) {
                                if(deleteLibroReport(context)) {

                                    fragmentActivo fragActivo = fragmentActivo.getInstance();
                                    fragActivo.setData("LISTA_REPORT");
                                    FragmentManager fragmentManager = getFragmentManager();
                                    fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                                    dialog.cancel();
                                }
                        }
                    }})
                    .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Log.i("Dialogos", "Confirmacion Cancelada.");
                            dialog.cancel();
                        }
                    });

            return builder.create();
        }


    }


}