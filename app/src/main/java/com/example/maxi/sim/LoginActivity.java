package com.example.maxi.sim;

import android.app.Service;
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

import com.example.maxi.sim.R;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;

public class LoginActivity extends AppCompatActivity {
    private UserActivity user;
    private Button btnAceptar;
    private Button btnRegistrar;
    private EditText txUser;
    private EditText txPassword;
    private TextView msgLogin;
    public static String  algoritmoEncriptacion = "SHA-256";
    private TextInputLayout txtInputLayoutPass;
    private TextInputLayout txtInputLayoutUser;


    public boolean validaUsuario(){
        boolean userOk = false;

        String prueba = getStringMessageDigest("hola", algoritmoEncriptacion);
        ServiceActiviy service = new ServiceActiviy();

        if(service.validarConexion(this.getApplicationContext())){
           System.out.println("Red disponible");
        }
        else{
            txtInputLayoutPass.setErrorEnabled(true);
            txtInputLayoutPass.setError("Error: Red no disponible");
        }

       /* ServiceActiviy service = new ServiceActiviy();

        service.configurarMetodo("GET");

        service.configurarUrl("https://");

       if(service.conectar(this.getApplicationContext())){
           if(this.user.getPassword().isEmpty() || this.user.getPassword() != prueba) {
               txtInputLayoutPass.setErrorEnabled(true);
               txtInputLayoutPass.setError("Error: Password Incorrecto");
               userOk = false;
           }
           else {
               //txtInputLayoutPass.setError(null);
               userOk = true;


           }
       }else{

           txtInputLayoutPass.setErrorEnabled(true);
           txtInputLayoutPass.setError("Error: Red no disponible");
       }

        //Conectar a la BD
        //Consulta BD que trae el dato usuario y password
        //Obtener datos de session

        /*if(this.user.getPassword().isEmpty() || this.user.getPassword() != prueba) {
            txtInputLayoutPass.setErrorEnabled(true);
            txtInputLayoutPass.setError("Error: Password Incorrecto");
            userOk = false;
        }
        else {
            txtInputLayoutPass.setError(null);
            userOk = true;
        }
*/
        return userOk;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final Calendar fechaActual = Calendar.getInstance();


        txUser = (EditText)findViewById(R.id.TxtUser);
        txPassword = (EditText)findViewById(R.id.TxtPassword);
        btnAceptar = (Button)findViewById(R.id.BtnLoginIngresar);
        btnRegistrar = (Button)findViewById(R.id.BtnLoginRegistrarse);

        txtInputLayoutPass = (TextInputLayout)findViewById(R.id.TiLayoutPass);
        txtInputLayoutUser = (TextInputLayout)findViewById(R.id.TiLayoutUser);

        txtInputLayoutPass.setErrorEnabled(true);
        txtInputLayoutUser.setErrorEnabled(true);

        //Implementamos el evento click del boton
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String passEncriptada = getStringMessageDigest(txPassword.getText().toString(), algoritmoEncriptacion);
                user = new UserActivity(txUser.getText().toString(),passEncriptada);

             //  if (validaUsuario()) {
                    //Creamos el IntentZ
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    //Creamos la informacion a pasar entre actividades
                    Bundle b = new Bundle();
                    b.putString("USER", txUser.getText().toString());
                    b.putString("FECHA", fechaActual.getTime().toString());
                    //Aniadimos la informacion al intent
                    intent.putExtras(b);
                    //Iniciamos la nueva actividad
                    startActivity(intent);
           // }
            }
        });
    }

    /***
     * Convierte un arreglo de bytes a String usando valores hexadecimales
     * @param digest arreglo de bytes a convertir
     * @return String creado a partir de <code>digest</code>
     */
    private static String toHexadecimal(byte[] digest){
        String hash = "";
        for(byte aux : digest) {
            int b = aux & 0xff;
            if (Integer.toHexString(b).length() == 1) hash += "0";
            hash += Integer.toHexString(b);
        }
        return hash;
    }

            /***
             * Encripta un mensaje de texto mediante algoritmo de resumen de mensaje.
             * @param message texto a encriptar
             * @param algorithm algoritmo de encriptacion, puede ser: MD2, MD5, SHA-1, SHA-256, SHA-384, SHA-512
             * @return mensaje encriptado
             */
    public static String getStringMessageDigest(String message, String algorithm){
        byte[] digest = null;
        byte[] buffer = message.getBytes();
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            messageDigest.reset();
            messageDigest.update(buffer);
            digest = messageDigest.digest();
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("Error creando Digest");
        }
        return toHexadecimal(digest);
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
}


// Probandooooo!