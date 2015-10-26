package com.example.maxi.sim;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

/**
 * Created by yamila on 24/10/2015.
 */
public class ModificarUsuarioFragment  extends Fragment{
    private View rootView;
    private Sesion sesion;
    private static final String URL = "http://192.168.0.3:8080/simWebService/resources/";
    public static final String  algoritmoEncriptacion = "SHA-256";

    private String[] roles =  {"Seleccionar Rol","Enfermero","Medico"};;
    private String RolActivo ;

    private EditText EditTxtNombre;
    private EditText EditTxtDni;
    private EditText EditTxtFechaNac;
    private EditText EditTxtEmail;
    private EditText EditTxtUsuario;
    private EditText EditTxtPassword;
    private TextInputLayout TiLayoutPassword;
    private TextInputLayout TiLayoutPasswordR;
    private TextInputLayout TiLayoutUsuario;

    private EditText EditTxtPasswordR;
    private ImageButton btnGuardar;
    private Spinner spinnerRol;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        rootView = inflater.inflate(R.layout.fragment_modificar_usuario, container, false);

        fragmentActivo fragActivo =  fragmentActivo.getInstance();
        fragActivo.setData("MODIFICAR_USUARIO");

        sesion = Sesion.getInstance();

        EditTxtNombre = (EditText) rootView.findViewById(R.id.EditTxtNombre);
        EditTxtDni = (EditText)  rootView.findViewById(R.id.EditTxtDni);
        EditTxtFechaNac =  (EditText)  rootView.findViewById(R.id.EditTxtFechaNac);
        EditTxtEmail = (EditText)  rootView.findViewById(R.id.EditTxtEmail);
        EditTxtUsuario = (EditText)  rootView.findViewById(R.id.EditTxtUsuario);
        EditTxtPasswordR =(EditText)  rootView.findViewById(R.id.EditTxtPasswordR);
        EditTxtPassword =(EditText)  rootView.findViewById(R.id.EditTxtPassword);

        TiLayoutPassword = (TextInputLayout) rootView.findViewById(R.id.TiLayoutPassword);
        TiLayoutPasswordR = (TextInputLayout) rootView.findViewById(R.id.TiLayoutPasswordR);
        TiLayoutUsuario = (TextInputLayout) rootView.findViewById(R.id.TiLayoutUsuario);

        inicializarCampos();

        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(rootView.getContext(),android.R.layout.simple_spinner_item,roles);
        spinnerRol = (Spinner)  rootView.findViewById(R.id.spinnerRol);
        spinnerRol.setAdapter(adaptador);
        spinnerRol.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                       long arg3) {
                if (arg2 != 0) {
                    //Posicion del spinner debe coincidir con la posicion de la lista de pacientes..
                    System.out.println("Numero " + arg2);
                    RolActivo = roles[arg2].toString();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                //optionally do something here
            }
        });

        btnGuardar = (ImageButton) rootView.findViewById(R.id.btnGuardar);

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String vNombre = EditTxtNombre.getText().toString();
                String vDni = EditTxtDni.getText().toString();
                String vFechaNac = EditTxtFechaNac.getText().toString();
                String vEmail = EditTxtEmail.getText().toString();
                String vUsuario = EditTxtUsuario.getText().toString();
                String vPassword = EditTxtPassword.getText().toString();
                String vPasswordR = EditTxtPasswordR.getText().toString();

                Boolean datos_ok = true;

                ContraseñaEnciptada password = new ContraseñaEnciptada();
                ContraseñaEnciptada passwordR = new ContraseñaEnciptada();

                password.EncriptarContraseña(vPassword,algoritmoEncriptacion);
                passwordR.EncriptarContraseña(vPasswordR,algoritmoEncriptacion);

                StringBuilder datos = new StringBuilder();

                String idUsuarioJson = "\"" + "idUsuario" + "\"" + ":" + "\"" + sesion.getUser().getIdUsuario() + "\"";
                datos.append("{");
                datos.append(idUsuarioJson);

                System.out.println(RolActivo);
                System.out.println(sesion.getUser().getRol().getNombreRol());
                if(RolActivo != null && RolActivo.compareTo(sesion.getUser().getRol().getNombreRol())!=0){
                    String rolJson = "\"" + "rol" + "\"" + ":" + "\"" + RolActivo + "\"";
                    datos.append(",");
                    datos.append(rolJson);
                }

                if(!vDni.equals("") && vDni.compareTo(Integer.toString(sesion.getUser().getDni()))!=0){
                        String DniJson = "\"" + "dni" + "\"" + ":" + vDni;
                        datos.append(",");
                        datos.append(DniJson);
                 }


                if(!vNombre.equals("") && vNombre.compareTo(sesion.getUser().getNombre())!=0){

                        String NombreJson = "\"" + "nombre" + "\"" + ":" + "\"" + vNombre + "\"";
                        datos.append(",");
                        datos.append(NombreJson);
                }

                if(!vUsuario.equals("") && vUsuario.compareTo(sesion.getUser().getUsuario())!=0){

                    if(!verificarUsuario(vUsuario)) {
                        String UsuarioJson = "\"" + "usuario" + "\"" + ":" + vUsuario;
                        datos.append(",");
                        datos.append(UsuarioJson);
                        TiLayoutUsuario.setErrorEnabled(false);
                    }else{
                        TiLayoutUsuario.setErrorEnabled(true);
                        TiLayoutUsuario.setError("Existe un usuario con ese nombre");
                        datos_ok = false;
                    }
                }

                if(!vPassword.equals("")
                        && !vPassword.equals("")){
                        if( password.getPasswordEncriptada().compareTo(passwordR.getPasswordEncriptada())==0) {

                            String PasswordJson = "\"" + "password" + "\"" + ":" + "\"" + password.getPasswordEncriptada() + "\"";
                            datos.append(",");
                            datos.append(PasswordJson);
                            TiLayoutPasswordR.setErrorEnabled(false);

                        }
                        else {
                            TiLayoutPasswordR.setErrorEnabled(true);
                            TiLayoutPasswordR.setError("Las contraseñas no coinciden");
                            datos_ok = false;
                        }
                }
                datos.append("}");
                //String fechaNacJson = "\"" + "fecha" + "\"" + ":" + "\"" + "Oct 10, 2015 9:24:43 PM" + "\"";
                //String EmailJson = "\"" + "email" + "\"" + ":" + "\"" + vEmail + "\"";
                System.out.println(datos);
                if(datos_ok) {
                   /* try {
                        //updateUsuario(datos);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/
                }else{
                    Toast toast = Toast.makeText(rootView.getContext(),"Existen datos incorrectos. Por favor, corrija los datos y presione guardar nuevamente",Toast.LENGTH_LONG);
                    toast.show();
                }

            }});
        return rootView;
    }

    public  void updateUsuario(StringBuilder datos) throws IOException {
        SimWebService service = new SimWebService();

        if (service.validarConexion(rootView.getContext())) {
            System.out.println("Red disponible");

            service.configurarMetodo("PUT");
            service.configurarUrl(URL+"UsuarioResources?id="+sesion.getUser().getIdUsuario());

            if (service.conectar(rootView.getContext(),datos.toString().getBytes().length)) {
                System.out.println("Datos " + "\n" + datos);
                service.post(datos.toString());
            }
        }
        else{
            Toast toast = Toast.makeText(rootView.getContext(),"Red No Disponible",Toast.LENGTH_LONG);
            toast.show();
        }

    }

    public boolean verificarUsuario(String usuario){
            SimWebService service = new SimWebService();

            if (service.validarConexion(rootView.getContext())) {
                System.out.println("Red disponible");

                service.configurarMetodo("GET");
                service.configurarUrl(URL + "UsuarioResources?nombre=" + usuario);

                if (service.conectar(rootView.getContext(), 1)) {
                    String datos;
                    datos = service.get();
                    return true;
                }
            } else {
                Toast toast = Toast.makeText(rootView.getContext(), "Red No Disponible", Toast.LENGTH_LONG);
                toast.show();
            }
            return false;
    }

    public void inicializarCampos(){
        EditTxtNombre.setText(sesion.getUser().getNombre());
        EditTxtDni.setText(Integer.toString(sesion.getUser().getDni()));
        //EditTxtFechaNac.setText(sesion.getUser().getFechaNac());
        //EditTxtEmail.setText(sesion.getUser().getEmail());
        EditTxtUsuario.setText(sesion.getUser().getUsuario());
    }


}
