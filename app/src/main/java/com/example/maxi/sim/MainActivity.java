package com.example.maxi.sim;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.TypedArray;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.app.NotificationCompat.*;

import android.support.v7.internal.widget.ActionBarContainer;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Vector;


public class MainActivity extends AppCompatActivity {
    private String[] titulos;
    private DrawerLayout NavDrawerLayout;
    private ListView NavList;
    private ArrayList<Item_objct> NavItms;
    private TypedArray NavIcons;
    private NavigationAdapter NavAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListaPacientes ListaPaciente;
    //private Bundle pacientes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Notificacion notificacion= new  Notificacion(this.getApplicationContext());

        NotificationManager notifManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        int notif_ref = 1;
        notifManager.notify(notif_ref,notificacion.getNotificacion("Titulo","Contenido","Resumen","ContenidoResumido"));
        notif_ref = 2;
        notifManager.notify(notif_ref,notificacion.getNotificacion("Titulo","Contenido","Resumen","ContenidoResumido"));


        ListaPaciente = ListaPacientes.getInstance();

        // Listapaciente = new ArrayList<Paciente>();
        Paciente paciente;

       ServiceActiviy service = new ServiceActiviy();
        if(service.validarConexion(this.getApplicationContext())){
            System.out.println("Red disponible");

            service.configurarMetodo("GET");
            service.configurarUrl("http://192.168.0.3:8080/simWebService/resources/PacienteResource?id=1");
            if(service.conectar(this.getApplicationContext(),0)) {
                String datos;
                datos = service.get();
                  // paciente = new Paciente();
                    Gson gson = new GsonBuilder()
                            .setDateFormat("yyyy-MM-dd")
                            .create();
                    System.out.println("GEEET "+datos);

                    paciente  = gson.fromJson(datos, Paciente.class);
                   // paciente.parserJsonPaciente(datos);
                    ListaPaciente.setLista(paciente);

            }
            else {
               // ListaPaciente.setLista(paciente);

                //Listapaciente.add(paciente);

            }
        }
        else{
            System.out.println("Red No disponible");
          //  ListaPaciente.setLista(paciente);

            // Listapaciente.add(paciente);


        }
        paciente = new Paciente(111, "Maximiliano", "Akike", 34809913,/*"105"*/25, 5.23, 8.3/*,"Infarto Agudo del Miocardio"*/);

        ListaPaciente.setLista(paciente);
        ListaPaciente.setLista(paciente);
        ListaPaciente.setLista(paciente);

           /* pacientes = new Bundle();
            pacientes.putSerializable("LISTA", ListaPaciente.getLista());*/

        //////////////////////////////////////////////////////////////////////////////

        NavDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        //Lista
        NavList = (ListView) findViewById(R.id.lista);

        View header = getLayoutInflater().inflate(R.layout.header, null);

        NavList.addHeaderView(header);

        NavIcons = getResources().obtainTypedArray(R.array.navigation_iconos);

        titulos = getResources().getStringArray(R.array.nav_options);

        NavItms = new ArrayList<Item_objct>();

        //Elementos de la lista del meno
        NavItms.add(new Item_objct(titulos[0], NavIcons.getResourceId(0, -1)));
        NavItms.add(new Item_objct(titulos[1], NavIcons.getResourceId(1, -1)));
        NavItms.add(new Item_objct(titulos[2], NavIcons.getResourceId(2, -1)));
        NavItms.add(new Item_objct(titulos[3], NavIcons.getResourceId(3, -1)));
        NavItms.add(new Item_objct(titulos[4], NavIcons.getResourceId(4, -1)));
        NavItms.add(new Item_objct(titulos[5], NavIcons.getResourceId(5, -1)));
        NavItms.add(new Item_objct(titulos[6], NavIcons.getResourceId(6, -1)));


        NavAdapter = new NavigationAdapter(this, NavItms);
        NavList.setAdapter(NavAdapter);


        //Declaramos el mDrawerToggle y las imgs a utilizar
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                NavDrawerLayout,         /* DrawerLayout object */
                R.string.app_name,  /* "open drawer" description */
                R.string.hello_world  /* "close drawer" description */
        ) {

            /**
             * Called when a drawer has settled in a completely closed state.
             */
            public void onDrawerClosed(View view) {
                Log.e("Cerrado completo", "!!");
            }

            /**
             * Called when a drawer has settled in a completely open state.
             */
            public void onDrawerOpened(View drawerView) {
                Log.e("Apertura completa", "!!");
            }
        };

        // Establecemos que mDrawerToggle declarado anteriormente sea el DrawerListener
        NavDrawerLayout.setDrawerListener(mDrawerToggle);
        //Establecemos que el ActionBar muestre el Boton Home
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //Establecemos la accion al clickear sobre cualquier item del menu.
        //De la misma forma que hariamos en una app comun con un listview.
        NavList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
                MostrarFragment(position);
            }
        });

        MostrarFragment(1);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
       /* int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }*/

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            Log.e("mDrawerToggle pushed", "x");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*Pasando la posicion de la opcion en el menu nos mostrara el Fragment correspondiente*/
    private void MostrarFragment(int position) {
        // update the main content by replacing fragments
        Fragment fragment = null;
        fragmentActivo fragActivo = fragmentActivo.getInstance();
        switch (position) {
            case 1:
                fragment = new HomeFragment();
                fragActivo.setData("HOME");
                break;
            case 2:
                fragment = new EcgFragment();
                fragActivo.setData("ECG");

                break;
            case 3:
                fragActivo.setData("LISTA_FARMACO");
                fragment = new ListaPacientesFragment();

                break;
            case 4:
                fragActivo.setData("LISTA_REPORT");
                fragment = new ListaPacientesFragment();
                break;
            case 5:
                fragActivo.setData("LISTA_SIGNOS");
                fragment = new ListaPacientesFragment();

                break;
            case 6:
                fragActivo.setData("LISTA_GLUCOSA");
                fragment = new ListaPacientesFragment();
                break;
            case 7:
                this.finish();
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                break;
            default:
                //si no esta la opcion mostrara un toast y nos mandara a Home
                Toast.makeText(getApplicationContext(), "Opcion " + titulos[position - 1] + "no disponible!", Toast.LENGTH_SHORT).show();
                position = 1;
                break;

        }


        //Validamos si el fragment no es nulo
        if (fragment != null) {
            //  pacientes.putString("ORG", "MAIN");
            // fragment.setArguments(pacientes);

            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

            // Actualizamos el contenido segun la opcion elegida
            NavList.setItemChecked(position, true);
            NavList.setSelection(position);
            //Cambiamos el titulo en donde decia "
            setTitle(titulos[position - 1]);
            //Cerramos el menu deslizable
            NavDrawerLayout.closeDrawer(NavList);
        } else {
            //Si el fragment es nulo mostramos un mensaje de error.
            Log.e("Error  ", "MostrarFragment" + position);
        }
    }

    @Override
    public void onBackPressed() {
        fragmentActivo fragActivo = fragmentActivo.getInstance();
        System.out.println(fragActivo.getData());

        if (fragActivo.getData() == "HOME") {
            System.out.println("home");

            //  DialogFragment dialog = new Dialogo();
            // dialog.show(getSupportFragmentManager(), "dialog");
            this.onPause();

            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        } else if (fragActivo.getData() == "FARMACO") {
            System.out.println("FARMACO");
            MostrarFragment(3);
        } else if (fragActivo.getData() == "REPORT") {
            System.out.println("REPORT");
            MostrarFragment(4);
        } else if (fragActivo.getData() == "SIGNOS") {
            System.out.println("SIGNOS");
            MostrarFragment(5);
        } else if (fragActivo.getData() == "GLUCOSA") {
            System.out.println("GLUCOSA");
            MostrarFragment(6);
        } else {
            MostrarFragment(1);
        }
        System.out.println("Salir del ONpresed");


    }

}
