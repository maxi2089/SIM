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
    private static final String URL = "http://192.168.0.3:8080/simWebService/resources/LibroReportResource";


    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
         rootView = inflater.inflate(R.layout.fragment_farmaco, container, false);

        fragmentActivo fragActivo =  fragmentActivo.getInstance();
        fragActivo.setData("FARMACO");

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
                //Conectarse con el web service para que envie el msj al enfermero a cargo
                //Se postea en el libro report
                String datos = new String();
            // datos ="{"+"\""+"idLibroReport"+"\""+":"+0+","+"\""+"fechaAlta"+"\":"+"\""+"2015-09-16"+"\""+","+"\""+"estado"+"\":"+"\""+"ACTIVO"+"\""+","+"\""+"paciente"+"\""+":"+"{"+"\""+"idPaciente"+"\""+":"+0+","+"\""+"dni"+"\":"+"34809913"+","+"\""+"nombre"+"\":"+"\""+"Maxi"+"\""+","+"\""+"apellido"+"\":"+"\""+"Akike"+"\""+","+"\""+"edad"+"\":"+"26"+","+"\""+"altura"+"\":"+"1.75"+","+"\""+"peso"+"\""+":"+"1.8"+"}}";
              //datos ="{"+"\""+"fechaAlta"+"\":"+"\""+"Oct 10, 2015"+"\""+","+"\""+"fechaBaja"+"\""+":"+"\""+"Oct 15, 2015"+"\""+","+"\""+"estado"+"\":"+"\""+"ACTIVO"+"\""+","+"\""+"paciente"+"\""+":"+"{"+"\""+"idPaciente"+"\""+":"+0+","+"\""+"dni"+"\":"+"34809913"+","+"\""+"nombre"+"\":"+"\""+"Maxi"+"\""+","+"\""+"apellido"+"\":"+"\""+"Akike"+"\""+","+"\""+"edad"+"\":"+"26"+","+"\""+"altura"+"\":"+"1.75"+","+"\""+"peso"+"\""+":"+"1.8"+"}"+","+"\""+"medicions"+"\""+":"+"[]"+"}";
               datos= "{"
                        +"\""+"fechaAlta"+"\":"+"\""+"Oct 10, 2015 9:24:43 PM"+"\""
                        +","
                        +"\""+"fechaBaja"+"\""+":"+"\""+"Oct 10, 2015 9:24:43 PM"+"\""
                        +","
                        +"\""+"estado"+"\":"+"\""+"Activo"+"\""
                        +","
                        +"\""+"paciente"+"\""+":"+"{"
                        +"\""+"idPaciente"+"\""+":"+0
                        +","
                        +"\""+"dni"+"\":"+"34"
                        +","
                        +"\""+"nombre"+"\":"+"\""+"Maxi"+"\""
                        +","
                        +"\""+"apellido"+"\":"+"\""+"Akike"+"\""
                        +","
                        +"\""+"edad"+"\":"+"26"
                        +","
                        +"\""+"altura"+"\":"+"1.8"
                        +","
                        +"\""+"peso"+"\""+":"+"90.0"+"}"
                        +","
                        +"\""+"medicions"+"\""+":"
                        +"[]"+"}";
           try {
                    setFarmaco(rootView.getContext(),datos);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                datos = "Administrar:xxxxxx \n"
                        +"Factor Goteo(ml x h): "+resultadoVolumen.getText()+"\n"
                        +"Factor Goteo(gotas x min): "+resultadoGotas.getText()+"\n"
                ;

            }
        });

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
            service.configurarUrl(URL);

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
