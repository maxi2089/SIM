package com.example.maxi.sim;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class GlucosaFragment extends Fragment {
    private View rootView;
    private ImageButton   btnGuardar;
    private TextView txtPaciente;
    private EditText TxtGlucosa;
    private PacienteActivo pacienteActivo;
   // private static final String URL = "http://192.168.0.3:8080/simWebService/resources/MedicionResource";
    private String URL;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView  = inflater.inflate(R.layout.fragment_glucosa, container, false);
        fragmentActivo fragActivo =  fragmentActivo.getInstance();
        fragActivo.setData("GLUCOSA");
        Url urlServer = Url.getInstance();
        URL = urlServer.getUrl();

        txtPaciente = (TextView) rootView.findViewById(R.id.txtPaciente);
        TxtGlucosa = (EditText) rootView.findViewById(R.id.TxtGlucosa);
        pacienteActivo = PacienteActivo.getInstance();
        txtPaciente.setText(pacienteActivo.getPaciente().getNombre() + " " + pacienteActivo.getPaciente().getApellido());


        btnGuardar = (ImageButton) rootView.findViewById(R.id.btnGuardar);

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String vGlucosa =  TxtGlucosa.getText().toString();
                if(!vGlucosa.equals("")) {

                    StringBuilder datosJson = new StringBuilder();

                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    String currentDateandTime = sdf.format(new Date());

                    String IdlibroReport = "\"" + "idLibroReport" + "\"" + ":" +pacienteActivo.getPaciente().getIdPaciente();
                    String glucosaJson = "\"" + "glucosa" + "\"" + ":"+vGlucosa;
                    String fechaJson = "\"" + "fecha" + "\"" + ":" + "\"" + currentDateandTime.toString() + "\"";
                    String descJson = "\"" + "descripcion" + "\"" + ":" + "\"" + "nivel glucosa" + "\"";

                    datosJson.append("{");
                    datosJson.append(IdlibroReport);
                    datosJson.append(","+"\""+"mediciones"+"\""+":[{");
                    datosJson.append(fechaJson);
                    datosJson.append(",");
                    datosJson.append(descJson);
                    datosJson.append(",");
                    datosJson.append(glucosaJson);
                    datosJson.append("}]}");

                    System.out.println(datosJson.toString());
                    try {
                        postGlucosa(rootView.getContext(),datosJson);
                        AnalizarGlucosa();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                else {
                    Toast toast = Toast.makeText(rootView.getContext(),
                            "El campo de nivel de glucosa esta vacio", Toast.LENGTH_LONG);

                    toast.show();
                }

            }});



        return  rootView;
    }

    private void postGlucosa(Context farmacoContext, StringBuilder datos) throws IOException {

        SimWebService service = new SimWebService();

        if (service.validarConexion(farmacoContext)) {
            System.out.println("Red disponible");

            service.configurarMetodo("POST");
            service.configurarUrl(URL+"MedicionResource");

            if (service.conectar(farmacoContext,datos.toString().getBytes().length)) {
                System.out.println("Datos "+"\n"+datos);
                service.write(datos.toString());
                System.out.println("-------------");

            }
        }
        else{
            System.out.println("Red No disponible");
            Toast toast = Toast.makeText(farmacoContext,"Red No Disponible",Toast.LENGTH_LONG);
            toast.show();
        }
    }

    private void AnalizarGlucosa(){
        Double glucosa = Double.parseDouble(TxtGlucosa.getText().toString());
        Double valorMinimo = 70.0;
        Double ValorMaximo = 110.0;
        String dosisRecomendada="" ;
        String msj;

          if(glucosa < valorMinimo || glucosa >ValorMaximo){

            if(glucosa >= 150.0 && glucosa < 200.0)
                dosisRecomendada = "2";
            if(glucosa >= 200.0 && glucosa < 250.0)
                dosisRecomendada = "4";
            if(glucosa >= 250.0 && glucosa < 300.0)
                dosisRecomendada = "6";
            if(glucosa >= 300.0)
                dosisRecomendada = "8 a 10";
            if(dosisRecomendada.compareTo("")==0) {
                msj = "Desea enviar un alerta? ";
            }else{
                msj = "Se recomienda administrar " + dosisRecomendada + " Unidades de Insulina"+"\n"+"Desea enviar un alerta?";
            }

            Dialogo dialogo = new Dialogo();
            Bundle bundle = new Bundle();
            bundle.putString("TITULO", "Nivel de Glucosa fuera de rango");
            bundle.putString("MENSAJE",msj);
                    dialogo.setArguments(bundle);
            dialogo.show(getFragmentManager(), "Nivel");
        }else{
            Toast toast = Toast.makeText(rootView.getContext(),"Red No Disponible",Toast.LENGTH_LONG);
            toast.show();
        }
    }



}
