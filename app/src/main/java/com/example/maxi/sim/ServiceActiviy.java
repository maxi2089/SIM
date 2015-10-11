package com.example.maxi.sim;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.StrictMode;
import android.util.Base64;

import org.apache.http.client.HttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;

import javax.net.ssl.HttpsURLConnection;


public class ServiceActiviy {
        private URL url;
        private HttpURLConnection connection;
        private String requestMethod;

        public void configurarMetodo(String metodo){
            this.requestMethod = metodo;
        }

        public void configurarUrl(String urlString){
            try {
                System.out.println("URL "+ urlString);
                this.url = new URL(urlString);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        public boolean conectar(Context context,int longitud)  {
            try {
                if (validarConexion(context)) {
                    int SDK_INT = android.os.Build.VERSION.SDK_INT;
                    if (SDK_INT > 8)
                    {
                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                                .permitAll().build();
                        StrictMode.setThreadPolicy(policy);
                        //your codes here



                    System.out.println("Valida Conexion");
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod(requestMethod);

                    if(requestMethod.compareTo("POST")==0){
                        System.out.println("POST");
                        connection.setDoOutput(true);
                        //connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                        connection.setFixedLengthStreamingMode(longitud);
                    }
                    else{
                        System.out.println("GET");

                        connection.setDoInput(true);
                    }
                    connection.setConnectTimeout(10000);
                    connection.setReadTimeout(15000);
                    // connection.setRequestProperty("Content-Type", "application/json");
                    //connection.setRequestProperty("User-Agent", "cliente Android 1.0");
                    //autenticacion BASIC
                    //connection.setRequestProperty("Authorization", "Basic " + Base64.encodeToString((usuario + ":" + password).getBytes(), Base64.NO_WRAP));
                    System.out.println("Antes de Conectar");

                    connection.connect();
                    System.out.println("Despues de Conectar");

                    return true;
                    }
                    else
                        return false;
                }
                else {
                    System.out.println("No hay conexion");

                    return false;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;

            }
        }

        public boolean validarConexion(Context context) {
            boolean conectado = false;
            ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo[] redes = connMgr.getAllNetworkInfo();

            for (int i = 0; i < 2; i++) {
                // Si tenemos conexion ponemos a true
                if (redes[i].getState() == NetworkInfo.State.CONNECTED) {
                    conectado = true;
                }
            }
            return conectado;
        }



        public String get(){
            int cant=0;
            try {

                InputStream in = new BufferedInputStream(connection.getInputStream());

                BufferedReader buffered = new BufferedReader(new InputStreamReader(in));
                StringBuilder fullLines = new StringBuilder();

                String line;
                //
                while ((line = buffered.readLine()) != null) {
                    cant++;
                    System.out.println("Linea "+cant+line);

                    fullLines.append(line);
                }

                String result = fullLines.toString();

                System.out.println("Resultado" + result);

                //JSONArray objetos = new JSONArray(result);

                //JSONObject objeto = new JSONObject(result);
                //System.out.println("Linea "+objeto.getString("nombre"));
                //System.out.println("Linea "+objeto.);

/*
                for(int i=0;i<objetos.length();i++){

                    JSONObject objeto = new JSONObject(objetos.getJSONObject(i).toString());
                    System.out.println("Linea " + i + " " + objeto.toString());

                }*/

                return result;
            }catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

    public void post(String dato) throws IOException {

        //Send request
        DataOutputStream out = new DataOutputStream (connection.getOutputStream ());
        out.writeBytes (dato);
        out.flush ();
        out.close ();
        connection.disconnect();

    }
}
