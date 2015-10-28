package com.example.maxi.sim;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;

import java.util.Calendar;
import java.util.Iterator;

public class LoginActivity extends AppCompatActivity {
    private Usuario usuarioLogin;
    private Button btnAceptar;
    private Button btnRegistrar;
    private EditText txUser;
    private EditText txPassword;
    private TextView msgLogin;
    public static String algoritmoEncriptacion = "SHA-256";
    private TextInputLayout txtInputLayoutPass;
    private TextInputLayout txtInputLayoutUser;
    private static final String URL = "http://192.168.0.3:8080/simWebService/resources/";
    private Url urlServer;
    //PRUEBA
    private ListaPacientes ListaPaciente;
    private Usuario usuario;

    public boolean validaUsuario() throws JSONException {
        boolean userOk = false;


        SimWebService service = new SimWebService();
        if (service.validarConexion(this.getApplicationContext())) {

            System.out.println("Red disponible");
            service.configurarMetodo("GET");

            service.configurarUrl(URL + "UsuarioResource?usuario=" + usuarioLogin.getUsuario());


            if (service.conectar(this.getApplicationContext(), 0)) {
                String datos;
                datos = service.get();
                if (datos.isEmpty()) {
                    txtInputLayoutPass.setErrorEnabled(true);
                    txtInputLayoutPass.setError("Error: No se pudo recuperar datos del usuario");
                    userOk = false;
                } else {

                    Gson gson = new Gson();

                    Usuario usuarioBD = gson.fromJson(datos, Usuario.class);

                    System.out.println("User " + usuarioBD.getUsuario());
                    System.out.println("Rol " + usuarioBD.getRol());

                    if (usuarioBD.getUsuario().compareTo(usuarioLogin.getUsuario()) == 0
                            && usuarioBD.getPassword().compareTo(usuarioLogin.getPassword()) == 0) {
                        txtInputLayoutPass.setErrorEnabled(false);
                        System.out.println("Usuario OK");
                        usuarioLogin.setPassword("");
                        usuarioLogin.setDni(usuarioBD.getDni());
                        usuarioLogin.setNombre(usuarioBD.getNombre());
                        usuarioLogin.setRol(usuarioBD.getRol());
                        usuarioLogin.setIdUsuario(usuarioBD.getIdUsuario());
                        userOk = true;
                    } else {
                        txtInputLayoutPass.setErrorEnabled(true);
                        txtInputLayoutPass.setError("Password Incorrecta");
                        userOk = false;
                    }
                }
                System.out.println("..................");
            } else {

                txtInputLayoutPass.setErrorEnabled(true);
                txtInputLayoutPass.setError("Error: Red no disponible");
            }
        } else {
            txtInputLayoutPass.setErrorEnabled(true);
            txtInputLayoutPass.setError("Error: Red no disponible");
            userOk = false;
        }
        return userOk;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final Calendar fechaActual = Calendar.getInstance();
        this.setTitle("Bienvenido a SIM!");

        urlServer = Url.getInstance();
        urlServer.setUrl(URL);

        txUser = (EditText) findViewById(R.id.TxtUser);
        txPassword = (EditText) findViewById(R.id.TxtPassword);
        btnAceptar = (Button) findViewById(R.id.BtnLoginIngresar);
        btnRegistrar = (Button) findViewById(R.id.BtnLoginRegistrarse);

        txtInputLayoutPass = (TextInputLayout) findViewById(R.id.TiLayoutPass);
        txtInputLayoutUser = (TextInputLayout) findViewById(R.id.TiLayoutUsuario);

        txtInputLayoutPass.setErrorEnabled(true);
        txtInputLayoutUser.setErrorEnabled(true);

        //Implementamos el evento click del boton
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ContraseñaEnciptada passEncriptada = new ContraseñaEnciptada();

                passEncriptada.EncriptarContraseña(txPassword.getText().toString(), algoritmoEncriptacion);
                //prueba
                Rol rol = new Rol();
                rol.setIdRol(1);
                rol.setNombreRol("Enfermero");

                getListaPacientes();

                //usuarioLogin =  new  Usuario(txUser.getText().toString(),passEncriptada.getPasswordEncriptada());
                usuarioLogin = new Usuario(usuario.getIdUsuario(),rol, usuario.getDni(), usuario.getNombre(), usuario.getUsuario(), passEncriptada.getPasswordEncriptada(),usuario.getEmail());

                //try {
                //if (validaUsuario()) {
                //Si se valida correctamente el usuario se crea la sesion para dicho usuario
                Sesion SesionUsuario = Sesion.getInstance(1, usuarioLogin, fechaActual.getTime());

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                // Intent intent = new Intent(LoginActivity.this, AddNewPhoto.class);
                //intent.setAction(Intent.ACTION_SEND);
                //intent.setType("image/*");
                //Iniciamos la nueva actividad
                startActivity(intent);
                // }
                // } catch (JSONException e) {
                //     e.printStackTrace();
                // }
            }
        });

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LoginActivity.this, RegistrarUsuarioActivity.class);
                //Iniciamos la nueva actividad
                startActivity(intent);

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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

    private void getListaPacientes() {
        ListaPaciente = ListaPacientes.getInstance();
        ListaPaciente.getLista().clear();


        SimWebService service = new SimWebService();
        if (service.validarConexion(this.getApplicationContext())) {
            System.out.println("Red disponible");

            service.configurarMetodo("GET");
            service.configurarUrl(URL + "UsuarioResource?id=" + 1);

            if (service.conectar(this.getApplicationContext(), 0)) {
                String datos;
                datos = service.get();
                Gson gson = new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd")
                        .create();
                System.out.println("GET: " + datos);

                usuario = gson.fromJson(datos, Usuario.class);

            } else {
                System.out.println("Red No disponible");
            }
        }
    }

}



