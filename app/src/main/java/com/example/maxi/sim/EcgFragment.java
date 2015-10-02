package com.example.maxi.sim;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import com.example.maxi.sim.ItemPaciente;
public class EcgFragment extends Fragment {
    private ArrayList<ItemPaciente> ListItem = new ArrayList<ItemPaciente>();
    private ArrayList<Paciente> ListaPaciente;
    private  String origen;

    public EcgFragment() {
    }

    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        View rootView = inflater.inflate(R.layout.fragment_ecg,container,false);

        origen = (String) getArguments().getString("ORIGEN");
        ListaPaciente = (ArrayList<Paciente>) getArguments().getSerializable("LISTA");

        ListItem.add(new ItemPaciente(R.drawable.afeccion2, "Maximiliano Akike", 26, 1.78, 90.0, 34809913, "Epoc Reagudizado"));
        ListItem.add(new ItemPaciente(R.drawable.normal3, "Debora Puebla", 26, 1.78, 90.0, 34809913,"Infarto Agudo de Miocardio"));
        ListItem.add(new ItemPaciente(R.drawable.normal3, "Andres Dengra", 26, 1.78, 90.0, 34809913,"Edema Cerebral"));
        ListItem.add(new ItemPaciente(R.drawable.normal3, "Andres Sanchez", 26, 1.78, 90.0, 34809913, "Sistole Auricular"));

        ArrayAdapter<ItemPaciente> adaptador = new pacienteAdapter(rootView.getContext());
        ListView listaPaciente = (ListView) rootView.findViewById(R.id.listMonitoreo);
        listaPaciente.setAdapter(adaptador);

        listaPaciente.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                String msg = ListItem.get(position).getNombreyApellido();
               // Toast.makeText(rootView, msg, Toast.LENGTH_LONG).show();
            }
        });
        return rootView;

    }

    public class pacienteAdapter extends ArrayAdapter<ItemPaciente> {

        public pacienteAdapter(Context rootView){
            super(rootView, R.layout.item_monitoreo_layout, ListItem);

        }

        public View getView(int position, View convertView, ViewGroup parent){
            View itemView = convertView;

            if(itemView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                itemView = inflater.inflate(R.layout.item_monitoreo_layout,null);
            }

            ItemPaciente currentPaciente = ListItem.get(position);

            ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
            imageView.setImageResource(currentPaciente.getImagen());

            TextView nombreText = (TextView)itemView.findViewById(R.id.textView);
            nombreText.setText(currentPaciente.getNombreyApellido());

            TextView diagnosticoText = (TextView)itemView.findViewById(R.id.textView2);

            diagnosticoText.setText(currentPaciente.getDiagnostico());
            return itemView;


        }

    }

}
