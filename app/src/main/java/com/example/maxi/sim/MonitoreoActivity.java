package com.example.maxi.sim;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MonitoreoActivity extends AppCompatActivity {

    // Cantidad maxima de pacientes asignados, provisorio
    // Esto se consulta o se pone un numero +grande
    private int ASIGNADOS = 5;

    // LISTA DE MONITOREO
    // Contiene: ID paciente, PPM y Estado
    private int listaMonitoreo[][] = new int[ASIGNADOS][3];

    // LISTA DE PACIENTES ASIGNADOS (provisorio 10 pacientes max)
    // Instancia local provisoria
    private Paciente listaPacientes[] = new Paciente[ASIGNADOS];

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
        for ( int i = 0; i < ASIGNADOS; i ++ ){

            listaMonitoreo[i][0] = i;
            listaPacientes[i].setID(i);
            listaPacientes[i].setNombre("Paciente" + i);
            listaPacientes[i].setApellido("");
            listaPacientes[i].setEdad(30 + i);
            listaPacientes[i].setUbicacion("Sala " + i);

        }


        // MONITOREAR
        // LANZO UN THREAD de escucha
        // Actualizo PPM cada 30 segundos
        // Reviso rangos de PPM segun la edad en el Thread

        // RANGOS
        /*
        Primer mes de vida: 70-190
        Entre uno y once meses: 80-160
        Entre uno y dos años de edad: 80-130
        Entre tres y cuatro años de edad: 80-120
        Entre cinco y seis años de edad: 75-115
        Entre siete y nueve años de edad: 70-110
        A partir de los diez años de edad: 60-100.
         */


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
}
