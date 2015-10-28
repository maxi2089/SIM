package com.example.maxi.sim;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by yamila on 27/10/2015.
 */
public class DialogoExito  extends DialogFragment {


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder =
                new AlertDialog.Builder(getActivity());
        builder.setTitle("Informacion")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setMessage("Guardado Exitoso")
        ;

        return builder.create();
    }

}
