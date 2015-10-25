package com.example.maxi.sim;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by yamila on 23/10/2015.
 */
public class ModificarLibroReportFragment extends Fragment {
    private TextInputLayout TiLayoutNombre;
    private TextInputLayout TiLayoutApellido;
    private TextInputLayout TiLayoutDni;
    private TextInputLayout TiLayoutEdad;
    private TextInputLayout TiLayoutAltura;
    private TextInputLayout TiLayoutPeso;

    private EditText EditTxtNombre;
    private EditText EditTxtApellido;
    private EditText EditTxtDni;
    private EditText EditTxtEdad;
    private EditText EditTxtAltura;
    private EditText EditTxtPeso;
    private Button btnGuardar;

    private View rootView;
    private fragmentActivo fragActivo;
    private PacienteActivo pacienteActivo;
    private ListaPacientes ListaPaciente;

    private static final String URL = "http://192.168.0.3:8080/simWebService/resources/";

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        rootView = inflater.inflate(R.layout.fragment_modificar_report, container, false);


        fragActivo = fragmentActivo.getInstance();
        fragActivo.setData("MODIFICAR_LIBRO_REPORT");
        pacienteActivo = PacienteActivo.getInstance();



        TiLayoutNombre = (TextInputLayout) rootView.findViewById(R.id.TiLayoutNombre);
        TiLayoutApellido = (TextInputLayout) rootView.findViewById(R.id.TiLayoutApellido);
        TiLayoutDni = (TextInputLayout) rootView.findViewById(R.id.TiLayoutDni);
        TiLayoutEdad = (TextInputLayout) rootView.findViewById(R.id.TiLayoutEdad);
        TiLayoutAltura = (TextInputLayout) rootView.findViewById(R.id.TiLayoutAltura);
        TiLayoutPeso = (TextInputLayout) rootView.findViewById(R.id.TiLayoutPeso);


        EditTxtNombre = (EditText) rootView.findViewById(R.id.EditTxtNombre);
        EditTxtApellido = (EditText) rootView.findViewById(R.id.EditTxtApellido);
        EditTxtDni = (EditText) rootView.findViewById(R.id.EditTxtDni);
        EditTxtEdad = (EditText) rootView.findViewById(R.id.EditTxtEdad);
        EditTxtAltura = (EditText) rootView.findViewById(R.id.EditTxtAltura);
        EditTxtPeso = (EditText) rootView.findViewById(R.id.EditTxtPeso);

        inicializarLibroReport();

        btnGuardar = (Button) rootView.findViewById(R.id.btnGuardar);
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String vNombre = EditTxtNombre.getText().toString();
                String vApellido = EditTxtApellido.getText().toString();
                String vDni = EditTxtDni.getText().toString();
                String vEdad = EditTxtEdad.getText().toString();
                String vAltura = EditTxtAltura.getText().toString();
                String vPeso = EditTxtPeso.getText().toString();

                if (!vNombre.equals("")
                        && !vApellido.equals("")
                        && !vDni.equals("")
                        && !vEdad.equals("")
                        && !vAltura.equals("")
                        && !vPeso.equals("")) {

                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                    String currentDateandTime = sdf.format(new Date());


                    String NombreJson = "\"" + "nombre" + "\"" + ":" + "\"" + vNombre + "\"";
                    String ApellidoJson = "\"" + "apellido" + "\"" + ":" + "\"" + vApellido + "\"";
                    String DniJson = "\"" + "dni" + "\"" + ":" + "\"" + vDni + "\"";
                    String AlturaJson = "\"" + "altura" + "\"" + ":" + vAltura;
                    String PesoJson = "\"" + "peso" + "\"" + ":" + vPeso;
                    String EdadJson = "\"" + "edad" + "\"" + ":" + vEdad;
                    String EstadoJson = "\"" + "estado" + "\"" + ":" + "\"" + "Activo" + "\"";
                    String PacienteJson = "\"" + "paciente" + "\"" + ":";
                    String idPacienteJson =  "\"" + "idPaciente" + "\"" + ":" +pacienteActivo.getPaciente().getIdPaciente().toString();
                   //datos ="{"+"\""+"idPaciente"+"\":"+"5"+","+"\""+"dni"+"\":"+"34809913"+","+"\""+"nombre"+"\":"+"\""+"Maxi"+"\""+","+"\""+"apellido"+"\":"+"\""+"Akike"+"\""+","+"\""+"edad"+"\":"+"26"+","+"\""+"altura"+"\":"+"1.75"+"}";
                    StringBuilder datosJson = new StringBuilder();

                    //datosJson.append("{");//"
                    //datosJson.append(libroReport);
                    //datosJson.append(",");
                    //datosJson.append(fechaAltaJson);
                    //datosJson.append(",");
                    //datosJson.append(fechaBajaJson);
                    //datosJson.append(",");
                    //datosJson.append(EstadoJson);
                    //datosJson.append(",");
                    //datosJson.append(PacienteJson);
                    datosJson.append("{");
                    datosJson.append(idPacienteJson);
                    datosJson.append(",");
                    datosJson.append(DniJson);
                    datosJson.append(",");
                    datosJson.append(NombreJson);
                    datosJson.append(",");
                    datosJson.append(ApellidoJson);
                    datosJson.append(",");
                    datosJson.append(EdadJson);
                    datosJson.append(",");
                    datosJson.append(AlturaJson);
                    datosJson.append(",");
                    datosJson.append(PesoJson);
                    datosJson.append("}");

                    System.out.println("Datos" + datosJson.toString());
                    try {
                        updatePacienteReport(rootView.getContext(), datosJson);
                        refrescarPacienteReportLocal();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast toastRequerido = Toast.makeText(rootView.getContext(),
                            "Todos los campos deben ser completados", Toast.LENGTH_LONG);
                    toastRequerido.show();
                }

        }});
        return rootView;
    }

    private void inicializarLibroReport(){
        EditTxtNombre.setText(pacienteActivo.getPaciente().getNombre());
        EditTxtApellido.setText(pacienteActivo.getPaciente().getApellido());
        EditTxtDni.setText(pacienteActivo.getPaciente().getDni().toString());
        EditTxtEdad.setText(pacienteActivo.getPaciente().getEdad().toString());
        EditTxtAltura.setText(pacienteActivo.getPaciente().getAltura().toString());
        EditTxtPeso.setText(pacienteActivo.getPaciente().getPeso().toString());
    }

    private void updatePacienteReport(Context Context, StringBuilder datos) throws IOException {

        SimWebService service = new SimWebService();

        if (service.validarConexion(Context)) {
            System.out.println("Red disponible");

            service.configurarMetodo("PUT");
            service.configurarUrl(URL+"PacienteResource?id="+pacienteActivo.getPaciente().getIdPaciente().toString());

            if (service.conectar(Context,datos.toString().getBytes().length)) {
                System.out.println("Datos " + "\n" + datos);
                service.post(datos.toString());
            }
        }
        else{
            Toast toast = Toast.makeText(Context,"Red No Disponible",Toast.LENGTH_LONG);
            toast.show();
        }
    }

    //Actualizo los datos locales por el caso de que alguna modificacion realizada sea invalida
    public void refrescarPacienteReportLocal(){

        Paciente paciente;
        SimWebService service = new SimWebService();

        if(service.validarConexion(rootView.getContext())){
            System.out.println("Red disponible");

            service.configurarMetodo("GET");
            service.configurarUrl(URL+"PacienteResource?id="+pacienteActivo.getPaciente().getIdPaciente().toString());

            if(service.conectar(rootView.getContext(),0)) {
                String datos;
                datos = service.get();
                Gson gson = new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd")
                        .create();
                System.out.println("GET: " + datos);

                paciente  = gson.fromJson(datos, Paciente.class);
                pacienteActivo.setPaciente(paciente);


            }
            else {
                Toast toast = Toast.makeText(rootView.getContext(),"No se obtuvieron datos del paciente",Toast.LENGTH_LONG);
                toast.show();
            }
        }
        else{
            Toast toast = Toast.makeText(rootView.getContext(),"Red No disponible",Toast.LENGTH_LONG);
            toast.show();
        }
    }


}
