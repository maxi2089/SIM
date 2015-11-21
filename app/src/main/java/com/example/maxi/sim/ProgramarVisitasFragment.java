package com.example.maxi.sim;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.IOException;
import java.util.Calendar;

/**
 * Created by yamila on 25/10/2015.
 */
public class ProgramarVisitasFragment extends DialogFragment {
    private  View rootView;
   // private static final String URL = "http://192.168.0.3:8080/simWebService/resources/";
    private String URL;
    private DatePicker datePicker;
    private Calendar calendar;
    private TextView txtFecha;
    private TextView txtHora;
    private EditText editObservacion;
    private ImageButton btnCalendario;
    private int year, month, day;
    private ImageButton btnHora;
    private fragmentActivo fragActivo;
    private ImageButton btnGuardar;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        rootView = inflater.inflate(R.layout.fragment_programar_visita, container, false);
        Url urlServer = Url.getInstance();
        URL = urlServer.getUrl();

        fragActivo = fragmentActivo.getInstance();
        fragActivo.setData("CREAR_VISITA");

        btnCalendario = (ImageButton)rootView.findViewById(R.id.btnCalendario);
        btnGuardar  = (ImageButton) rootView.findViewById(R.id.btnGuardar);

        txtFecha = (TextView)rootView.findViewById(R.id.txtFecha);
        txtHora = (TextView)rootView.findViewById(R.id.txtHora);

        editObservacion = (EditText) rootView.findViewById(R.id.TxtAnotacion);

        btnCalendario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                DialogFragment newFragment = new DateFragment();
                newFragment.show(getFragmentManager(), "DatePicker");

            }
        });
        btnHora = (ImageButton)rootView.findViewById(R.id.btnHora);

        btnHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                DialogFragment newFragment = new TimeFragment();
                newFragment.show(getFragmentManager(), "TimePicker");
            }
        });

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                try {
                    crearVisita();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            });
        return rootView;
    }

    public class DateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int yy = calendar.get(Calendar.YEAR);
            int mm = calendar.get(Calendar.MONTH);
            int dd = calendar.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this, yy, mm, dd);
        }
        public void onDateSet(DatePicker view, int yy, int mm, int dd) {
            populateSetDate(yy, mm+1, dd);
        }
        public void populateSetDate(int year, int month, int day) {
            txtFecha.setText(day+"/"+month+"/"+year);
        }
    }

    public class TimeFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int hh = calendar.get(Calendar.HOUR_OF_DAY);
            int mm = calendar.get(Calendar.MINUTE);
            return new TimePickerDialog(getActivity(),this, hh, mm, DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute){
            //Display the user changed time on TextView
            txtHora.setText(String.valueOf(hourOfDay)+":"+String.valueOf(minute));
        }


    }

    private void crearVisita() throws IOException {
        String fecha = txtFecha.getText().toString();
        String hora = txtHora.getText().toString();
        String observacion = editObservacion.getText().toString();

        if(fecha.compareTo("DD - MM - YYYY") !=0
                && hora.compareTo("HH:MM")!=0){
            StringBuilder datosJson = new StringBuilder();
            String fechaHoraJson   = "\""+"fechaVisita"+"\""+":"+"\""+fecha+" "+hora+"\"";


            datosJson.append("{");
            datosJson.append(fechaHoraJson);

            if(!observacion.equals("")) {
                datosJson.append(",");
                String observacionJson = "\""+"observacion"+"\""+":"+"\"" + observacion + "\"";
                datosJson.append(observacionJson);
            }

            datosJson.append("}");
            System.out.println(datosJson.toString());
            //postVisita(rootView.getContext(),datosJson);
        }else{
            Toast toast = Toast.makeText(rootView.getContext(),"Debe completar los campos de fecha y hora",Toast.LENGTH_LONG);
            toast.show();
            System.out.println("toast");
        }

    }
    private void postVisita(Context Context, StringBuilder datos) throws IOException {

        SimWebService service = new SimWebService();

        if (service.validarConexion(Context)) {
            System.out.println("Red disponible");

            service.configurarMetodo("POST");
            service.configurarUrl(URL+"VisitaResource");

            if (service.conectar(Context,datos.toString().getBytes().length)) {
                System.out.println("Datos " + "\n" + datos);
                service.write(datos.toString());
            }
        }
        else{
            Toast toast = Toast.makeText(Context,"Red No Disponible",Toast.LENGTH_LONG);
            toast.show();
        }
    }

}
