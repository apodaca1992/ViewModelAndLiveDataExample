package com.example.adrian.viewmodelandlivedataexample;

import android.Manifest;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adrian.viewmodelandlivedataexample.clases.MyViewModelFactory;
import com.example.adrian.viewmodelandlivedataexample.clases.NotificacionPopUp;
import com.example.adrian.viewmodelandlivedataexample.model.NameViewModel;
import com.example.adrian.viewmodelandlivedataexample.utilerias.Utilerias;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private static final int ACTION_RESULT_AGREGAR_NUEVA_AFILIACION = 3;

    private NameViewModel mModel;
    private RelativeLayout rlNoAutorizado;
    private TextView txtCodigoActivacion;
    private ProgressBar progressBarSplash;

    private static final int PERMISSION_ALL = 1;
    public static final String[] PERMISSIONS = {Manifest.permission.READ_PHONE_STATE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rlNoAutorizado = findViewById(R.id.RelativeNoAutorizado);
        txtCodigoActivacion = findViewById(R.id.txtCodigoActivacion);
        progressBarSplash = findViewById(R.id.progressBarSplash);

        //mNameTextView = findViewById(R.id.txtNombre);
        progressBarSplash.setVisibility(View.VISIBLE);
        rlNoAutorizado.setVisibility(View.GONE);

        /*if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }*/

        /*
        HeroesViewModel model = ViewModelProviders.of(this).get(HeroesViewModel.class);

        model.getHeroes().observe(this, new Observer<List<Hero>>() {
            @Override
            public void onChanged(@Nullable List<Hero> heroList) {
                adapter = new HeroesAdapter(MainActivity.this, heroList);
                recyclerView.setAdapter(adapter);
            }
        });
         */

        mModel = ViewModelProviders.of(this, new MyViewModelFactory(this)).get(NameViewModel.class);
        mModel.getResult().observe(this, new Observer<JSONObject>() {
            @Override
            public void onChanged(final JSONObject prResult) {
                try{
                    if (!prResult.getJSONObject("meta").getBoolean("isValid")) {
                        progressBarSplash.setVisibility(View.GONE);
                        Intent oIntent = new Intent(getApplicationContext(), NotificacionPopUp.class);
                        oIntent.putExtra("tipo_notificacion", NotificacionPopUp.ERROR_CODE);
                        oIntent.putExtra("mensaje",prResult.getJSONObject("meta").getString("message"));
                        startActivity(oIntent);
                    } else {
                        progressBarSplash.setVisibility(View.GONE);
                        JSONObject resultado = prResult.getJSONObject("data").getJSONObject("datos");
                        if(resultado.getString("autorizado").equals("S")){
                            /*Intent intent = new Intent(SplashScreenActivity.this,MainActivity.class);
                            startActivity(intent);
                            finish();*/
                        }else{
                            rlNoAutorizado.setVisibility(View.VISIBLE);
                            txtCodigoActivacion.setText("Codigo Activacion: \n" + resultado.getString("codigo_activacion"));
                        }
                    }
                }catch (JSONException ex){
                    progressBarSplash.setVisibility(View.GONE);
                    Intent oIntent = new Intent(getApplicationContext(), NotificacionPopUp.class);
                    oIntent.putExtra("tipo_notificacion", NotificacionPopUp.ERROR_CODE);
                    oIntent.putExtra("mensaje", ex.getMessage());
                    startActivity(oIntent);
                }
            }
        });

        if(!Utilerias.hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_agregar_paciente:
                Intent iAgregarNuevaAfiliacion = new Intent(MainActivity.this, AgregarNuevaAfiliacion.class);
                startActivityForResult(iAgregarNuevaAfiliacion,ACTION_RESULT_AGREGAR_NUEVA_AFILIACION);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                //finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_ALL:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(),"SE REQUIEREN LOS PERMISOS PARA ACCEDER A LA INFORMACION DEL DISPOSITIVO.",Toast.LENGTH_LONG).show();
                    if(!Utilerias.hasPermissions(this, PERMISSIONS)){
                        ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
                    }
                }else{
                    if (Utilerias.isOnline(getApplicationContext())) {
                        mModel.verificarDispositivo();
                    } else {
                        progressBarSplash.setVisibility(View.GONE);
                        Intent oIntent = new Intent(getApplicationContext(), NotificacionPopUp.class);
                        oIntent.putExtra("tipo_notificacion", NotificacionPopUp.ERROR_CODE);
                        oIntent.putExtra("mensaje", getResources().getString(R.string.strNoCuentaConInternet));
                        startActivity(oIntent);}
                }
                break;
            default:
                break;
        }
    }
}
