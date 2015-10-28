package com.example.maxi.sim;

import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.w3c.dom.Text;

import java.util.Iterator;


public class HomeFragment extends Fragment {


    private View rootView;
    private Sesion sesion;
    private ImageButton btnModificar;
    private TextView txtRol;
    private TextView txtUser;
    private ListaPacientes ListaPaciente;
    private TextView txtPacienteAsignado;
    private String URL;

    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        rootView = inflater.inflate(R.layout.fragment_home, container, false);

        Url urlServer = Url.getInstance();
        URL = urlServer.getUrl();

        sesion = Sesion.getInstance();
        btnModificar = (ImageButton)rootView.findViewById(R.id.btnModificar);
        txtRol = (TextView)rootView.findViewById(R.id.txtRol);
        txtUser = (TextView)rootView.findViewById(R.id.titulo);
        txtPacienteAsignado = (TextView) rootView.findViewById(R.id.txtPacienteAsignados);

        txtRol.setText(sesion.getUser().getRol().getNombreRol());
        txtUser.setText(sesion.getUser().getNombre());

        getListaPacientes();

        txtPacienteAsignado.setText(Integer.toString(ListaPaciente.getLista().size()));

        btnModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment;

                fragment = new ModificarUsuarioFragment();

                if (fragment != null) {

                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                } else {
                    Log.e("Error  ", "Modificar Usuario");
                }
            }});

        return rootView;

    }

    private void getListaPacientes(){
        ListaPaciente = ListaPacientes.getInstance();
        ListaPaciente.getLista().clear();
        Usuario usuario;

        SimWebService service = new SimWebService();
        if(service.validarConexion(rootView.getContext())){
            System.out.println("Red disponible");

            service.configurarMetodo("GET");
            service.configurarUrl(URL+"UsuarioResource?id="+sesion.getUser().getIdUsuario());

            if(service.conectar(rootView.getContext(),0)) {
                String datos;
                datos = service.get();
                Gson gson = new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd")
                        .create();
                System.out.println("GET: "+datos);

                usuario =  gson.fromJson(datos, Usuario.class);

                if(usuario.getPacientes()!=null){
                    Iterator<Paciente> iter = usuario.getPacientes().iterator();

                    while (iter.hasNext()){

                        ListaPaciente.setLista(iter.next());
                        System.out.println("aaa");
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
    }
}
