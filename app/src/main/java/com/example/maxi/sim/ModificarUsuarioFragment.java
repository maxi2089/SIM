package com.example.maxi.sim;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yamila on 24/10/2015.
 */
public class ModificarUsuarioFragment  extends Fragment{
    private View rootView;
    private Sesion sesion;
    //private static final String URL = "http:configurarUrl//192.168.0.3:8080/simWebService/resources/";
    private Url URL;
    public static final String  algoritmoEncriptacion = "SHA-256";

    private String[] roles =  {"Seleccionar Rol","Enfermero","Medico"};;
    private int RolActivo ;

    private EditText EditTxtNombre;
    private EditText EditTxtDni;
    private EditText EditTxtEmail;
    private EditText EditTxtUsuario;
    private EditText EditTxtPassword;
    private TextInputLayout TiLayoutPassword;
    private TextInputLayout TiLayoutPasswordR;
    private TextInputLayout TiLayoutUsuario;
    private TextInputLayout TiLayoutCorreo;

    private EditText EditTxtPasswordR;
    private ImageButton btnGuardar;
    private Spinner spinnerRol;
    private TextView txtTituloUsuario;
    private ImageButton btnCalendario;
    private TextView txtFecha;

    private String vNombre;
    private String vDni;
    private String vFechaNac;
    private String vEmail;
    private String vUsuario;
    private String vPassword ;
    private String vPasswordR;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        rootView = inflater.inflate(R.layout.fragment_modificar_usuario, container, false);

        fragmentActivo fragActivo =  fragmentActivo.getInstance();
        fragActivo.setData("MODIFICAR_USUARIO");

        sesion = Sesion.getInstance();

        URL = Url.getInstance();

        EditTxtNombre = (EditText) rootView.findViewById(R.id.EditTxtNombre);
        EditTxtDni = (EditText)  rootView.findViewById(R.id.EditTxtDni);
        EditTxtEmail = (EditText)  rootView.findViewById(R.id.EditTxtEmail);
        EditTxtUsuario = (EditText)  rootView.findViewById(R.id.EditTxtUsuario);
        EditTxtPasswordR =(EditText)  rootView.findViewById(R.id.EditTxtPasswordR);
        EditTxtPassword =(EditText)  rootView.findViewById(R.id.EditTxtPassword);

        TiLayoutPassword = (TextInputLayout) rootView.findViewById(R.id.TiLayoutPassword);
        TiLayoutPasswordR = (TextInputLayout) rootView.findViewById(R.id.TiLayoutPasswordR);
        TiLayoutUsuario = (TextInputLayout) rootView.findViewById(R.id.TiLayoutUsuario);
        TiLayoutCorreo = (TextInputLayout) rootView.findViewById(R.id.TiLayoutEmail);

        txtTituloUsuario = (TextView)rootView.findViewById(R.id.txtPaciente);
        txtTituloUsuario.setText("Usuario : " + String.format("%04d", sesion.getUser().getIdUsuario()));
        btnCalendario = (ImageButton) rootView.findViewById(R.id.btnCalendario);
        txtFecha = (TextView) rootView.findViewById(R.id.txtFecha);

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
                    RolActivo = arg2;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                //optionally do something here
            }
        });

        btnCalendario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                DialogFragment newFragment = new DateFragment();
                newFragment.show(getFragmentManager(), "DatePicker");

            }
        });

        btnGuardar = (ImageButton) rootView.findViewById(R.id.btnGuardar);

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                vNombre = EditTxtNombre.getText().toString();
                vDni = EditTxtDni.getText().toString();
                vEmail = EditTxtEmail.getText().toString();
                vUsuario = EditTxtUsuario.getText().toString();
                vPassword = EditTxtPassword.getText().toString();
                vPasswordR = EditTxtPasswordR.getText().toString();
                vFechaNac = txtFecha.getText().toString();

                Boolean correoSinCambio = false;
                Boolean huboCambio = false;

                Boolean datos_ok = true;

                ContraseñaEnciptada password = new ContraseñaEnciptada();
                ContraseñaEnciptada passwordR = new ContraseñaEnciptada();

                password.EncriptarContraseña(vPassword,algoritmoEncriptacion);
                passwordR.EncriptarContraseña(vPasswordR,algoritmoEncriptacion);

                StringBuilder datos = new StringBuilder();

                String idUsuarioJson = "\"" + "idUsuario" + "\"" + ":" + sesion.getUser().getIdUsuario();
                datos.append("{");
                datos.append(idUsuarioJson);

                System.out.println(RolActivo);
                System.out.println(sesion.getUser().getRol().getNombreRol());

                if((RolActivo == 1 || RolActivo == 2 ) && RolActivo!=sesion.getUser().getRol().getIdRol()){
                    String rolJson = "\"" + "rol" + "\"" + ":{"+"\""+"idRol" +"\""+":"+ Integer.toString(RolActivo+1) + "}";
                    datos.append(",");
                    datos.append(rolJson);
                    huboCambio = true;
                }else{
                    String rolJson = "\"" + "rol" + "\"" + ":{"+"\""+"idRol" +"\""+":"+ Integer.toString(sesion.getUser().getRol().getIdRol()) + "}";
                    datos.append(",");
                    datos.append(rolJson);
                }

                if(!vDni.equals("") && vDni.compareTo(Integer.toString(sesion.getUser().getDni()))!=0){
                        String DniJson = "\"" + "dni" + "\"" + ":" + vDni;
                        datos.append(",");
                        datos.append(DniJson);
                    huboCambio = true;
                 }else{
                    String DniJson = "\"" + "dni" + "\"" + ":" + Integer.toString(sesion.getUser().getDni());
                    datos.append(",");
                    datos.append(DniJson);
                }

               /* if(!vFechaNac.equals("") && vFechaNac.compareTo(sesion.getUser().getFechNac)){
                    String FechaJson = "\"" + "fechaNac" + "\"" + ":" +"\""+ vFechaNac+"\"";
                    datos.append(",");
                    datos.append(FechaJson);
                    huboCambio = true;
                }else{
                    String FechaJson = "\"" + "fechaNac" + "\"" + ":" + "\""+sesion.getUser().getFechaNac()+"\"";
                    datos.append(",");
                    datos.append(FechaJson);
                }*/
                if(!vNombre.equals("") && vNombre.compareTo(sesion.getUser().getNombre())!=0){

                    String NombreJson = "\"" + "nombre" + "\"" + ":" + "\"" + vNombre + "\"";
                    datos.append(",");
                    datos.append(NombreJson);
                    huboCambio = true;
                }else{
                    String NombreJson = "\"" + "nombre" + "\"" + ":" + "\"" + sesion.getUser().getNombre() + "\"";
                    datos.append(",");
                    datos.append(NombreJson);
                }

                if(!vUsuario.equals("") && vUsuario.compareTo(sesion.getUser().getUsuario())!=0){
                    huboCambio = true;
                    if(verificarUsuario(vUsuario)) {
                        String UsuarioJson = "\"" + "usuario" + "\"" + ":" +"\""+ vUsuario+"\"";
                        datos.append(",");
                        datos.append(UsuarioJson);
                        TiLayoutUsuario.setErrorEnabled(false);
                    }else{

                        String UsuarioJson = "\"" + "usuario" + "\"" + ":" + "\""+sesion.getUser().getUsuario()+"\"";
                        datos.append(",");
                        datos.append(UsuarioJson);
                        datos_ok = false;
                    }
                }else{
                    String UsuarioJson = "\"" + "usuario" + "\"" + ":" + "\""+sesion.getUser().getUsuario()+"\"";
                    datos.append(",");
                    datos.append(UsuarioJson);
                }

                if(!vPassword.equals("")
                        && !vPassword.equals("")){
                        if( password.getPasswordEncriptada().compareTo(passwordR.getPasswordEncriptada())==0) {

                            String PasswordJson = "\"" + "password" + "\"" + ":" + "\"" + password.getPasswordEncriptada() + "\"";
                            datos.append(",");
                            datos.append(PasswordJson);
                            TiLayoutPasswordR.setErrorEnabled(false);
                            huboCambio = true;

                        }
                        else {
                            String PasswordJson = "\"" + "password" + "\"" + ":" + "\"" +sesion.getUser().getPassword() + "\"";
                            datos.append(",");
                            datos.append(PasswordJson);
                            TiLayoutPasswordR.setErrorEnabled(true);
                            TiLayoutPasswordR.setError("Las contraseñas no coinciden");
                            datos_ok = false;
                        }
                }else{
                    String PasswordJson = "\"" + "password" + "\"" + ":" + "\"" +sesion.getUser().getPassword() + "\"";
                    datos.append(",");
                    datos.append(PasswordJson);
                }

                if(!vEmail.equals("")) {
                    if(validarCorreo(vEmail)) {
                        if (vEmail.compareTo(sesion.getUser().getEmail()) != 0){


                            String EmailJson = "\"" + "mail" + "\"" + ":" + "\"" + vEmail + "\"";
                            datos.append(",");
                            datos.append(EmailJson);
                            correoSinCambio= false;
                            huboCambio = true;
                        }else{
                            correoSinCambio = true;
                        }
                    }else{
                        correoSinCambio = true;
                        TiLayoutCorreo.setErrorEnabled(true);
                        TiLayoutCorreo.setError("Formato de Correo Electronico Incorrecto");
                    }
                }else{
                    correoSinCambio = true;
                }

                if(correoSinCambio){
                    String EmailJson = "\"" + "mail" + "\"" + ":" + "\"" + sesion.getUser().getEmail() + "\"";
                    datos.append(",");
                    datos.append(EmailJson);
                }

                datos.append("}");
                System.out.println(datos);

                if(huboCambio)
                {
                    if(datos_ok){
                        try {
                            updateUsuario(datos);
                            SystemClock.sleep(2000);
                            actualizarSesion();
                            inicializarCampos();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else{
                        Toast toast = Toast.makeText(rootView.getContext(),"Existen datos incorrectos. Por favor, corrija los datos y presione guardar nuevamente",Toast.LENGTH_LONG);
                        toast.show();
                    }
                }else{
                    Toast toast = Toast.makeText(rootView.getContext(),"No se registraron cambios en el usuario",Toast.LENGTH_LONG);
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
            service.configurarUrl(URL.getUrl()+"UsuarioResource");

            if (service.conectar(rootView.getContext(),datos.toString().getBytes().length)) {
                System.out.println("Datos " + "\n" + datos);
                service.write(datos.toString());
               // DialogoExito dialogo = new DialogoExito();
                //dialogo.show(getFragmentManager(),"Informacion");
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
            service.configurarUrl(URL.getUrl() + "UsuarioResource?usuario=" + usuario);

            if (service.conectar(rootView.getContext(), 1)) {
                String datos;
                datos = service.get();
                Gson gson = new Gson();
                Usuario usuarioVerf = gson.fromJson(datos, Usuario.class);

                if (usuarioVerf.getError().compareTo("OK")!=0) {
                    return true;
                }else{
                    TiLayoutUsuario.setErrorEnabled(true);
                    TiLayoutUsuario.setError("Existe un Usuario con ese nombre");
                    return false;
                }
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
        System.out.println("Correo " + sesion.getUser().getEmail());
        EditTxtEmail.setText(sesion.getUser().getEmail());
        EditTxtUsuario.setText(sesion.getUser().getUsuario());
    }

    private void actualizarSesion() {
        SimWebService service = new SimWebService();
        if (service.validarConexion(rootView.getContext())) {
            System.out.println("Red disponible");

            service.configurarMetodo("GET");
            service.configurarUrl(URL.getUrl() + "UsuarioResource?usuario=" + vUsuario);

            if (service.conectar(rootView.getContext(), 0)) {
                String datos;
                datos = service.get();

                Gson gson = new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd")
                        .create();
                System.out.println("GET: " + datos);

                sesion.setUser(gson.fromJson(datos, Usuario.class));

            } else {
                System.out.println("Red No disponible");
            }
        }
    }
    private boolean validarCorreo(String correo){
        final String PATTERN_EMAIL = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(PATTERN_EMAIL);
        Matcher matcher = pattern.matcher(correo);
        return matcher.matches();
    }

    public class DateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int yy = calendar.get(Calendar.YEAR);
            int mm = calendar.get(Calendar.MONTH);
            int dd = calendar.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this, yy, mm, dd);
        }
        public void onDateSet(DatePicker view, int yy, int mm, int dd) {
            populateSetDate(yy, mm+1, dd);
        }
        public void populateSetDate(int year, int month, int day) {
            txtFecha.setText(day+"/"+month+"/"+year);
        }

    }

}
