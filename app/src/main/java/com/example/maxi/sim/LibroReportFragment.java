package com.example.maxi.sim;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**texto
 * Created by yamila on 27/08/2015.
 */
public class LibroReportFragment extends Fragment {
    private Paciente pacienteReport;
    private String origen;
    private Button volverOrigen;
    //private ArrayList<Paciente> ListaPaciente;
    private ListaPacientes ListaPaciente;

    private TextView txtNombre;
    private TextView txtDNI;
    private TextView txtAltura;
    private TextView txtPeso;
    private TextView txtEdad;
    private TextView txtDiagnostico;
    private ArrayList<Evento> ListaEvento;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        View  rootView = inflater.inflate(R.layout.fragment_report, container, false);
        fragmentActivo fragActivo = fragmentActivo.getInstance();

        fragActivo.setData("LIBROREPORT");
        //Instanciamos el objeto Lista de eventos
        ListaEvento = new ArrayList<Evento>();

        //PRUEBA
       // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
      //  SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
       // String currentDateandTime = sdf.format(new Date());
        //Evento evento = new Evento(currentDateandTime,"Se registro 35 mm de ibuprofeno xxxxxxxxxxxx","Debora Puebla");

      /*  ListaEvento.add(evento);
        ListaEvento.add(evento);
        ListaEvento.add(evento);
        ListaEvento.add(evento);*/

        TextView dat = (TextView)rootView.findViewById(R.id.textView3);
        String datos = new String();
        datos = dat.getText().toString();
        //PRUEBA

        getEventos(rootView.getContext(),datos);

        //Argumentos provenientes del Fragmento de Lista de Pacientes
        //Paciente Seleccionado
        pacienteReport = (Paciente)getArguments().getSerializable("PACIENTE");
        //Origen del fragment
        origen = (String) getArguments().getString("ORIGEN");
        //Lista de Paciente asignados al usuario
        ListaPaciente = ListaPacientes.getInstance();
        //ListaPaciente = (ArrayList<Paciente>) getArguments().getSerializable("LISTA");

        //Configuramos los elementos visuales del report
        txtNombre = (TextView)rootView.findViewById(R.id.txtNombre);
        txtDNI = (TextView)rootView.findViewById(R.id.txtDNI);
        txtAltura = (TextView)rootView.findViewById(R.id.txtAltura);
        txtPeso = (TextView)rootView.findViewById(R.id.txtPeso);
        txtEdad = (TextView)rootView.findViewById(R.id.txtEdad);
        txtDiagnostico = (TextView)rootView.findViewById(R.id.txtDiagnostico);

        //Completamos los elementos visuales
        txtNombre.setText(pacienteReport.getNombre()+" "+pacienteReport.getApellido());
        txtDNI.setText("DNI: "+pacienteReport.getDni());
        txtAltura.setText("Altura: "+pacienteReport.getAltura().toString());
        txtPeso.setText("Peso:  " + pacienteReport.getPeso().toString());
        txtEdad.setText("Edad: "+pacienteReport.getEdad().toString());
       // txtDiagnostico.setText("Diagnostico: "+pacienteReport.getDiagnostico());
        txtDiagnostico.setText("Diagnostico: "+pacienteReport.getNombre());

        //Creamos el adpatator para la lista de eventos
        ArrayAdapter<Evento> adaptador = new reportAdapter(rootView.getContext());
        //Configuramos la lista de eventos
        ListView listaEventos = (ListView) rootView.findViewById(R.id.listEventos);
        //Asignamos el adaptador a al lista de eventos
        listaEventos.setAdapter(adaptador);
        listaEventos.setOnItemClickListener((new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {

            }
        }));
        //Si el libro report fue abirto del menu de farmaco se crea un boton volver a farmaco
        if(origen.compareTo("FarmacoFragment")== 0){

            volverOrigen = (Button) rootView.findViewById(R.id.btnVolver);
            volverOrigen.setVisibility(View.VISIBLE);

            volverOrigen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment fragment = null;
                    fragment = new FarmacoFragment();

                    if (fragment != null) {
                        volverOrigen.setVisibility(View.INVISIBLE);

                        Bundle pacientes = new Bundle();
                        pacientes.putSerializable("LISTA",ListaPaciente.getLista());
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
       /* if(pacienteReport != null){

        }
        else{
        }*/


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

    public void getEventos(Context context,String dat) {

        Evento evento;

       /* ServiceActiviy service = new ServiceActiviy();

        if (service.validarConexion(context)) {
            System.out.println("Red disponible");

            service.configurarMetodo("GET");
            service.configurarUrl("http://192.168.0.4:8080/simWebService/resources/LibroReportResource?id=1");
*/
          // if (service.conectar(context)) {
                String datos;
                //datos = service.get();
                datos = dat;
                try {

                    JSONObject objeto = new JSONObject(datos);

                    Gson gson = new GsonBuilder()
                            .setDateFormat("yyyy-MM-dd")
                            .create();

                    LibroReport libroreport = gson.fromJson(datos, LibroReport.class);
                    System.out.println("Datos: " + libroreport.getPaciente().getApellido());

                    if(!libroreport.getMedicions().isEmpty()){

                        for ( Iterator iterador = libroreport.getMedicions().iterator(); iterador.hasNext(); ) {
                            evento = new Evento();
                            MedicionDto medDto =(MedicionDto) iterador.next();

                            if( medDto.getTemperatura()!= null){

                                evento.setFecha(medDto.getFecha().toString());
                                evento.setRegistro("Temperatura: " + medDto.getTemperatura() + " ºC");
                                evento.setResponsable(medDto.getDescripcion());

                            }else{
                                if((medDto.getGlucosa()!=null) && !(medDto.getGlucosa().isEmpty())) {
                                    evento.setFecha(medDto.getFecha().toString());
                                    evento.setRegistro("Nivel de Glucosa " + medDto.getGlucosa());
                                    evento.setResponsable(medDto.getDescripcion());
                                }else{
                                    if( (medDto.getFreceunciaRespiratoria()!=null) &&  !(medDto.getFreceunciaRespiratoria().isEmpty())){
                                        evento.setFecha(medDto.getFecha().toString());
                                        evento.setRegistro("Frecuencia Respiratoria: " + medDto.getFreceunciaRespiratoria() + " Respiraciones/Min");
                                        evento.setResponsable(medDto.getDescripcion());

                                    }else{

                                        if(medDto.getOxigenoEnSangre()!=null){

                                            evento.setFecha(medDto.getFecha().toString());
                                            evento.setRegistro("Saturometria: " + medDto.getOxigenoEnSangre() + " SpO2");
                                            evento.setResponsable(medDto.getDescripcion());

                                        }else
                                        if(medDto.getTensionArterial()!=null){
                                            evento.setFecha(medDto.getFecha().toString());
                                            evento.setRegistro("Tension Arterial: " + medDto.getTensionArterial());
                                            evento.setResponsable(medDto.getDescripcion());
                                        }

                                    }

                                }
                            }
                            ListaEvento.add(evento);
                       }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

           /*}
        } else {
            System.out.println("Red No disponible");
            Toast toast = Toast.makeText(context,"Red No Disponible",Toast.LENGTH_LONG);
            toast.show();
        }*/
    }



}