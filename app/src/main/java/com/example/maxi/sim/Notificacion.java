package com.example.maxi.sim;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;

import java.util.Objects;

/**
 * Created by yamila on 03/10/2015.
 */
public class Notificacion extends AppCompatActivity {
    private Notification.Builder builder;
    private Context context;

    public Notificacion(Context context) {
        this.context = context;
        builder = new Notification.Builder(context);
    }



    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public Notification getNotificacion(String titulo, String contenido, String resumenContenido, String contenidoReducido) {
        builder
                .setContentTitle(titulo)
                .setContentText(contenidoReducido)
                .setContentInfo("Info")
                .setSmallIcon(R.mipmap.ic_logo_sim)
        ;

        return new Notification.BigTextStyle(builder)
                .bigText(contenido)
                .setBigContentTitle(titulo)
                .setSummaryText(resumenContenido)
                .build();
    }

}
