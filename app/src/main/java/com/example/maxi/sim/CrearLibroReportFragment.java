package com.example.maxi.sim;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by yamila on 12/10/2015.
 */
public class CrearLibroReportFragment extends Fragment {

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

    private ImageButton btnGuardar;
    private View rootView;
    private fragmentActivo fragActivo;
    private String URL;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_crear_libro_report, container, false);

        fragActivo = fragmentActivo.getInstance();
        fragActivo.setData("CREAR_LIBRO_REPORT");

        Url urlServer = Url.getInstance();
        URL = urlServer.getUrl();


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


        btnGuardar = (ImageButton) rootView.findViewById(R.id.btnCrearVisita);
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

                        //Variable para armar el dato JSON
                        //String datosJson = new String();
                        String fechaAltaJson = "\"" + "fechaAlta" + "\"" + ":" + "\"" + "Oct 10, 2015 9:24:43 PM" + "\"";
                        String fechaBajaJson = "\"" + "fechaBaja" + "\"" + ":" + "\"" + "Oct 10, 2015 9:24:43 PM" + "\"";
                        String NombreJson = "\"" + "nombre" + "\"" + ":" + "\"" + vNombre + "\"";
                        String ApellidoJson = "\"" + "apellido" + "\"" + ":" + "\"" + vApellido + "\"";
                        String DniJson = "\"" + "dni" + "\"" + ":" + "\"" + vDni + "\"";
                        String AlturaJson = "\"" + "altura" + "\"" + ":" + vAltura;
                        String PesoJson = "\"" + "peso" + "\"" + ":" + vPeso;
                        String EdadJson = "\"" + "edad" + "\"" + ":" + vEdad;
                        String EstadoJson = "\"" + "estado" + "\"" + ":" + "\"" + "Activo" + "\"";
                        String PacienteJson = "\"" + "paciente" + "\"" + ":";

                        //datos ="{"+"\""+"idPaciente"+"\":"+"5"+","+"\""+"dni"+"\":"+"34809913"+","+"\""+"nombre"+"\":"+"\""+"Maxi"+"\""+","+"\""+"apellido"+"\":"+"\""+"Akike"+"\""+","+"\""+"edad"+"\":"+"26"+","+"\""+"altura"+"\":"+"1.75"+"}";
                        StringBuilder datosJson = new StringBuilder();

                        datosJson.append("{");//"
                        datosJson.append(fechaAltaJson);
                        datosJson.append(",");
                        datosJson.append(fechaBajaJson);
                        datosJson.append(",");
                        datosJson.append(EstadoJson);
                        datosJson.append(",");
                        datosJson.append(PacienteJson);
                        datosJson.append("{");
                        datosJson.append("\"");
                        datosJson.append("idPaciente");
                        datosJson.append("\"");
                        datosJson.append(":0,");
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
                        datosJson.append("}}");

                        System.out.println("Datos" + datosJson.toString());
                        try {
                            postLibroReport(rootView.getContext(),datosJson);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast toastRequerido = Toast.makeText(rootView.getContext(),
                                "Todos los campos deben ser completados", Toast.LENGTH_LONG);

                        toastRequerido.show();
                    }
                }



        });

        return rootView;
    }

    private void postLibroReport(Context Context, StringBuilder datos) throws IOException {

        SimWebService service = new SimWebService();

        if (service.validarConexion(Context)) {
            System.out.println("Red disponible");

            service.configurarMetodo("POST");
            service.configurarUrl(URL+"LibroReportResource");

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
}

