package com.example.maxi.sim;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class FarmacoFragment extends Fragment {
   // private ArrayList<Paciente> ListaPaciente;
    private ListaPacientes ListaPaciente;

    private EditText volumen;
    private EditText tiempo;
    private TextView resultadoVolumen;
    private TextView resultadoGotas;
    private ImageView macroGotas;
    private ImageView microGotas;
    private Switch factorGoteo;
    private Button btnCalcular;
    private Button btnEnviar;
    private Double factorG = 60.00;
    private ImageButton btnReport;
    private Integer libroReportFlag;
    private String org;
    private TextView txtPaciente;
    private  View rootView;
    private PacienteActivo pacienteActivo;
    //private static final String URL = "http://192.168.0.3:8080/simWebService/resources/LibroReportResource";
    private String URL;

    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
         rootView = inflater.inflate(R.layout.fragment_farmaco, container, false);

        fragmentActivo fragActivo =  fragmentActivo.getInstance();
        fragActivo.setData("FARMACO");

        Url urlServer = Url.getInstance();
        URL = urlServer.getUrl();

       //ListaPaciente = (ArrayList<Paciente>)getArguments().getSerializable("LISTA");
        btnReport = (ImageButton) rootView.findViewById(R.id.libroReport);

        txtPaciente = (TextView)rootView.findViewById(R.id.txtPaciente);

        pacienteActivo = PacienteActivo.getInstance();

        //pacienteActivo = (Paciente)getArguments().getSerializable("PACIENTE");
        txtPaciente.setText(pacienteActivo.getPaciente().getNombre()+" "+pacienteActivo.getPaciente().getApellido());


        microGotas = (ImageView)rootView.findViewById(R.id.imageMicroGota);
        macroGotas = (ImageView)rootView.findViewById(R.id.imageMacroGota);

        factorGoteo = (Switch) rootView.findViewById(R.id.factorGoteo);

        factorGoteo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if(isChecked){
                    //MicroGota
                    macroGotas.setVisibility(View.INVISIBLE);
                    microGotas.setVisibility(View.VISIBLE);
                    factorG=60.0;
                    System.out.println("FACTO GOTEO "+factorG );
                }else{
                    //MacroGota
                    macroGotas.setVisibility(View.VISIBLE);
                    microGotas.setVisibility(View.INVISIBLE);
                    factorG=20.0;
                    System.out.println("FACTO GOTEO "+factorG );

                }

            }
        });

        volumen   = (EditText) rootView.findViewById(R.id.TxtVolumen);
        tiempo    = (EditText) rootView.findViewById(R.id.TxtTiempo);
        resultadoVolumen = (TextView) rootView.findViewById(R.id.TxtResultadoVolumen);
        resultadoGotas = (TextView) rootView.findViewById(R.id.TxtResultadoGotas);

        btnCalcular =  (Button) rootView.findViewById(R.id.btnCalcular);
        btnEnviar   =  (Button) rootView.findViewById(R.id.btnEnviar);

        btnCalcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Double nVol,nTiempo,nResultado;
               // getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                InputMethodManager inputMethodManager = (InputMethodManager)  getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                if(!volumen.getText().toString().equals("")
                        && !tiempo.getText().toString().equals("") ){
                    DecimalFormat df = new DecimalFormat("#####.##");
                    //Obtenemos los valores ingresados de volumen y tiempo
                    nVol = Double.parseDouble(volumen.getText().toString());
                    nTiempo = Double.parseDouble(tiempo.getText().toString());
                    //Calculamos el resultado en  ml/hr
                    nResultado = ( nVol / (nTiempo / 60));
                    resultadoVolumen.setText(df.format(nResultado).toString() + " ml/Hr");

                    //Calculamos el resultado en  gotas/min
                    nResultado = ((nVol * factorG) / (nTiempo * 60));

                    resultadoGotas.setText(df.format(nResultado).toString() + " Gotas/min");
                }else{
                    Toast toast = Toast.makeText(rootView.getContext(),"No se puede calcular. No hay datos cargados", Toast.LENGTH_LONG);
                    toast.show();
                }
                }


        });

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!volumen.getText().toString().equals("")
                        && !tiempo.getText().toString().equals("")
                        && !resultadoGotas.getText().toString().equals("")
                        && !resultadoVolumen.getText().toString().equals("")) {
                    StringBuilder datos = new StringBuilder();

                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    String currentDateandTime = sdf.format(new Date());

                    String libroReportJson = "\""+"idLibroReport"+"\""+":"+pacienteActivo.getPaciente().getIdPaciente().toString();
                    String fechaJson       = "\""+"fecha"        +"\""+":"+"\""+currentDateandTime.toString()+"\"";
                    String volumeJson      = "\""+"volumen"      +"\""+":"+resultadoVolumen.getText().toString() ;
                    String gotasJson       = "\""+"gotas"        +"\""+":"+resultadoGotas.getText().toString() ;

                    datos.append("{");
                    datos.append(libroReportJson);
                    datos.append(",");
                    datos.append(fechaJson);
                    datos.append(",");
                    datos.append(volumeJson);
                    datos.append(",");
                    datos.append(gotasJson);
                    datos.append("}");


                    try {
                        setFarmaco(rootView.getContext(), datos.toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast toast = Toast.makeText(rootView.getContext(), "No se puede calcular. No hay datos cargados", Toast.LENGTH_LONG);
                    toast.show();
                }


            }});

        btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              MostrarFragment();
            }
        });
        return rootView;

    }





    private void MostrarFragment() {
        // update the main content by replacing fragments
        Fragment fragment =  new LibroReportFragment();
        if(pacienteActivo.getPaciente() != null){

            //Validamos si el fragment no es nulo
            if (fragment != null) {

                //Creamos  Bundle que guardara los datos para enviar al fragment de paciente
                Bundle  datos = new Bundle();

                //datos.putSerializable("PACIENTE", pacienteActivo);
                datos.putString("ORIGEN", "FARMACO");

                fragment.setArguments(datos);

                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
            } else {
                //Si el fragment es nulo mostramos un mensaje de error.
                Log.e("Error  ", "No se puedo encontrara el fragmento LibroReportFragment");
            }
        }
        else{
            Log.e("Error  ", "No selecciono paciente paciente");

        }
    }


    private void setFarmaco(Context farmacoContext, String datos) throws IOException {

        SimWebService service = new SimWebService();

        if (service.validarConexion(farmacoContext)) {
            System.out.println("Red disponible");

            service.configurarMetodo("POST");
            service.configurarUrl(URL+"LibroReportResource");

             if (service.conectar(farmacoContext,datos.getBytes().length)) {
                 System.out.println("Datos "+"\n"+datos);
                 service.write(datos);
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
