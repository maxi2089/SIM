package com.example.maxi.sim;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import static com.example.maxi.sim.R.color.color_texto;


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

    private Button btnGuardar;
    private View rootView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        rootView = inflater.inflate(R.layout.fragment_signos_vitales, container, false);

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




        //Guardar
        btnGuardar = (Button) rootView.findViewById(R.id.btnGuardar);

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
                String datos = new String();
                datos ="{"+"\""+"idPaciente"+"\":"+"5"+","+"\""+"dni"+"\":"+"34809913"+","+"\""+"nombre"+"\":"+"\""+"Maxi"+"\""+","+"\""+"apellido"+"\":"+"\""+"Akike"+"\""+","+"\""+"edad"+"\":"+"26"+","+"\""+"altura"+"\":"+"1.75"+"}";
                try {
                    setSignosVitales(rootView.getContext(),datos);
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        });


        return rootView;

    }

    private void setSignosVitales(Context farmacoContext, String datos) throws IOException {

        ServiceActiviy service = new ServiceActiviy();

        if (service.validarConexion(farmacoContext)) {
            System.out.println("Red disponible");

            service.configurarMetodo("POST");
            service.configurarUrl("http://192.168.0.6:8080/simWebService/resources/MedicionResource");

            if (service.conectar(farmacoContext,datos.getBytes().length)) {
                System.out.println("Datos "+"\n"+datos);
                service.post(datos);
                System.out.println("-------------");

            }
        }
        else{
            System.out.println("Red No disponible");
            Toast toast = Toast.makeText(farmacoContext,"Red No Disponible",Toast.LENGTH_LONG);
            toast.show();
        }
    }
}