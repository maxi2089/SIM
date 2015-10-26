package com.example.maxi.sim;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by yamila on 04/09/2015.
 */
public class ListaPacientesFragment  extends Fragment {
    private ListaPacientes ListaPaciente;
    private fragmentActivo fragActivo;
    private ImageButton btnCrearLibReport;
    private String origen;
    private Sesion SesionUsuario;
    private PacienteActivo pacienteActivo;
    private  ListView listaPaciente;
    private View rootView;
    private static final String URL = "http://192.168.0.3:8080/simWebService/resources/UsuarioResource?id=";

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
          rootView = inflater.inflate(R.layout.fragment_lista_paciente, container, false);

        //Seccion seleccionada
        fragActivo = fragmentActivo.getInstance();
        SesionUsuario = Sesion.getInstance(1,null,null);
        pacienteActivo = PacienteActivo.getInstance();

        //SI ES ADMINISTRADOR SE ACTIVA UN BOTON QUE PERMITE CREAR LIBROS REPORTS
        if (fragActivo.getData().compareTo("LISTA_REPORT") == 0
               /* && SesionUsuario.getUser().getRol().compareTo("ADMINISTRADOR")==0*/) {

            btnCrearLibReport = (ImageButton) rootView.findViewById(R.id.btnCrearLibReport);
            btnCrearLibReport.setVisibility(View.VISIBLE);

            btnCrearLibReport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment fragment;

                    fragment = new CrearLibroReportFragment();

                    if (fragment != null) {
                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                    } else {
                        Log.e("Error  ", "Crear Libro Report");
                    }
                }
            });


        }
        getListaPacientes();

        ArrayAdapter<Paciente>  adaptador = new pacienteAdapter(rootView.getContext());
        listaPaciente = (ListView) rootView.findViewById(R.id.listView);

        listaPaciente.setAdapter(adaptador);

       listaPaciente.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                Fragment fragment;

                //CREO EL FRAGMENT DEPENDIENDO DE QUE ITEM DEL NAVIGATION DRAWER FUE SELCCIONADO
                if (fragActivo.getData().compareTo("LISTA_FARMACO") == 0) {
                    fragment = new FarmacoFragment();
                } else if (fragActivo.getData().compareTo("LISTA_SIGNOS") == 0) {
                    fragment = new SignosVitalesFragment();
                } else if (fragActivo.getData().compareTo("LISTA_REPORT") == 0) {
                    fragment = new LibroReportFragment();
                } else if( fragActivo.getData().compareTo("LISTA_GLUCOSA") == 0) {
                    fragment = new GlucosaFragment();
                }else /*if( fragActivo.getData().compareTo("LISTA_VISITAS") == 0)*/{
                    fragment = new ProgramarVisitasFragment();
                }


                if (fragment != null) {
                    System.out.println("ESTOY EN LISTA" + ListaPaciente.getLista().get(position).getApellido());

                    Bundle datos = new Bundle();

                   // datos.putSerializable("PACIENTE", ListaPaciente.getLista().get(position));
                   // datos.putSerializable("LISTA", ListaPaciente.getLista());
                    datos.putString("ORIGEN", "ListaPacientesFragment");
                    System.out.println(ListaPaciente.getLista().get(position).getNombre());
                    pacienteActivo.setPaciente(ListaPaciente.getLista().get(position));

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
    private void getListaPacientes(){
        ListaPaciente = ListaPacientes.getInstance();

        Usuario usuario;

        SimWebService service = new SimWebService();
        if(service.validarConexion(rootView.getContext())){
            System.out.println("Red disponible");

            service.configurarMetodo("GET");
            service.configurarUrl(URL+"1");

            if(service.conectar(rootView.getContext(),0)) {
                String datos;
                datos = service.get();
                Gson gson = new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd")
                        .create();
                System.out.println("GET: "+datos);

                //paciente  = gson.fromJson(datos, Paciente.class);
                usuario =  gson.fromJson(datos, Usuario.class);

                if(usuario.getPacientes()!=null){
                    Iterator<Paciente> iter = usuario.getPacientes().iterator();

                    while (iter.hasNext()){
                        ListaPaciente.setLista(iter.next());
                    }
                }else{
                    Paciente sinDatos = new Paciente();
                    sinDatos.setIdPaciente(-1);
                    sinDatos.setNombre("No");
                    sinDatos.setApellido(" hay datos");
                    sinDatos.setPeso(-1.0);
                    sinDatos.setAltura(-1.0);
                    sinDatos.setDni(-1);
                    sinDatos.setEdad(-1);
                    ListaPaciente.setLista(sinDatos);
                }

               //
            }
            else {
                System.out.println("No se recuperaron datos");
                Paciente sinDatos = new Paciente();
                sinDatos.setIdPaciente(-1);
                sinDatos.setNombre("No");
                sinDatos.setApellido(" hay datos");
                sinDatos.setPeso(-1.0);
                sinDatos.setAltura(-1.0);
                sinDatos.setDni(-1);
                sinDatos.setEdad(-1);
                ListaPaciente.setLista(sinDatos);
            }
        } else {
            System.out.println("Red No disponible");
        }
        Paciente paciente = new Paciente(6, "Maximiliano", "Akike", 34809917,/*"105"*/42, 1.7, 90.0/*,"Infarto Agudo del Miocardio"*/);

        ListaPaciente.setLista(paciente);
        ListaPaciente.setLista(paciente);
        ListaPaciente.setLista(paciente);
    }


}
