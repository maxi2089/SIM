package com.example.maxi.sim;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Base64;

import org.apache.http.client.HttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
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
        private HttpsURLConnection connection;
        private String requestMethod;

        public void configurarMetodo(String metodo){
            this.requestMethod = metodo;
        }

        public void configurarUrl(String urlString){
            try {
                this.url = new URL(urlString);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        public boolean conectar(Context context)  {

            try {
                if (validarConexion(context)) {
                    connection = (HttpsURLConnection) url.openConnection();
                    connection.setRequestMethod(requestMethod);
                    connection.setDoInput(true);
                    connection.setConnectTimeout(5000);
                    connection.setReadTimeout(10000);
                    // connection.setRequestProperty("Content-Type", "application/json");
                    connection.setRequestProperty("User-Agent", "cliente Android 1.0");
                    //autenticación BASIC
                    //connection.setRequestProperty("Authorization", "Basic " + Base64.encodeToString((usuario + ":" + password).getBytes(), Base64.NO_WRAP));
                    connection.connect();
                    return true;
                }
                else
                    return false;
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
                // ¿Tenemos conexión? ponemos a true
                if (redes[i].getState() == NetworkInfo.State.CONNECTED) {
                    conectado = true;
                }
            }
            return conectado;
        }



        public void get(){
            try {

                InputStream in = new BufferedInputStream(connection.getInputStream());

                BufferedReader buffered = new BufferedReader(new InputStreamReader(in));
                StringBuilder fullLines = new StringBuilder();

                String line;

                //
                while ((line = buffered.readLine()) != null) {
                    fullLines.append(line);
                }

                String result = fullLines.toString();

                JSONArray objetos = new JSONArray(result);

                for(int i=0;i<objetos.length();i++){

                    JSONObject objeto = new JSONObject(objetos.getJSONObject(i).toString());

                    /*

                        cada objecto sera compuesto por esto
                        {
                            country: "pais",
                            capital:"capital"
                        }

                    */

                }
            }catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
}
