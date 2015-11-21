package com.example.maxi.sim;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
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
 * Created by yamila on 07/11/2015.
 */
public class VisitaFragment extends Fragment{

    private View rootView;
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
    private String fecha[];
    private String anotacion;
    private ImageButton btnEliminar;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        rootView = inflater.inflate(R.layout.fragment_visita, container, false);
        Url urlServer = Url.getInstance();
        URL = urlServer.getUrl();

        fragActivo = fragmentActivo.getInstance();
        fragActivo.setData("MODIFICAR_VISITA");

        fecha = getArguments().getString("FECHA").split(" ");
        anotacion = getArguments().getString("ANOTACION");

        btnCalendario = (ImageButton)rootView.findViewById(R.id.btnCalendario);
        btnGuardar  = (ImageButton) rootView.findViewById(R.id.btnGuardar);

        txtFecha = (TextView)rootView.findViewById(R.id.txtFecha);
        txtHora = (TextView)rootView.findViewById(R.id.txtHora);

        editObservacion = (EditText) rootView.findViewById(R.id.TxtAnotacion);

        inicializarVisita();
        btnEliminar = (ImageButton) rootView.findViewById(R.id.btnEliminar);
        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EliminarVisita eliminarVisita = new EliminarVisita();
                eliminarVisita.show(getFragmentManager(), "Eliminar");
            }
        });

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
                    modificarVisita();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        return rootView;
    }

    public void inicializarVisita(){
        txtFecha.setText(fecha[0]);
        txtHora.setText(fecha[1]);
        editObservacion.setText(anotacion);
    }

    private void modificarVisita() throws IOException {
        String fechaVisita = txtFecha.getText().toString();
        String horaVisita = txtHora.getText().toString();
        String obsVisita = editObservacion.getText().toString();

        Boolean huboCambio = false;
        StringBuilder fechaHoraJson  =  new StringBuilder();
        String horaJson;
        StringBuilder datosJson = new StringBuilder();
        datosJson.append("{");

        if(fechaVisita.compareTo(fecha[0])!=0) {
            huboCambio = true;
            fechaHoraJson.append("\""+"fecha"+"\""+":"+"\""+ fechaVisita + " ");
        }else
            fechaHoraJson.append("\""+"fecha"+"\""+":"+"\""+ fecha[0] + " ");

        if(horaVisita.compareTo(fecha[1])!=0) {
            huboCambio = true;
            horaJson = horaVisita;
        }else
            horaJson = fecha[1];


        if(huboCambio) {
            fechaHoraJson.append(horaJson+"\"");
            datosJson.append(fechaHoraJson.toString());
        }

        if(obsVisita.compareTo(anotacion)!=0) {
            if(huboCambio)
                datosJson.append(",");
            datosJson.append("\"" + "Anotacion" + "\"" + ":" + "\"" + obsVisita + "\"");
            huboCambio = true;
        }

        if(huboCambio){
            datosJson.append("}");

            System.out.println("datos : "+datosJson.toString());
            //putVisita(rootView.getContext(),datosJson);
            DialogoExito exito = new DialogoExito();
            exito.show(getFragmentManager(),"Visita");
            fecha[0] = fechaVisita;
            fecha[1] = horaVisita;
            anotacion = obsVisita;
        }else{
            Toast toast = Toast.makeText(rootView.getContext(),"No se registraron cambios en la visita",Toast.LENGTH_LONG);
            toast.show();
        }


    }
    private void putVisita(Context Context, StringBuilder datos) throws IOException {

        SimWebService service = new SimWebService();

        if (service.validarConexion(Context)) {
            System.out.println("Red disponible");

            service.configurarMetodo("PUT");
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
    @SuppressLint("ValidFragment")
    public class EliminarVisita extends DialogFragment {
        private  Context context;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            AlertDialog.Builder builder =
                    new AlertDialog.Builder(getActivity());
            String mensaje  ="Desea eliminar la visita Programada?";
            context = rootView.getContext();

            builder.setMessage(mensaje)
                    .setTitle( "Eliminar Visita Programada")
                    .setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Log.i("Dialogos", "Confirmacion Aceptada.");

                            Fragment fragment;
                            fragment = new ListaVisitasFragment();
                            if (fragment != null) {
                                if(deleteVisita(context)) {
                                    fragmentActivo fragActivo = fragmentActivo.getInstance();
                                    fragActivo.setData("PACIENTE_VISITAS");
                                    FragmentManager fragmentManager = getFragmentManager();
                                    fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                                    dialog.cancel();
                                }
                            }
                        }})
                    .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Log.i("Dialogos", "Confirmacion Cancelada.");
                            dialog.cancel();
                        }
                    });

            return builder.create();
        }


    }

    private boolean deleteVisita(Context context){
       /* SimWebService service = new SimWebService();

        if (service.validarConexion(context)) {
            System.out.println("Red disponible");

            service.configurarMetodo("DELETE");
            service.configurarUrl(URL);

            if (service.conectar(context, 1)) {
                service.delete();
            }else
                return false;
        }else
            return false;*/
        return true;
    }

}
