package com.example.maxi.sim;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistrarUsuarioActivity extends AppCompatActivity {
    private Spinner spinnerRol;
    private Integer RolActivo ;
    private String[] roles =  {"Selecccionar Rol","Administrador","Medico","Enfermero"};
    //private static final String URL = "http://192.168.0.3:8080/simWebService/resources/";
    private String URL;
    private EditText EditTxtNombre;
    private EditText EditTxtDni;
    private EditText EditTxtEmail;
    private EditText EditTxtUsuario;
    private EditText EditTxtPassword;
    private TextInputLayout TiLayoutPassword;
    private TextInputLayout TiLayoutPasswordR;
    private TextInputLayout TiLayoutUsuario;
    private TextInputLayout TiLayoutCorreo;
    private TextView txtFecha;
    private ImageButton btnCalendario;

    public static String  algoritmoEncriptacion = "SHA-256";

    String vNombre;
    String vDni;
    String vFechaNac;
    String vEmail;
    String vUsuario;
    String vPassword;
    String vPasswordR;
    ContraseñaEnciptada password ;
    ContraseñaEnciptada passwordR;

    private EditText EditTxtPasswordR;
    private ImageButton btnRegistrarse;
    private RelativeLayout progressLayout;
    private RelativeLayout detalle;
    private ProgressBar barraProg;
    //Registro Gooogle API
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "MainActivity";
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private String token;
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_usuario);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Url urlServer = Url.getInstance();

        URL = urlServer.getUrl();

        progressLayout = (RelativeLayout)findViewById(R.id.progressLayout);
        detalle = (RelativeLayout)findViewById(R.id.detalleLayout);

        EditTxtNombre = (EditText) findViewById(R.id.EditTxtNombre);
        EditTxtDni = (EditText) findViewById(R.id.EditTxtDni);
        EditTxtUsuario = (EditText) findViewById(R.id.EditTxtUsuario);
        EditTxtPasswordR =(EditText) findViewById(R.id.EditTxtPasswordR);
        EditTxtPassword =(EditText) findViewById(R.id.EditTxtPassword);
        EditTxtEmail = (EditText) findViewById(R.id.EditTxtEmail);

        txtFecha = (TextView) findViewById(R.id.txtFecha);
        btnCalendario = (ImageButton) findViewById(R.id.btnCalendario);

        TiLayoutPassword = (TextInputLayout)findViewById(R.id.TiLayoutPassword);
        TiLayoutPasswordR = (TextInputLayout)findViewById(R.id.TiLayoutPasswordR);
        TiLayoutUsuario = (TextInputLayout) findViewById(R.id.TiLayoutUsuario);
        TiLayoutCorreo = (TextInputLayout) findViewById(R.id.TiLayoutEmail);

        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(getBaseContext(),android.R.layout.simple_spinner_item,roles);
        spinnerRol = (Spinner) findViewById(R.id.spinnerRol);
        spinnerRol.setAdapter(adaptador);

        spinnerRol.setPopupBackgroundResource(R.color.color_secondary);
        spinnerRol.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                       long arg3) {

                if (arg2 != 0) {
                    //Posicion del spinner debe coincidir con la posicion de la lista de pacientes..
                    RolActivo = arg2;
                    System.out.println("Rol: " + RolActivo.toString());

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

        btnRegistrarse = (ImageButton) findViewById(R.id.btnGuardar);
        btnRegistrarse.setOnClickListener(new View.OnClickListener() {
          @Override
              public void onClick(View v) {
              detalle.setVisibility(View.INVISIBLE);
              progressLayout.setVisibility(View.VISIBLE);

              vNombre = EditTxtNombre.getText().toString();
              vDni = EditTxtDni.getText().toString();
              vFechaNac = txtFecha.getText().toString();
              vEmail = EditTxtEmail.getText().toString();
              vUsuario = EditTxtUsuario.getText().toString();
              vPassword = EditTxtPassword.getText().toString();
              vPasswordR = EditTxtPasswordR.getText().toString();

              password = new ContraseñaEnciptada();
              passwordR = new ContraseñaEnciptada();

              password.EncriptarContraseña(vPassword,algoritmoEncriptacion);
              passwordR.EncriptarContraseña(vPasswordR,algoritmoEncriptacion);

              if(!vNombre.equals("")
                      && !vDni.equals("")
                      && !vFechaNac.equals("")
                      && !vEmail.equals("")
                      && !vUsuario.equals("")
                      && !vPassword.equals("")
                      && !vPasswordR.equals("")
                      && RolActivo!=null){

                  if(password.getPasswordEncriptada().compareTo(passwordR.getPasswordEncriptada())==0) {
                      TiLayoutPasswordR.setErrorEnabled(false);
                      if(verificarUsuario(vUsuario)) {
                          TiLayoutUsuario.setErrorEnabled(false);
                          if(validarCorreo(vEmail)){
                              registrarGoogleApi();
                          }else{
                              progressLayout.setVisibility(View.INVISIBLE);
                              detalle.setVisibility(View.VISIBLE);
                              TiLayoutCorreo.setErrorEnabled(true);
                              TiLayoutCorreo.setError("Formato de Correo Electronico Incorrecto");
                          }
                       }

                  }
                  else{
                      progressLayout.setVisibility(View.INVISIBLE);
                      detalle.setVisibility(View.VISIBLE);
                      TiLayoutPasswordR.setErrorEnabled(true);
                      TiLayoutPasswordR.setError("Las contraseñas no coinciden");

                  }
              }
              else{
                  progressLayout.setVisibility(View.INVISIBLE);
                  detalle.setVisibility(View.VISIBLE);
                  Toast toastRequerido = Toast.makeText(getBaseContext(),
                          "Todos los campos deben ser completados", Toast.LENGTH_LONG);
                  toastRequerido.show();

              }

          }
          });

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                System.out.println("RCV1");

               SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences
                        .getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
                System.out.println("sentToken: "+sentToken );

                if (sentToken) {
                    System.out.println("GCM: " + getString(R.string.gcm_send_message));
                    token = intent.getStringExtra("TOKEN");
                    System.out.println("TOKEEEEN "+token);
                    TiLayoutUsuario.setErrorEnabled(false);
                    TiLayoutCorreo.setErrorEnabled(false);
                    String fechaNacJson = "\"" + "fecha" + "\"" + ":" + "\"" + "Oct 10, 2015 9:24:43 PM" + "\"";
                    String NombreJson = "\"" + "nombre" + "\"" + ":" + "\"" + vNombre + "\"";
                    String DniJson = "\"" + "dni" + "\"" + ":" + vDni;
                    String EmailJson = "\"" + "mail" + "\"" + ":" + "\"" + vEmail + "\"";
                    String UsuarioJson = "\"" + "usuario" + "\"" + ":" + "\"" + vUsuario + "\"";
                    String PasswordJson = "\"" + "password" + "\"" + ":" + "\"" + password.getPasswordEncriptada() + "\"";
                    String rolJson = "\"" + "rol" + "\"" + ":{" + "\"" + "idRol" + "\"" + ":" + RolActivo.toString() + "}";
                    String tokenJson = "\"" + "mensajeRegId" + "\"" + ":" + "\"" + token + "\"";
                    StringBuilder datos = new StringBuilder();

                    datos.append("{");
                    datos.append(rolJson);
                    datos.append(",");
                    datos.append(DniJson);
                    datos.append(",");
                    datos.append(NombreJson);
                    datos.append(",");
                    datos.append(UsuarioJson);
                    datos.append(",");
                    datos.append(PasswordJson);
                    datos.append(",");
                    datos.append(EmailJson);
                    datos.append(",");
                    datos.append(tokenJson);
                    datos.append("}");

                     try {
                    postUsuario(getBaseContext(), datos);
                    System.out.println(datos);
                    } catch (IOException e) {
                      e.printStackTrace();
                    }

                } else {
                    progressLayout.setVisibility(View.INVISIBLE);
                    detalle.setVisibility(View.VISIBLE);
                    Toast toastRequerido = Toast.makeText(getBaseContext(),
                            "Error: No es posible completar el registro. Comuniquese con Administracion.", Toast.LENGTH_LONG);
                    toastRequerido.show();
                }
            }
        };


    }
   @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_registrar_usuario, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void postUsuario(Context Context, StringBuilder datos) throws IOException {

        SimWebService service = new SimWebService();

        if (service.validarConexion(Context)) {
            System.out.println("Red disponible");

            service.configurarMetodo("POST");
            service.configurarUrl(URL+"UsuarioResource");

            if (service.conectar(Context,datos.toString().getBytes().length)) {
                System.out.println("Datos " + "\n" + datos);
                service.write(datos.toString());

                progressLayout.setVisibility(View.INVISIBLE);
                detalle.setVisibility(View.VISIBLE);
                DialogoExito dialogo = new DialogoExito();
                dialogo.show(getFragmentManager(), "Informacion");
            }
        }
        else{
            progressLayout.setVisibility(View.INVISIBLE);
            detalle.setVisibility(View.VISIBLE);
            Toast toast = Toast.makeText(Context,"Red No Disponible",Toast.LENGTH_LONG);
            toast.show();
        }
    }

    public boolean verificarUsuario(String usuario){
        SimWebService service = new SimWebService();

        if (service.validarConexion(this.getApplicationContext())) {
            System.out.println("Red disponible");

            service.configurarMetodo("GET");
            service.configurarUrl(URL + "UsuarioResource?usuario=" + usuario);

            if (service.conectar(this.getApplicationContext(), 1)) {
                String datos;
                datos = service.get();
                Gson gson = new Gson();
                Usuario usuarioVerf = gson.fromJson(datos, Usuario.class);
                if (usuarioVerf.getError().compareTo("OK")!=0) {
                    return true;
                }else{
                    progressLayout.setVisibility(View.INVISIBLE);
                    detalle.setVisibility(View.VISIBLE);
                    TiLayoutUsuario.setErrorEnabled(true);
                    TiLayoutUsuario.setError("Existe un Usuario con ese nombre");
                    return false;
                }
            }
        } else {
            progressLayout.setVisibility(View.INVISIBLE);
            detalle.setVisibility(View.VISIBLE);
            Toast toast = Toast.makeText(this.getApplicationContext(), "Red No Disponible", Toast.LENGTH_LONG);
            toast.show();
        }
        return false;
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
    private void registrarGoogleApi(){


        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            System.out.println("GET TOKEN");
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }

    }
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                System.out.println(TAG + ": This device is not supported." );
                finish();
            }
            return false;
        }
        return true;
    }
}
