package com.example.maxi.sim;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class SignosVitalesFragment extends Fragment {
    private Paciente pacienteActivo;
    private TextView txtPaciente;
    private EditText editSaturometria;
    private EditText editFrecRespiratoria;
    private EditText editTemperatura;
    private EditText editTensionArterial;
    private CheckBox cbSaturometria;
    private CheckBox cbFrecRespiratoria;
    private CheckBox cbTemperatura;
    private CheckBox cbTensionArterial;

    private TextInputLayout lySaturometria;
    private TextInputLayout lyFrecRespiratoria;
    private TextInputLayout lyTemperatura;
    private TextInputLayout lyTensionArterial;

    private ImageButton btnGuardar;
    private ImageButton btnAnalizar;

    private String vSaturometria;
    private String vTensionArterial;
    private String vTemperatura;
    private String vFrecRespiratoria;

    private  View rootView;

    private  ValoresMedicion FrecuenciaRepiratoria;
    private ValoresMedicion TensionArterial;
    private ValoresMedicion Temperatura;

    private ImageButton btnAlertaSaturometria;
    private ImageButton btnAlertaFrecRespiratoria;
    private ImageButton btnAlertaTemperatura;
    private ImageButton btnAlertaTensionArterial;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        rootView = inflater.inflate(R.layout.fragment_signos_vitales, container, false);

        fragmentActivo fragActivo =  fragmentActivo.getInstance();
        fragActivo.setData("SIGNOS");

        pacienteActivo = (Paciente) getArguments().getSerializable("PACIENTE");

        txtPaciente = (TextView)rootView.findViewById(R.id.txtPaciente);

        txtPaciente.setText(pacienteActivo.getNombre() + " " + pacienteActivo.getApellido());


        //
        lySaturometria = (TextInputLayout)rootView.findViewById(R.id.TiLayoutSaturometria);
        lySaturometria.bringToFront();
        lyFrecRespiratoria = (TextInputLayout)rootView.findViewById(R.id.TiLayoutFrecRespiratoria);
        lyTemperatura = (TextInputLayout)rootView.findViewById(R.id.TiLayoutTemperatura);
        lyTensionArterial = (TextInputLayout)rootView.findViewById(R.id.TiLayoutTensionArterial);

        //Signos vitales
        editSaturometria = (EditText)rootView.findViewById(R.id.editSaturometria);
        editFrecRespiratoria = (EditText)rootView.findViewById(R.id.editFrecRespiratoria);
        editTemperatura = (EditText)rootView.findViewById(R.id.editTemperatura);
        editTensionArterial = (EditText)rootView.findViewById(R.id.editTensionArteria);

        //CheckBox x SignosVItal
        cbSaturometria = (CheckBox)rootView.findViewById(R.id.cbSaturometria);
        cbFrecRespiratoria = (CheckBox)rootView.findViewById(R.id.cbFrecRespiratoria);
        cbTemperatura = (CheckBox)rootView.findViewById(R.id.cbTemperatura);
        cbTensionArterial = (CheckBox)rootView.findViewById(R.id.cbTensionArterial);

        btnAlertaSaturometria = (ImageButton) rootView.findViewById(R.id.btnAlertaSaturometria);
        btnAlertaFrecRespiratoria =  (ImageButton) rootView.findViewById(R.id.btnAlertaFrecRes);
        btnAlertaTemperatura =  (ImageButton) rootView.findViewById(R.id.btnAlertaTemp);
        btnAlertaTensionArterial =  (ImageButton) rootView.findViewById(R.id.btnAlertaTensionArt);



        //Guardar
        btnGuardar = (ImageButton) rootView.findViewById(R.id.btnGuardar);

        cbSaturometria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isChecked = ((CheckBox)view).isChecked();

                if (isChecked) {
                    editSaturometria.setEnabled(true);
                 }
                else {
                    editSaturometria.setEnabled(false);
                }
            }
        });
        cbFrecRespiratoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isChecked = ((CheckBox)view).isChecked();

                if (isChecked) {
                    editFrecRespiratoria.setEnabled(true);
                }
                else {
                    editFrecRespiratoria.setEnabled(false);
                }
            }
        });
        cbTemperatura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isChecked = ((CheckBox)view).isChecked();

                if (isChecked) {
                    editTemperatura.setEnabled(true);
                }
                else {
                    editTemperatura.setEnabled(false);
                }
            }
        });
        cbTensionArterial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isChecked = ((CheckBox)view).isChecked();

                if (isChecked) {
                    editTensionArterial.setEnabled(true);
                }
                else {
                    editTensionArterial.setEnabled(false);
                }
            }
        });


        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Conectarse con el web service para que envie el msj al enfermero a cargo
                //Se postea en el libro report
                 vSaturometria = editSaturometria.getText().toString();
                 vTensionArterial = editTensionArterial.getText().toString();
                 vTemperatura = editTemperatura.getText().toString();
                 vFrecRespiratoria = editFrecRespiratoria.getText().toString();

                if ((editSaturometria.isEnabled()&& !vSaturometria.equals(""))
                        || (editTensionArterial.isEnabled()&& !vTensionArterial.equals("") )
                        || (editTemperatura.isEnabled() && !vTemperatura.equals(""))
                        || (editFrecRespiratoria.isEnabled()&& !vFrecRespiratoria.equals(""))) {



                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                    String currentDateandTime = sdf.format(new Date());

                    //Variable para armar el dato JSON
                    //String datosJson = new String();
                    String comillasJson = "\"";
                    String descJson = comillasJson + "descripcion" + comillasJson + ":";
                    String fechaJson = comillasJson + "fecha" + comillasJson + ":" + comillasJson + "Oct 10, 2015 9:24:43 PM"/*currentDateandTime.toString()*/ + comillasJson;

                    //datos ="{"+"\""+"idPaciente"+"\":"+"5"+","+"\""+"dni"+"\":"+"34809913"+","+"\""+"nombre"+"\":"+"\""+"Maxi"+"\""+","+"\""+"apellido"+"\":"+"\""+"Akike"+"\""+","+"\""+"edad"+"\":"+"26"+","+"\""+"altura"+"\":"+"1.75"+"}";
                    StringBuilder datosJson = new StringBuilder();

                    datosJson.append("{");//"
                    datosJson.append(comillasJson);//"
                    datosJson.append("idLibroReport");
                    datosJson.append(comillasJson);//"
                    datosJson.append(":");
                    datosJson.append("5");
                    datosJson.append(",");

                    //Auxiliares
                    boolean primero = false;

                    if (editSaturometria.isEnabled() && !vSaturometria.equals("")) {

                       // datosJson.append("{");
                        datosJson.append(fechaJson);

                        datosJson.append(",");

                        datosJson.append(descJson);
                        datosJson.append(comillasJson);//"
                        datosJson.append("Oxigeno");
                        datosJson.append(comillasJson); //"

                        datosJson.append(",");

                        datosJson.append(comillasJson); //"
                        datosJson.append("oxigenoEnSangre");
                        datosJson.append(comillasJson);// "
                        datosJson.append(":");
                        datosJson.append(vSaturometria);
                        //datosJson.append("}");

                        primero = true;

                    }

                    if (editTensionArterial.isEnabled() && !vTensionArterial.equals("")) {

                        if (primero) {
                            datosJson.append(",");
                        } else {
                            primero = true;
                        }
                       // datosJson.append("{");
                        datosJson.append(fechaJson);

                        datosJson.append(",");

                        datosJson.append(descJson);
                        datosJson.append(comillasJson);//"
                        datosJson.append("Tension Arterial");
                        datosJson.append(comillasJson); //"

                        datosJson.append(",");

                        datosJson.append(comillasJson); //"
                        datosJson.append("TensionArterial");
                        datosJson.append(comillasJson);// "
                        datosJson.append(":");
                        datosJson.append(vSaturometria);


                    }
                    if (editTemperatura.isEnabled() && !vTemperatura.equals("")) {

                        if (primero) {
                            datosJson.append(",");
                        } else {
                            primero = true;
                        }
                        //datosJson.append("{");
                        datosJson.append(fechaJson);

                        datosJson.append(",");

                        datosJson.append(descJson);
                        datosJson.append(comillasJson);//"
                        datosJson.append("Temperatura");
                        datosJson.append(comillasJson); //"

                        datosJson.append(",");

                        datosJson.append(comillasJson); //"
                        datosJson.append("temperatura");
                        datosJson.append(comillasJson);// "
                        datosJson.append(":");
                        datosJson.append(vTemperatura);

                    }
                    if (editFrecRespiratoria.isEnabled() && !vFrecRespiratoria.equals("")) {

                        if (primero) {
                            datosJson.append(",");
                        }
                        //datosJson.append("{");
                        datosJson.append(fechaJson);

                        datosJson.append(",");

                        datosJson.append(descJson);
                        datosJson.append(comillasJson);//"
                        datosJson.append("Frecuencia Respiratoria");
                        datosJson.append(comillasJson); //"

                        datosJson.append(",");

                        datosJson.append(comillasJson); //"
                        datosJson.append("FrecuenciaRespiratoria");
                        datosJson.append(comillasJson);// "
                        datosJson.append(":");
                        datosJson.append(vFrecRespiratoria);
                    }
                    datosJson.append("}");

                    try {
                        System.out.println(datosJson.toString());
                        putSignosVitales(rootView.getContext(), datosJson);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

        });

        btnAnalizar = (ImageButton) rootView.findViewById(R.id.btnAnalizar);

        btnAnalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    analizarMediciones();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });


        return rootView;

    }

    private void putSignosVitales(Context farmacoContext, StringBuilder datos) throws IOException {

        SimWebService service = new SimWebService();

        if (service.validarConexion(farmacoContext)) {
            System.out.println("Red disponible");

            service.configurarMetodo("POST");
            service.configurarUrl("http://192.168.0.3:8080/simWebService/resources/MedicionResource");

            if (service.conectar(farmacoContext,datos.toString().getBytes().length)) {
                System.out.println("Datos "+"\n"+datos);
                service.post(datos.toString());
                System.out.println("-------------");

            }
        }
        else{
            System.out.println("Red No disponible");
            Toast toast = Toast.makeText(farmacoContext,"Red No Disponible",Toast.LENGTH_LONG);
            toast.show();
        }
    }

    private void analizarMediciones() throws IOException {

        vTensionArterial = editTensionArterial.getText().toString();
        vTemperatura = editTemperatura.getText().toString();
        vFrecRespiratoria = editFrecRespiratoria.getText().toString();

        if((editTensionArterial.isEnabled()&& !vTensionArterial.equals(""))){


            getFrecRespiratoriaValores(rootView.getContext(),pacienteActivo.getEdad().toString());

            float valorTensionArterial = Float.parseFloat(vTensionArterial);

            if( valorTensionArterial > TensionArterial.getValorMinimo()
                    && valorTensionArterial < TensionArterial.getValorMinimo()){

                btnAlertaTensionArterial.setVisibility(View.VISIBLE);

            }

        }

        if((editFrecRespiratoria.isEnabled()&& !vFrecRespiratoria.equals(""))){

            ValoresMedicion FrecuenciaRespiratoria = new ValoresMedicion();
            getTemperaturaValores(rootView.getContext(),pacienteActivo.getEdad().toString());

            float valorFreRepiratoria = Float.parseFloat(vFrecRespiratoria);

            if( valorFreRepiratoria > TensionArterial.getValorMinimo()
                    && valorFreRepiratoria < TensionArterial.getValorMinimo()){

                btnAlertaFrecRespiratoria.setVisibility(View.VISIBLE);
            }
        }

        if((editTemperatura.isEnabled()&& !vTemperatura.equals(""))){

            ValoresMedicion Temperatura = new ValoresMedicion();
            getTemperaturaValores(rootView.getContext(),pacienteActivo.getEdad().toString());

            float valorTemperatura = Float.parseFloat(vTemperatura);

            if( valorTemperatura > TensionArterial.getValorMinimo()
                    && valorTemperatura < TensionArterial.getValorMinimo()){
                btnAlertaTemperatura.setVisibility(View.VISIBLE);
            }
        }
    }

    private void getFrecRespiratoriaValores(Context farmacoContext, String edad) throws IOException {
        SimWebService service = new SimWebService();

        if (service.validarConexion(farmacoContext)) {
            System.out.println("Red disponible");

            service.configurarMetodo("POST");
            service.configurarUrl("http://192.168.0.3:8080/simWebService/resources/ValoresFrecuenciaRespiratoriaResource?edad="+edad);

            if (service.conectar(farmacoContext,1)){
                String datos;
                datos = service.get();

                Gson gson = new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd")
                        .create();

                FrecuenciaRepiratoria = gson.fromJson(datos, ValoresMedicion.class) ;

            }
        }
        else{
            System.out.println("Red No disponible");
            Toast toast = Toast.makeText(farmacoContext,"Red No Disponible",Toast.LENGTH_LONG);
            toast.show();
        }
    }

    private void getTemperaturaValores(Context farmacoContext, String edad) throws IOException {
        SimWebService service = new SimWebService();
        if (service.validarConexion(farmacoContext)) {
            System.out.println("Red disponible");

            service.configurarMetodo("GET");
            service.configurarUrl("http://192.168.0.3:8080/simWebService/resources/ValoresFrecuenciaRespiratoriaResource?edad="+edad);

            if (service.conectar(farmacoContext,1)){
                String datos;
                datos = service.get();

                Gson gson = new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd")
                        .create();
                Temperatura =gson.fromJson(datos, ValoresMedicion.class) ;

            }
        }
        else{
            System.out.println("Red No disponible");
            Toast toast = Toast.makeText(farmacoContext,"Red No Disponible",Toast.LENGTH_LONG);
            toast.show();
        }
    }
    private void getTensionArterialValores(Context farmacoContext, String edad) throws IOException {
        SimWebService service = new SimWebService();

        if (service.validarConexion(farmacoContext)) {
            System.out.println("Red disponible");

            service.configurarMetodo("POST");
            service.configurarUrl("\"http://192.168.0.3:8080/simWebService/resources/ValoresFrecuenciaRespiratoriaResource?edad="+edad);

            if (service.conectar(farmacoContext,1)){
                String datos;
                datos = service.get();


                Gson gson = new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd")
                        .create();
                TensionArterial =gson.fromJson(datos, ValoresMedicion.class) ;

            }
        }
        else{
            System.out.println("Red No disponible");
            Toast toast = Toast.makeText(farmacoContext,"Red No Disponible",Toast.LENGTH_LONG);
            toast.show();
        }
    }
}