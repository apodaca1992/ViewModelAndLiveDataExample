package com.example.adrian.viewmodelandlivedataexample.model;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.adrian.viewmodelandlivedataexample.MainActivity;
import com.example.adrian.viewmodelandlivedataexample.R;
import com.example.adrian.viewmodelandlivedataexample.utilerias.Utilerias;
import com.example.adrian.viewmodelandlivedataexample.volley.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AgregarNuevaAfiliacionModel extends ViewModel {
    private String TAG = NameViewModel.class.getSimpleName();
    //private MutableLiveData<JSONObject> oResult;
    private MutableLiveData<JSONObject> oResult = new MutableLiveData<>();
    private Context oContext;

    public AgregarNuevaAfiliacionModel(Context prContext){
        oContext = prContext;
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            obtenerDatosSpinnerAgregarAfiliacion();
        }else{
            if(Utilerias.hasPermissions(prContext, MainActivity.PERMISSIONS)){
                obtenerDatosSpinnerAgregarAfiliacion();
            }
        }
    }

    public MutableLiveData<JSONObject> getResult() {
        return oResult;
    }

    public void obtenerDatosSpinnerAgregarAfiliacion() {
        String REQUEST_TAG = "SPLASHSCREENACTIVITY.VERIFICARDISPOSITIVO";

        if (Utilerias.isOnline(oContext)) {


                StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://applicationpas2.000webhostapp.com/Aplicacion1/ApiAndroid.php",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject object = new JSONObject(response);
                                    oResult.postValue(object);
                                } catch (JSONException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                try {
                                    JSONObject meta = new JSONObject();
                                    meta.put("isValid", false);
                                    meta.put("message", error.getMessage());
                                    JSONObject object = new JSONObject();
                                    object.put("meta", meta);
                                    oResult.postValue(object);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String,String> params = new HashMap<>();
                        params.put(oContext.getResources().getString(R.string.strIdentificadorDispositivo),Utilerias.getIMEI(oContext.getApplicationContext()));
                        params.put(oContext.getResources().getString(R.string.strControlApi), oContext.getResources().getString(R.string.strUrlLlenarDatosSpinner));
                        return params;
                    }
                };
                // Adding JsonObject request to request queue
                VolleySingleton.getInstance(oContext.getApplicationContext()).addToRequestQueue(stringRequest, REQUEST_TAG);

        }else{
            try {
                JSONObject meta = new JSONObject();
                meta.put("isValid", false);
                meta.put("message", oContext.getResources().getString(R.string.strNoCuentaConInternet));
                JSONObject object = new JSONObject();
                object.put("meta", meta);
                oResult.postValue(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.d(TAG, "on cleared called");
    }
}