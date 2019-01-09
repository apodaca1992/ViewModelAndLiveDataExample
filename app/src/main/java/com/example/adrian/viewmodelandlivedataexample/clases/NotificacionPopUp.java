package com.example.adrian.viewmodelandlivedataexample.clases;


import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.adrian.viewmodelandlivedataexample.R;


/**
 * Created by adrian on 06/05/2018.
 */

public class NotificacionPopUp extends AppCompatActivity{
    public static final int ERROR_CODE = 0;
    public static final int SUCCESS_CODE = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notificacion_pop_up);

        if(getSupportActionBar() != null)
            getSupportActionBar().hide();

        Button btnAceptar = findViewById(R.id.btnAceptar);
        TextView txtMensaje = findViewById(R.id.txtMensaje);
        ImageView imgImageStatus = findViewById(R.id.imgStatus);

        Bundle parametros = this.getIntent().getExtras();
        if(parametros != null){
            Integer tipo_notificacion = parametros.getInt("tipo_notificacion");
            String mensaje = parametros.getString("mensaje");
            txtMensaje.setText(mensaje);

            switch (tipo_notificacion){
                case ERROR_CODE:
                    try{
                        imgImageStatus.setImageDrawable(getResources().getDrawable(R.drawable.ic_error170dp));
                    }catch (Exception e){
                        Log.e("NotificacionPopUp","error al cargar la imagen");
                    }
                    break;
                case SUCCESS_CODE:
                    try{
                        imgImageStatus.setImageDrawable(getResources().getDrawable(R.drawable.ic_success170dp));
                    }catch (Exception e){
                        Log.e("NotificacionPopUp","error al cargar la imagen");
                    }
                    break;
            }
        }

        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        DisplayMetrics oDisplayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(oDisplayMetrics);

        int width = oDisplayMetrics.widthPixels;
        int height = oDisplayMetrics.heightPixels;

        getWindow().setLayout((int)(width*.8),(int)(height*.7));

    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        DisplayMetrics oDisplayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(oDisplayMetrics);

        int width = oDisplayMetrics.widthPixels;
        int height = oDisplayMetrics.heightPixels;

        getWindow().setLayout((int)(width*.8),(int)(height*.7));

    }
}
