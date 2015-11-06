package com.example.maxi.sim;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;

public class RegistrarUsuarioActivity extends AppCompatActivity {
    private Spinner spinnerRol;
    private Integer RolActivo ;
    private String[] roles =  {"Selecccionar Rol","Administrador","Medico","Enfermero"};
    //private static final String URL = "http://192.168.0.3:8080/simWebService/resources/";
    private String URL;
    private EditText EditTxtNombre;
    private EditText EditTxtDni;
    private EditText EditTxtFechaNac;
    private EditText EditTxtEmail;
    private EditText EditTxtUsuario;
    private EditText EditTxtPassword;
    private TextInputLayout TiLayoutPassword;
    private TextInputLayout TiLayoutPasswordR;
    private TextInputLayout TiLayoutUsuario;

    public static String  algoritmoEncriptacion = "SHA-256";


    private EditText EditTxtPasswordR;
    private Button btnRegistrarse;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_usuario);

        Url urlServer = Url.getInstance();

        URL = urlServer.getUrl();

        EditTxtNombre = (EditText) findViewById(R.id.EditTxtNombre);
        EditTxtDni = (EditText) findViewById(R.id.EditTxtDni);
        EditTxtFechaNac =  (EditText) findViewById(R.id.EditTxtFechaNac);
        EditTxtEmail = (EditText) findViewById(R.id.EditTxtEmail);
        EditTxtUsuario = (EditText) findViewById(R.id.EditTxtUsuario);
        EditTxtPasswordR =(EditText) findViewById(R.id.EditTxtPasswordR);
        EditTxtPassword =(EditText) findViewById(R.id.EditTxtPassword);

        TiLayoutPassword = (TextInputLayout)findViewById(R.id.TiLayoutPassword);
        TiLayoutPasswordR = (TextInputLayout)findViewById(R.id.TiLayoutPasswordR);
        TiLayoutUsuario = (TextInputLayout) findViewById(R.id.TiLayoutUsuario);

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
                    System.out.println("Rol: "+RolActivo.toString());

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                //optionally do something here
            }
        });

        btnRegistrarse = (Button) findViewById(R.id.btnRegistrarse);
        btnRegistrarse.setOnClickListener(new View.OnClickListener() {
          @Override
              public void onClick(View v) {

              String vNombre = EditTxtNombre.getText().toString();
              String vDni = EditTxtDni.getText().toString();
              String vFechaNac = EditTxtFechaNac.getText().toString();
              String vEmail = EditTxtEmail.getText().toString();
              String vUsuario = EditTxtUsuario.getText().toString();
              String vPassword = EditTxtPassword.getText().toString();
              String vPasswordR = EditTxtPasswordR.getText().toString();

              ContraseñaEnciptada password = new ContraseñaEnciptada();
              ContraseñaEnciptada passwordR = new ContraseñaEnciptada();

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
                      //if(!verificarUsuario(vUsuario)) {
                          TiLayoutUsuario.setErrorEnabled(false);
                          String fechaNacJson = "\"" + "fecha" + "\"" + ":" + "\"" + "Oct 10, 2015 9:24:43 PM" + "\"";
                          String NombreJson = "\"" + "nombre" + "\"" + ":" + "\"" + vNombre + "\"";
                          String DniJson = "\"" + "dni" + "\"" + ":" + vDni;
                          String EmailJson = "\"" + "mail" + "\"" + ":" + "\"" + vEmail + "\"";
                          String UsuarioJson = "\"" + "usuario" + "\"" + ":" + "\"" + vUsuario + "\"";
                          String PasswordJson = "\"" + "password" + "\"" + ":" + "\"" + password.getPasswordEncriptada() + "\"";
                          String rolJson = "\"" + "rol" + "\"" + ":{"+ "\"" +"idRol"+"\"" +":" + RolActivo.toString() + "}";

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
                          datos.append("}");
                          try {
                              postUsuario(getBaseContext(), datos);
                          } catch (IOException e) {
                              e.printStackTrace();
                          }
                     /* }else{
                          TiLayoutUsuario.setErrorEnabled(true);
                          TiLayoutUsuario.setError("Existe un Usuario con ese nombre");
                      }*/
                  }
                  else{

                      TiLayoutPasswordR.setErrorEnabled(true);
                      TiLayoutPasswordR.setError("Las contraseñas no coinciden");
                  }
              }
              else{
                  Toast toastRequerido = Toast.makeText(getBaseContext(),
                          "Todos los campos deben ser completados", Toast.LENGTH_LONG);
                  toastRequerido.show();
              }

          }
          });


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
                service.post(datos.toString());

                DialogoExito dialogo = new DialogoExito();
                dialogo.show(getFragmentManager(), "Informacion");
            }
        }
        else{
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
                return true;
            }
        } else {
            Toast toast = Toast.makeText(this.getApplicationContext(), "Red No Disponible", Toast.LENGTH_LONG);
            toast.show();
        }
        return false;
    }
}
