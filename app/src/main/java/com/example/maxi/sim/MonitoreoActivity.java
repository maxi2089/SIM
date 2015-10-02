package com.example.maxi.sim;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Random;

public class MonitoreoActivity extends AppCompatActivity {

    // Cantidad de pacientes asignados, provisorio
    // Esto se consulta
    private int ASIGNADOS = 5;

    // Frecuencia de muestreo del ECG en ms
    private int FRECM = 3000;

    // LISTA DE MONITOREO
    // Contiene: [0] ID paciente, [1] PPM y [2] Estado
    private int listaMonitoreo[][] = new int[ASIGNADOS][3];

    // LISTA DE PACIENTES ASIGNADOS (provisorio 5 pacientes)
    // Instancia local provisoria
    private Paciente listaPacientes[] = new Paciente[ASIGNADOS];

    // TABLA DE RANGOS POR EDAD
    private int tabla[][] = new int[10][3];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitoreo);

        // CONSULTAR PACIENTES ASIGNADOS
        // Asigno pacientes de prueba
        listaMonitoreo[0][0] = 0;
        listaMonitoreo[1][0] = 1;
        listaMonitoreo[2][0] = 2;
        listaMonitoreo[3][0] = 3;
        listaMonitoreo[4][0] = 4;

        // CARGAR DATOS DEL PACIENTE:
        // Consulto datos en BD y hago set
        // Nombre, apellido, ubicacion y EDAD
        for (int i = 0; i < ASIGNADOS; i++) {

            listaMonitoreo[i][0] = i;
            listaPacientes[i].setIdPaciente(i);
            listaPacientes[i].setNombre("Paciente" + i);
            listaPacientes[i].setApellido("");
            listaPacientes[i].setEdad(30 + i);
            //listaPacientes[i].setHabitacion("Sala " + i);

        }
    }


    // MONITOREAR
    // LANZO UN THREAD de escucha
    // Actualizo PPM cada 30 segundos
    // Reviso rangos de PPM segun la edad en el Thread


    protected void onStart() {
        super.onStart();

        //TextView mensaje = (TextView) findViewById(R.id.mensaje);
        //mensaje.setText("Texto 01");

        new Thread(new Runnable() {

            @Override
            public void run() {

                Log.i("THREAD", "Mensaje del thread");

                // Genero entradas simuladas
                // Entradas random 10 - 300 con varianza continua

                // Inicializo las ppm
                inicializarPPM(70);

                // Las hago variar aleatoriamente, 30s por ciclo
                for (int i = 0; i < 10; i++) {

                    // Pausa con la frecuencia de muestreo FRECM
                    SystemClock.sleep(FRECM);

                    // Funcion random
                    generarPPM();

                    // Funcion para verificar rangos
                    verificarPPM();

                }

            }
        }).start();
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_monitoreo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    // Funcion para inicializar la TABLA
    void inicializarTabla(){

        /*
        Rangos aceptables:
        Primer mes de vida: 70-190      >>  considero fuera de rango, es muy riesgoso
        Entre uno y once meses: 80-160  >>  idem
        Entre uno y dos años de edad: 80-130
        Entre tres y cuatro años de edad: 80-120
        Entre cinco y seis años de edad: 75-115
        Entre siete y nueve años de edad: 70-110
        A partir de los diez años de edad: 60-100.

        NOTA:
        Puede haber gente con valores algo por debajo (ej. deportistas o por consumo de medicamento)
        La validez de rangos por edad varia segun la fuente, el 60-100 parece general

        Alertas:
        Habria que evaluar la gravedad de la alerta
        */

        tabla[0][0] = 2;
        tabla[0][1] = 80;
        tabla[0][2] = 160;

        tabla[1][0] = 4;
        tabla[1][1] = 80;
        tabla[1][2] = 120;

        tabla[2][0] = 6;
        tabla[2][1] = 75;
        tabla[2][2] = 115;

        tabla[3][0] = 9;
        tabla[3][1] = 70;
        tabla[3][2] = 110;

        tabla[4][0] = 999;
        tabla[4][1] = 60;
        tabla[4][2] = 100;

    }



    // Funcion para inicializar los PPM
    void inicializarPPM( int x ){

        for ( int i=0; i<ASIGNADOS; i++ )
            listaMonitoreo[i][1] = x ;

    }



    // Funcion para generar muestra random
    void generarPPM(){

        Random r = new Random();

        listaMonitoreo[0][1] += r.nextInt(9) -4 ;  // [ +4 -4 ]
        listaMonitoreo[1][1] += r.nextInt(7) -2 ;  // [ +4 -2 ]
        listaMonitoreo[2][1] += r.nextInt(7) -4 ;  // [ +2 -4 ]
        listaMonitoreo[3][1] += r.nextInt(9) -3 ;  // [ +5 -3 ]
        listaMonitoreo[4][1] += r.nextInt(9) -5 ;  // [ +3 -5 ]

        for (int j = 0; j < ASIGNADOS; j++) {

            //Verifico que el valor no salgo de rango
            if ( listaMonitoreo[j][1] < 10 )
                listaMonitoreo[j][1] = 10;
            if ( listaMonitoreo[j][1] > 300 )
                listaMonitoreo[j][1] = 300;

        }

    }


    void verificarPPM() {

        int ppm;
        int edad;

        for (int k = 0; k < ASIGNADOS; k++) {

            ppm = listaMonitoreo[k][1];
            edad = listaPacientes[k].getEdad();

            for (int i = 0; i < 5; i++) {

                if (edad < tabla[i][0]) {

                    if (ppm > tabla[i][2]) {      // MAX
                        //listaMonitoreo[k][2] = 1; // PONER ESTADO!
                        //generarAlerta(k);
                    }
                    if (ppm < tabla[i][1]) {  // MIN
                        //listaMonitoreo[k][2] = 1; // PONER ESTADO!
                        //generarAlerta(k);
                    }

                }
            }

        }

    }

}   // FIN
