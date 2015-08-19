package com.example.maxi.sim;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.TypedArray;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.internal.widget.ActionBarContainer;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private String[] titulos;
    private DrawerLayout NavDrawerLayout;
    private ListView NavList;
    private ArrayList<Item_objct> NavItms;
    private TypedArray NavIcons;
    private NavigationAdapter NavAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private ArrayList<Paciente> Listapaciente;
    private Bundle pacientes;
    @Override
    protected void onCreate (Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Ejemplo de busqueda de pacientes
        //Deberia haber una conexion a la base de datos///////////////////////////////
        Listapaciente = new ArrayList<Paciente>();

        Paciente paciente = new Paciente("Maximiliano","Akike","105",111,111,111,111);
        Paciente paciente2 = new Paciente("Andres","Sanchez","106",111,111,111,111);
        Paciente paciente3 = new Paciente("Andres","Dengra","106",111,111,111,111);

        Listapaciente.add(paciente);
        Listapaciente.add(paciente2);
        Listapaciente.add(paciente3);


        pacientes = new Bundle();
        pacientes.putSerializable("LISTA", Listapaciente);

        //////////////////////////////////////////////////////////////////////////////

        NavDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        //Lista
        NavList = (ListView) findViewById(R.id.lista);

        View header = getLayoutInflater().inflate(R.layout.header,null);

        NavList.addHeaderView(header);

        NavIcons = getResources().obtainTypedArray(R.array.navigation_iconos);

        titulos = getResources().getStringArray(R.array.nav_options);

        NavItms = new ArrayList<Item_objct>();

        NavItms.add(new Item_objct(titulos[0], NavIcons.getResourceId(0, -1)));
        NavItms.add(new Item_objct(titulos[1],NavIcons.getResourceId(1,-1)));
        NavItms.add(new Item_objct(titulos[2],NavIcons.getResourceId(2,-1)));

        NavAdapter = new NavigationAdapter(this,NavItms);
        NavList.setAdapter(NavAdapter);


        //Declaramos el mDrawerToggle y las imgs a utilizar
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                NavDrawerLayout,         /* DrawerLayout object */
                R.string.app_name,  /* "open drawer" description */
                R.string.hello_world  /* "close drawer" description */
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                Log.e("Cerrado completo", "!!");
            }

            /** Called when a drawer has settled in a completely open state. */
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

        switch (position) {
            case 1:
                fragment = new HomeFragment();
                break;
            case 2:
                fragment = new EcgFragment();
                break;
            case 3:
                fragment = new FarmacoFragment();
                break;
            default:
                //si no esta la opcion mostrara un toast y nos mandara a Home
                Toast.makeText(getApplicationContext(),"Opcion "+titulos[position-1]+"no disponible!", Toast.LENGTH_SHORT).show();
                position=1;
                break;

        }

        //Validamos si el fragment no es nulo
        if (fragment != null) {
            fragment.setArguments(pacientes);
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

            // Actualizamos el contenido segun la opcion elegida
            NavList.setItemChecked(position, true);
            NavList.setSelection(position);
            //Cambiamos el titulo en donde decia "
            setTitle(titulos[position-1]);
            //Cerramos el menu deslizable
            NavDrawerLayout.closeDrawer(NavList);
        } else {
            //Si el fragment es nulo mostramos un mensaje de error.
            Log.e("Error  ", "MostrarFragment"+position);
        }
    }
}
