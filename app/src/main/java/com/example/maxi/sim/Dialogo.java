package com.example.maxi.sim;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by yamila on 01/10/2015.
 */
public class Dialogo extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            AlertDialog.Builder builder =
                    new AlertDialog.Builder(getActivity());
            String titulo  = getArguments().getString("TITULO");
            String mensaje  = getArguments().getString("MENSAJE");

            builder.setMessage(mensaje)
                    .setTitle(titulo)
                    .setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Log.i("Dialogos", "Confirmacion Aceptada.");
                            dialog.cancel();
                        }
                    })
                    .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Log.i("Dialogos", "Confirmacion Cancelada.");
                            dialog.cancel();
                        }
                    });

            return builder.create();
        }


}
