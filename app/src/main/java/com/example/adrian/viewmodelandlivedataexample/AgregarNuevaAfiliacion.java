package com.example.adrian.viewmodelandlivedataexample;

import android.app.Activity;
import android.app.ProgressDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.adrian.viewmodelandlivedataexample.clases.MyViewModelFactory;
import com.example.adrian.viewmodelandlivedataexample.clases.NotificacionPopUp;
import com.example.adrian.viewmodelandlivedataexample.model.AgregarNuevaAfiliacionModel;
import com.example.adrian.viewmodelandlivedataexample.volley.VolleySingleton;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.adrian.viewmodelandlivedataexample.utilerias.Utilerias;

/**
 * Created by adrian on 20/09/2016.
 */
public class AgregarNuevaAfiliacion extends AppCompatActivity {
    public static final String TAG = "AgregarNuevaAfiliacion";
    private String REQUEST_TAG_OBTENER_DATOS_SPINNERS = "AGREGARNUEVAAFILIACION.OBTENERDATOSSPINNERAGREGARAFILIACION";

    private static final int ACTIVITY_RESULT_LISTADO_PACIENTE_CONSULTA = 1;
    private static final int ACTIVITY_RESULT_AGREGAR_SERVICIOS_PACIENTE = 2;

    //UI References
    private EditText txtApellidoPaterno;
    private EditText txtApellidoMaterno;
    private EditText txtNombre;
    private EditText txtFechaDeNacimiento;
    private Spinner spnSexo;
    private Spinner spnMunicipio;
    private Spinner spnOcupacion;
    private Spinner spnGradoDeEstudio;
    private EditText txtSeccion;
    private EditText txtTelefonoCasa;
    private EditText txtTelefonoMovil;
    private EditText txtCorreo;
    private Button btnConsultarPaciente;
    private Button btnAgregarPaciente;
    private Button btnAltaPaciente;
    private LinearLayout llAltaPaciente;
    private LinearLayout llAgregarPaciente;

    //List references
    private List<JSONObject> lstMunicipios = new ArrayList<>();
    private List<JSONObject> lstSexo  = new ArrayList<>();
    private List<JSONObject> lstOcupaciones = new ArrayList<>();
    private List<JSONObject> lstGradosDeEstudios = new ArrayList<>();

    //Adapter references
    private AdapterMunicipios oAdapterMunicipios;
    private AdapterSexo oAdapterSexo;
    private AdapterOcupaciones oAdapterOcupaciones;
    private AdapterGradosDeEstudios oAdapterGradosDeEstudios;

    private ProgressDialog pdDescargaDatosSpinner;

    private Gson gson;

    private static DialogFragment oDiaglogFragment = null;

    private AgregarNuevaAfiliacionModel mModel;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("nombre", txtNombre.getText().toString());
        outState.putString("paterno", txtApellidoPaterno.getText().toString());
        outState.putString("materno", txtApellidoMaterno.getText().toString());
        outState.putString("fechaNacimiento", txtFechaDeNacimiento.getText().toString());
        outState.putInt("spinner_sexo", spnSexo.getSelectedItemPosition());
        outState.putInt("spinner_municipio", spnMunicipio.getSelectedItemPosition());
        outState.putInt("spinner_ocupacion", spnOcupacion.getSelectedItemPosition());
        outState.putInt("spinner_gradoEstudios", spnGradoDeEstudio.getSelectedItemPosition());
        outState.putString("seccion", txtSeccion.getText().toString());
        outState.putString("telefonoCasa", txtTelefonoCasa.getText().toString());
        outState.putString("telefonoMovil", txtTelefonoMovil.getText().toString());
        outState.putString("correo", txtCorreo.getText().toString());
        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onRestoreInstanceState(final Bundle savedInstanceState) {
        // Check whether we're recreating a previously destroyed instance
        if (savedInstanceState != null) {
            txtNombre.post(new Runnable() {
                @Override
                public void run() {
                    txtNombre.setText(savedInstanceState.getString("nombre"));
                }
            });
            txtApellidoPaterno.post(new Runnable() {
                @Override
                public void run() {
                    txtApellidoPaterno.setText(savedInstanceState.getString("paterno"));
                }
            });
            txtApellidoMaterno.post(new Runnable() {
                @Override
                public void run() {
                    txtApellidoMaterno.setText(savedInstanceState.getString("materno"));
                }
            });
            txtFechaDeNacimiento.post(new Runnable() {
                @Override
                public void run() {
                    txtFechaDeNacimiento.setText(savedInstanceState.getString("fechaNacimiento"));
                }
            });

            spnSexo.post(new Runnable() {
                @Override
                public void run() {
                    spnSexo.setSelection(savedInstanceState.getInt("spinner_sexo"),true);
                }
            });

            spnMunicipio.post(new Runnable() {
                @Override
                public void run() {
                    spnMunicipio.setSelection(savedInstanceState.getInt("spinner_municipio"),true);
                }
            });

            spnOcupacion.post(new Runnable() {
                @Override
                public void run() {
                    spnOcupacion.setSelection(savedInstanceState.getInt("spinner_ocupacion"),true);
                }
            });

            spnGradoDeEstudio.post(new Runnable() {
                @Override
                public void run() {
                    spnGradoDeEstudio.setSelection(savedInstanceState.getInt("spinner_gradoEstudios"),true);
                }
            });

            txtSeccion.post(new Runnable() {
                @Override
                public void run() {
                    txtSeccion.setText(savedInstanceState.getString("seccion"));
                }
            });

            txtTelefonoCasa.post(new Runnable() {
                @Override
                public void run() {
                    txtTelefonoCasa.setText(savedInstanceState.getString("telefonoCasa"));
                }
            });

            txtTelefonoMovil.post(new Runnable() {
                @Override
                public void run() {
                    txtTelefonoMovil.setText(savedInstanceState.getString("telefonoMovil"));
                }
            });

            txtCorreo.post(new Runnable() {
                @Override
                public void run() {
                    txtCorreo.setText(savedInstanceState.getString("correo"));
                }
            });
        } else {
            // Probably initialize members with default values for a new instance
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agregar_nueva_afiliacion);

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        gson = new Gson();

        pdDescargaDatosSpinner = new ProgressDialog(AgregarNuevaAfiliacion.this);
        pdDescargaDatosSpinner.setMessage(AgregarNuevaAfiliacion.this.getString(R.string.strBusquedaDePaciente));
        pdDescargaDatosSpinner.setCancelable(false);
        pdDescargaDatosSpinner.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pdDescargaDatosSpinner.setIndeterminateDrawable(getResources().getDrawable(R.drawable.custom_progress_dialog));
        pdDescargaDatosSpinner.show();


        mModel = ViewModelProviders.of(this, new MyViewModelFactory(this)).get(AgregarNuevaAfiliacionModel.class);
        mModel.getResult().observe(this, new Observer<JSONObject>() {
            @Override
            public void onChanged(final JSONObject oResultado) {
                if(pdDescargaDatosSpinner.isShowing())
                    pdDescargaDatosSpinner.dismiss();

                try{
                    if (!oResultado.getJSONObject("meta").getBoolean("isValid")) {
                                /*new CustomToast(getApplicationContext(), AgregarNuevaAfiliacion.this,
                                        resultado.getMeta().getMessage(), CustomToast.FAIL).show();*/
                        btnConsultarPaciente.setEnabled(false);
                        btnAgregarPaciente.setEnabled(false);
                        btnAltaPaciente.setEnabled(false);
                    }
                    else {
                        lstMunicipios = recorrerArregloMunicipios(oResultado.getJSONObject("data").getJSONArray("municipios"));
                        if (oAdapterMunicipios != null) {
                            oAdapterMunicipios.notifyDataSetChanged();
                        }

                        spnMunicipio.post(new Runnable() {
                            @Override
                            public void run() {
                                spnMunicipio.setSelection(0,true);
                            }
                        });

                        lstOcupaciones = recorrerArregloOcupaciones(oResultado.getJSONObject("data").getJSONArray("ocupaciones"));
                        if (oAdapterOcupaciones != null) {
                            oAdapterOcupaciones.notifyDataSetChanged();
                        }
                        spnOcupacion.post(new Runnable() {
                            @Override
                            public void run() {
                                spnOcupacion.setSelection(0,true);
                            }
                        });

                        lstGradosDeEstudios = recorrerArregloGradosEstudios(oResultado.getJSONObject("data").getJSONArray("grados_estudios"));
                        if (oAdapterGradosDeEstudios != null) {
                            oAdapterGradosDeEstudios.notifyDataSetChanged();
                        }
                        spnGradoDeEstudio.post(new Runnable() {
                            @Override
                            public void run() {
                                spnGradoDeEstudio.setSelection(0,true);
                            }
                        });
                        btnConsultarPaciente.setEnabled(true);
                        obtenerSexos();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                catch (JsonSyntaxException e){
                            /*new CustomToast(getApplicationContext(), AgregarNuevaAfiliacion.this,
                                    e.getMessage(), CustomToast.FAIL).show();*/
                    if(pdDescargaDatosSpinner.isShowing())
                        pdDescargaDatosSpinner.dismiss();
                    btnConsultarPaciente.setEnabled(false);
                    btnAgregarPaciente.setEnabled(false);
                    btnAltaPaciente.setEnabled(false);
                }
            }
        });

        //declaramos todos los controles de la IU.
        txtNombre = (EditText) findViewById(R.id.txtNombre);
        txtApellidoPaterno = (EditText) findViewById(R.id.txtApellidoPaterno);
        txtApellidoMaterno = (EditText) findViewById(R.id.txtApellidoMaterno);
        txtFechaDeNacimiento = (EditText) findViewById(R.id.txtFechaDeNacimiento);
        spnMunicipio = (Spinner) findViewById(R.id.spnMunicipio);
        spnSexo = (Spinner) findViewById(R.id.spnSexo);
        spnOcupacion = (Spinner) findViewById(R.id.spnOcupacion);
        spnGradoDeEstudio = (Spinner) findViewById(R.id.spnGradoDeEstudio);
        txtSeccion = (EditText) findViewById(R.id.txtSeccion);
        txtTelefonoCasa = (EditText) findViewById(R.id.txtTelefonoCasa);
        txtTelefonoMovil = (EditText) findViewById(R.id.txtTelefonoMovil);
        txtCorreo = (EditText) findViewById(R.id.txtCorreo);
        btnConsultarPaciente = (Button) findViewById(R.id.btnConsultarPaciente);
        btnAgregarPaciente = (Button) findViewById(R.id.btnAgregarPaciente);
        btnAltaPaciente = (Button) findViewById(R.id.btnAltaPaciente);
        llAltaPaciente = (LinearLayout) findViewById(R.id.llAltaPaciente);
        llAgregarPaciente = (LinearLayout) findViewById(R.id.llAgregarPaciente);

        //agregamos la propiedad para solo aceptar mayusculas a los EditText que lo requieran. asi como delimitar el numero de caracteres que se aceptan en cada campo.
        txtNombre.setFilters(new InputFilter[]{new InputFilter.AllCaps(),new InputFilter.LengthFilter(255)});
        txtApellidoPaterno.setFilters(new InputFilter[]{new InputFilter.AllCaps(),new InputFilter.LengthFilter(255)});
        txtApellidoMaterno.setFilters(new InputFilter[]{new InputFilter.AllCaps(),new InputFilter.LengthFilter(255)});
        txtSeccion.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});
        txtTelefonoCasa.setFilters(new InputFilter[]{new InputFilter.LengthFilter(255)});
        txtTelefonoMovil.setFilters(new InputFilter[]{new InputFilter.LengthFilter(255)});
        txtCorreo.setFilters(new InputFilter[]{new InputFilter.AllCaps(),new InputFilter.LengthFilter(255)});

        //establecer focus a los campos del formulario.
        txtNombre.setNextFocusDownId(R.id.txtApellidoPaterno);
        txtApellidoPaterno.setNextFocusDownId(R.id.txtApellidoMaterno);
        txtApellidoMaterno.setNextFocusDownId(R.id.txtFechaDeNacimiento);
        txtSeccion.setNextFocusDownId(R.id.txtTelefonoCasa);
        txtTelefonoCasa.setNextFocusDownId(R.id.txtTelefonoMovil);
        txtTelefonoMovil.setNextFocusDownId(R.id.txtCorreo);

        txtFechaDeNacimiento.setInputType(InputType.TYPE_NULL);
        txtFechaDeNacimiento.setKeyListener(null);
        txtFechaDeNacimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(oDiaglogFragment == null){
                    if (txtFechaDeNacimiento.getText().toString().length() > 0) {
                        //el campo fechaDeNacimiento contiene valores.
                        String[] date = txtFechaDeNacimiento.getText().toString().split("-");
                        oDiaglogFragment = DatePickerFragment.newInstance(Integer.parseInt(date[0]), Integer.parseInt(date[1]) - 1, Integer.parseInt(date[2]));
                        oDiaglogFragment.show(getSupportFragmentManager(), "Date Picker");
                    } else {
                        final Calendar c = Calendar.getInstance();
                        int year = c.get(Calendar.YEAR);
                        int month = c.get(Calendar.MONTH);
                        int day = c.get(Calendar.DAY_OF_MONTH);

                        oDiaglogFragment = DatePickerFragment.newInstance(year, month, day);
                        oDiaglogFragment.show(getSupportFragmentManager(), "Date Picker");
                    }
                }
            }
        });

        //spinner municipios
        oAdapterMunicipios = new AdapterMunicipios(getApplicationContext(),R.layout.item_selected_generic_spinner,lstMunicipios);
        spnMunicipio.setAdapter(oAdapterMunicipios);

        //spinner sexo
        oAdapterSexo = new AdapterSexo(this,R.layout.item_selected_generic_spinner,lstSexo);
        spnSexo.setAdapter(oAdapterSexo);

        //spinner ocupaciones
        oAdapterOcupaciones = new AdapterOcupaciones(getApplicationContext(),R.layout.item_selected_generic_spinner,lstOcupaciones);
        spnOcupacion.setAdapter(oAdapterOcupaciones);

        //spinner grados de estudios
        oAdapterGradosDeEstudios = new AdapterGradosDeEstudios(getApplicationContext(),R.layout.item_selected_generic_spinner,lstGradosDeEstudios);
        spnGradoDeEstudio.setAdapter(oAdapterGradosDeEstudios);

        btnConsultarPaciente.setEnabled(false);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        inicializeFormulario();
    }

    private void inicializeFormulario(){
        txtNombre.setText("");
        txtApellidoPaterno.setText("");
        txtApellidoMaterno.setText("");
        txtFechaDeNacimiento.setText("");
        txtSeccion.setText("");
        txtTelefonoCasa.setText("");
        txtTelefonoMovil.setText("");
        txtCorreo.setText("");
    }

    public static void inicializeVarDiaglogFragment(){
        oDiaglogFragment = null;
    }

    //funcion para dar de alta, actualizar y buscar paciente
    private void obtenerDatosSpinnerAgregarAfiliacion(String url){
        pdDescargaDatosSpinner = new ProgressDialog(AgregarNuevaAfiliacion.this);
        pdDescargaDatosSpinner.setMessage(AgregarNuevaAfiliacion.this.getString(R.string.strBusquedaDePaciente));
        pdDescargaDatosSpinner.setCancelable(false);
        pdDescargaDatosSpinner.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pdDescargaDatosSpinner.setIndeterminateDrawable(getResources().getDrawable(R.drawable.custom_progress_dialog));
        pdDescargaDatosSpinner.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(pdDescargaDatosSpinner.isShowing())
                            pdDescargaDatosSpinner.dismiss();

                        try{
                            //DatosSpinner resultado = gson.fromJson(response, DatosSpinner.class);

                            JSONObject oResultado = new JSONObject(response);


                            if (!oResultado.getJSONObject("meta").getBoolean("isValid")) {
                                /*new CustomToast(getApplicationContext(), AgregarNuevaAfiliacion.this,
                                        resultado.getMeta().getMessage(), CustomToast.FAIL).show();*/
                                btnConsultarPaciente.setEnabled(false);
                                btnAgregarPaciente.setEnabled(false);
                                btnAltaPaciente.setEnabled(false);
                            }
                            else {
                                lstMunicipios = recorrerArregloMunicipios(oResultado.getJSONObject("data").getJSONArray("municipios"));
                                if (oAdapterMunicipios != null) {
                                    oAdapterMunicipios.notifyDataSetChanged();
                                }
                                spnMunicipio.setSelection(0);

                                lstOcupaciones = recorrerArregloOcupaciones(oResultado.getJSONObject("data").getJSONArray("ocupaciones"));
                                if (oAdapterOcupaciones != null) {
                                    oAdapterOcupaciones.notifyDataSetChanged();
                                }
                                spnOcupacion.setSelection(0);

                                lstGradosDeEstudios = recorrerArregloGradosEstudios(oResultado.getJSONObject("data").getJSONArray("grados_estudios"));
                                if (oAdapterGradosDeEstudios != null) {
                                    oAdapterGradosDeEstudios.notifyDataSetChanged();
                                }
                                spnGradoDeEstudio.setSelection(0);
                                btnConsultarPaciente.setEnabled(true);
                                obtenerSexos();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        catch (JsonSyntaxException e){
                            /*new CustomToast(getApplicationContext(), AgregarNuevaAfiliacion.this,
                                    e.getMessage(), CustomToast.FAIL).show();*/
                            if(pdDescargaDatosSpinner.isShowing())
                                pdDescargaDatosSpinner.dismiss();
                            btnConsultarPaciente.setEnabled(false);
                            btnAgregarPaciente.setEnabled(false);
                            btnAltaPaciente.setEnabled(false);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //VolleyLog.d(TAG, "Error: " + error.getMessage());
                        /*new CustomToast(getApplicationContext(), AgregarNuevaAfiliacion.this,
                                Utilerias.errorVolley(error), CustomToast.FAIL).show();*/
                        if(pdDescargaDatosSpinner.isShowing())
                            pdDescargaDatosSpinner.dismiss();
                        btnConsultarPaciente.setEnabled(false);
                        btnAgregarPaciente.setEnabled(false);
                        btnAltaPaciente.setEnabled(false);
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put(getResources().getString(R.string.strIdentificadorDispositivo),Utilerias.getIMEI(getApplicationContext()));
                params.put(getResources().getString(R.string.strControlApi), getResources().getString(R.string.strUrlLlenarDatosSpinner));
                return params;
            }
        };
        // Adding JsonObject request to request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest,REQUEST_TAG_OBTENER_DATOS_SPINNERS);
    }

    private List<JSONObject> recorrerArregloMunicipios(JSONArray listMunicipios){
        lstMunicipios.clear();

        try {
            JSONObject oInit = new JSONObject();
            oInit.put("id_municipio",0);
            oInit.put("nombre_municipio","MUNICIPIO DONDE NACIÓ");
            oInit.put("cabecera_municipal",null);
            oInit.put("cve",null);
            oInit.put("zona",-1);
            oInit.put("LONGITUD",null);
            oInit.put("LATITUD",null);
            //municipios oInit = new municipios(Integer.parseInt("0"),"MUNICIPIO DONDE NACIÓ",null, null, -1,null,null);
            lstMunicipios.add(oInit);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (int i =0; i < listMunicipios.length();i++ ){
            //item.setNombre_municipio(item.getNombre_municipio().toUpperCase());
            try {
                JSONObject item = listMunicipios.getJSONObject(i);
                String nombre = item.getString("nombre_municipio").toUpperCase();
                item.remove("nombre_municipio");
                item.put("nombre_municipio",nombre);
                lstMunicipios.add(item);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return lstMunicipios;
    }

    private List<JSONObject> recorrerArregloOcupaciones(JSONArray listOcupaciones){
        lstOcupaciones.clear();
        try {
            JSONObject oInit = new JSONObject();
            oInit.put("id_ocupacion",0);
            oInit.put("ocupacion","OCUPACIÓN");
            //ocupaciones oInit = new ocupaciones(Integer.parseInt("0"),"OCUPACIÓN");
            lstOcupaciones.add(oInit);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (int i =0; i < listOcupaciones.length();i++ ){
            try {
                JSONObject item = listOcupaciones.getJSONObject(i);
                lstOcupaciones.add(item);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return lstOcupaciones;
    }

    private List<JSONObject> recorrerArregloGradosEstudios(JSONArray listGradosEstudios){
        lstGradosDeEstudios.clear();

        try {
            JSONObject oInit = new JSONObject();
            oInit.put("id_grado_estudios",0);
            oInit.put("grado_estudios","ULTIMO GRADO DE ESTUDIOS");
            //grados_estudios oInit = new grados_estudios(Integer.parseInt("0"),"ULTIMO GRADO DE ESTUDIOS");
            lstGradosDeEstudios.add(oInit);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (int i =0; i < listGradosEstudios.length();i++ ){
            try {
                JSONObject item = listGradosEstudios.getJSONObject(i);
                lstGradosDeEstudios.add(item);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return lstGradosDeEstudios;
    }

    private void obtenerSexos(){
        lstSexo.clear();

        try {
            JSONObject oInit = new JSONObject();
            oInit.put("id_sexo",0);
            oInit.put("nombre","SELECCIONE EL SEXO");

            JSONObject oHombre = new JSONObject();
            oHombre.put("id_sexo",1);
            oHombre.put("nombre","HOMBRE");

            JSONObject oMujer = new JSONObject();
            oMujer.put("id_sexo",2);
            oMujer.put("nombre","MUJER");

            lstSexo.add(oInit);
            lstSexo.add(oHombre);
            lstSexo.add(oMujer);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (oAdapterSexo != null) {
            oAdapterSexo.notifyDataSetChanged();
        }

        spnSexo.post(new Runnable() {
            @Override
            public void run() {
                spnSexo.setSelection(0,true);
            }
        });
    }

    @Override
    public boolean onPrepareOptionsMenu (Menu menu) {
        //if (isFinalized) {
        //menu.findItem(R.id.action_descargaDeDatos_descargarTodo).setEnabled(false);
        // You can also use something like:
        // menu.findItem(R.id.example_foobar).setEnabled(false);
        //}
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, returnIntent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //clases adapter spinner de municipios,sexo,ocupaciones,gradosDeEstudio.
    private class AdapterMunicipios extends ArrayAdapter<JSONObject> {
        private ArrayList<JSONObject> municipios;

        AdapterMunicipios(Context context, int textViewResourceId,List<JSONObject> prMunicipios) {
            super(context, textViewResourceId, prMunicipios);
            this.municipios = (ArrayList<JSONObject>) prMunicipios;
        }
        @Override
        public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
            View v = convertView;
            final ViewHolderDrop holder;
            //municipios oMunicipioSelected = (municipios) spnMunicipio.getSelectedItem();
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.item_generic_spinner, parent, false);

                holder = new ViewHolderDrop();
                holder.txtDividerHead = (TextView) v.findViewById(R.id.txtDividerHead);

                holder.txtNombre = (TextView) v.findViewById(R.id.txtNombre);
                /*if(oMunicipioSelected.getId_municipio().equals(municipios.get(position).getId_municipio()))
                    holder.txtNombre.setBackgroundColor(getResources().getColor(R.color.SpinnerBackgroundColor));
                else
                    holder.txtNombre.setBackgroundColor(Color.WHITE);*/
                //holder.txtNombre.setPadding(25,5,5,5);
                v.setTag(holder);
            }
            else{
                holder = (ViewHolderDrop) v.getTag();
                /*if(oMunicipioSelected.getId_municipio().equals(municipios.get(position).getId_municipio()))
                    holder.txtNombre.setBackgroundColor(getResources().getColor(R.color.SpinnerBackgroundColor));
                else
                    holder.txtNombre.setBackgroundColor(Color.WHITE);*/
            }

            try {
                if (this.municipios.get(position).getString("nombre_municipio") != null)
                {
                    holder.txtNombre.setText(this.municipios.get(position).getString("nombre_municipio"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return v;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            View v = convertView;
            final ViewHolder holder;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.item_selected_generic_spinner, parent, false);
                holder = new ViewHolder();
                holder.txtNombre = (TextView) v.findViewById(R.id.txtNombre);
                v.setTag(holder);
            }
            else
                holder = (ViewHolder)v.getTag();

            try {
                if (this.municipios.get(position).getString("nombre_municipio") != null)
                {
                    holder.txtNombre.setText(this.municipios.get(position).getString("nombre_municipio"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return v;
        }
    }
    private class AdapterSexo extends ArrayAdapter<JSONObject> {
        private ArrayList<JSONObject> sexos;

        AdapterSexo(Context context, int textViewResourceId,List<JSONObject> prSexos) {
            super(context, textViewResourceId, prSexos);
            this.sexos = (ArrayList<JSONObject>) prSexos;
        }
        @Override
        public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
            View v = convertView;
            final ViewHolderDrop holder;
            //sexo oSexoSelected = (sexo) spnSexo.getSelectedItem();
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.item_generic_spinner, parent, false);

                holder = new ViewHolderDrop();
                holder.txtDividerHead = (TextView) v.findViewById(R.id.txtDividerHead);

                holder.txtNombre = (TextView) v.findViewById(R.id.txtNombre);
                /*if(oSexoSelected.getId_sexo().equals(sexos.get(position).getId_sexo()))
                    //holder.txtNombre.setBackgroundColor(getColor(R.color.SpinnerBackgroundColor));
                    //holder.txtNombre.setBackgroundColor(getApplicationContext().getColor(R.color.SpinnerBackgroundColor));
                    holder.txtNombre.setBackgroundColor(getResources().getColor(R.color.SpinnerBackgroundColor));
                else
                    holder.txtNombre.setBackgroundColor(Color.WHITE);*/
                //holder.txtNombre.setPadding(25,5,5,5);
                v.setTag(holder);
            }
            else{
                holder = (ViewHolderDrop) v.getTag();
                /*if(oSexoSelected.getId_sexo().equals(sexos.get(position).getId_sexo()))
                    holder.txtNombre.setBackgroundColor(getResources().getColor(R.color.SpinnerBackgroundColor));
                else
                    holder.txtNombre.setBackgroundColor(Color.WHITE);*/
            }

            try {
                if (this.sexos.get(position).getString("nombre") != null)
                {
                    holder.txtNombre.setText(this.sexos.get(position).getString("nombre"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return v;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            View v = convertView;
            final ViewHolder holder;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.item_selected_generic_spinner, parent, false);
                holder = new ViewHolder();
                holder.txtNombre = (TextView) v.findViewById(R.id.txtNombre);
                v.setTag(holder);
            }
            else
                holder = (ViewHolder)v.getTag();

            try {
                if (this.sexos.get(position).getString("nombre") != null)
                {
                    holder.txtNombre.setText(this.sexos.get(position).getString("nombre"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return v;
        }
    }
    private class AdapterOcupaciones extends ArrayAdapter<JSONObject> {
        private ArrayList<JSONObject> ocupaciones;

        AdapterOcupaciones(Context context, int textViewResourceId,List<JSONObject> prOcupaciones) {
            super(context, textViewResourceId, prOcupaciones);
            this.ocupaciones = (ArrayList<JSONObject>) prOcupaciones;
        }
        @Override
        public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
            View v = convertView;
            final ViewHolderDrop holder;
            //ocupaciones oOcupacionSelected = (ocupaciones) spnOcupacion.getSelectedItem();
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.item_generic_spinner, parent, false);

                holder = new ViewHolderDrop();
                holder.txtDividerHead = (TextView) v.findViewById(R.id.txtDividerHead);
                holder.txtNombre = (TextView) v.findViewById(R.id.txtNombre);

                /*if(oOcupacionSelected.getId_ocupacion().equals(ocupaciones.get(position).getId_ocupacion()))
                    holder.txtNombre.setBackgroundColor(getResources().getColor(R.color.SpinnerBackgroundColor));
                else
                    holder.txtNombre.setBackgroundColor(Color.WHITE);*/
                //holder.txtNombre.setPadding(25,5,5,5);
                v.setTag(holder);
            }
            else{
                holder = (ViewHolderDrop) v.getTag();
                /*if(oOcupacionSelected.getId_ocupacion().equals(ocupaciones.get(position).getId_ocupacion()))
                    holder.txtNombre.setBackgroundColor(getResources().getColor(R.color.SpinnerBackgroundColor));
                else
                    holder.txtNombre.setBackgroundColor(Color.WHITE);*/
            }

            try {
                if (this.ocupaciones.get(position).getString("ocupacion") != null)
                {
                    holder.txtNombre.setText(this.ocupaciones.get(position).getString("ocupacion"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return v;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            View v = convertView;
            final ViewHolder holder;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.item_selected_generic_spinner, parent, false);
                holder = new ViewHolder();
                holder.txtNombre = (TextView) v.findViewById(R.id.txtNombre);
                v.setTag(holder);
            }
            else
                holder = (ViewHolder)v.getTag();

            try {
                if (this.ocupaciones.get(position).getString("ocupacion") != null)
                {
                    holder.txtNombre.setText(this.ocupaciones.get(position).getString("ocupacion"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return v;
        }
    }
    private class AdapterGradosDeEstudios extends ArrayAdapter<JSONObject>  {
        private ArrayList<JSONObject> gradosDeEstudios;

        AdapterGradosDeEstudios(Context context, int textViewResourceId,List<JSONObject> prGradosDeEstudios) {
            super(context, textViewResourceId, prGradosDeEstudios);
            this.gradosDeEstudios = (ArrayList<JSONObject>) prGradosDeEstudios;
        }
        @Override
        public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
            View v = convertView;
            final ViewHolderDrop holder;
            //grados_estudios oGradosEstudioSelected = (grados_estudios) spnGradoDeEstudio.getSelectedItem();
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.item_generic_spinner, parent, false);

                holder = new ViewHolderDrop();
                holder.txtDividerHead = (TextView) v.findViewById(R.id.txtDividerHead);
                holder.txtNombre = (TextView) v.findViewById(R.id.txtNombre);

                /*if(oGradosEstudioSelected.getId_grado_estudios().equals(gradosDeEstudios.get(position).getId_grado_estudios()))
                    holder.txtNombre.setBackgroundColor(getResources().getColor(R.color.SpinnerBackgroundColor));
                else
                    holder.txtNombre.setBackgroundColor(Color.WHITE);*/
                //holder.txtNombre.setPadding(25,5,5,5);
                v.setTag(holder);
            }
            else{
                holder = (ViewHolderDrop) v.getTag();
                /*if(oGradosEstudioSelected.getId_grado_estudios().equals(gradosDeEstudios.get(position).getId_grado_estudios()))
                    holder.txtNombre.setBackgroundColor(getResources().getColor(R.color.SpinnerBackgroundColor));
                else
                    holder.txtNombre.setBackgroundColor(Color.WHITE);*/
            }

            try {
                if (this.gradosDeEstudios.get(position).getString("grado_estudios") != null)
                {
                    holder.txtNombre.setText(this.gradosDeEstudios.get(position).getString("grado_estudios"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return v;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            View v = convertView;
            final ViewHolder holder;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.item_selected_generic_spinner, parent, false);
                holder = new ViewHolder();
                holder.txtNombre = (TextView) v.findViewById(R.id.txtNombre);
                //holder.txtNombre.setTextColor(getResources().getColor(R.color.c_pressed_item_list));
                v.setTag(holder);
            }
            else
                holder = (ViewHolder)v.getTag();

            try {
                if (this.gradosDeEstudios.get(position).getString("grado_estudios") != null)
                {
                    holder.txtNombre.setText(this.gradosDeEstudios.get(position).getString("grado_estudios"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return v;
        }
    }

    private class ViewHolder    {
        TextView txtNombre;
    }
    private class ViewHolderDrop {
        TextView txtDividerHead;
        TextView txtNombre;
    }

}

