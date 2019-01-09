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
import com.example.adrian.viewmodelandlivedataexample.R;
import com.example.adrian.viewmodelandlivedataexample.utilerias.Utilerias;
import com.example.adrian.viewmodelandlivedataexample.volley.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NameViewModel extends ViewModel {
    private String TAG = NameViewModel.class.getSimpleName();
    //private MutableLiveData<JSONObject> oResult;
    private MutableLiveData<JSONObject> oResult = new MutableLiveData<>();
    private Context oContext;

    public NameViewModel(Context prContext){
        oContext = prContext;
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            verificarDispositivo();
        }
    }

    public MutableLiveData<JSONObject> getResult() {
        return oResult;
    }

    public void verificarDispositivo() {
        String REQUEST_TAG = "SPLASHSCREENACTIVITY.VERIFICARDISPOSITIVO";

        if (Utilerias.isOnline(oContext)) {

            final JSONObject localJSONObject = new JSONObject();
            try {//creamos el objeto con toda la informacion del dispositivo
                localJSONObject.put(oContext.getResources().getString(R.string.strImei), Utilerias.getIMEI(oContext.getApplicationContext()));
                localJSONObject.put(oContext.getResources().getString(R.string.strNumeroCelular), Utilerias.getNumberCelular(oContext.getApplicationContext()));
                localJSONObject.put(oContext.getResources().getString(R.string.strNumeroSerie), Utilerias.getNumeroSerie());
                localJSONObject.put(oContext.getResources().getString(R.string.strMarca), Utilerias.getManufacturer());
                localJSONObject.put(oContext.getResources().getString(R.string.strModelo), Utilerias.getModel());
                localJSONObject.put(oContext.getResources().getString(R.string.strMacWlan), Utilerias.getMacWlan());
                localJSONObject.put(oContext.getResources().getString(R.string.strSistemaOperativo), "ANDROID");
                localJSONObject.put(oContext.getResources().getString(R.string.strVersionSistemaOperativo), Utilerias.getVersionSdkStr());

                StringRequest stringRequest = new StringRequest(Request.Method.POST, oContext.getResources().getString(R.string.strUrlRegistrarDispositivo),
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
                        Map<String, String> params = new HashMap<>();
                        try {
                            params.put(oContext.getResources().getString(R.string.strImei), localJSONObject.getString(oContext.getResources().getString(R.string.strImei)));
                            params.put(oContext.getResources().getString(R.string.strToken), "token");
                            params.put(oContext.getResources().getString(R.string.strNumeroCelular), localJSONObject.getString(oContext.getResources().getString(R.string.strNumeroCelular)));
                            params.put(oContext.getResources().getString(R.string.strNumeroSerie), localJSONObject.getString(oContext.getResources().getString(R.string.strNumeroSerie)));
                            params.put(oContext.getResources().getString(R.string.strMarca), localJSONObject.getString(oContext.getResources().getString(R.string.strMarca)));
                            params.put(oContext.getResources().getString(R.string.strModelo), localJSONObject.getString(oContext.getResources().getString(R.string.strModelo)));
                            params.put(oContext.getResources().getString(R.string.strMacWlan), localJSONObject.getString(oContext.getResources().getString(R.string.strMacWlan)));
                            params.put(oContext.getResources().getString(R.string.strSistemaOperativo), localJSONObject.getString(oContext.getResources().getString(R.string.strSistemaOperativo)));
                            params.put(oContext.getResources().getString(R.string.strVersionSistemaOperativo), localJSONObject.getString(oContext.getResources().getString(R.string.strVersionSistemaOperativo)));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        return params;
                    }
                };
                // Adding JsonObject request to request queue
                VolleySingleton.getInstance(oContext.getApplicationContext()).addToRequestQueue(stringRequest, REQUEST_TAG);
            } catch (JSONException localJSONException) {
                Log.i("error", localJSONException.getMessage());
            }
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
/*public class HeroesViewModel extends ViewModel {

    //this is the data that we will fetch asynchronously
    private MutableLiveData<List<Hero>> heroList;

    //we will call this method to get the data
    public LiveData<List<Hero>> getHeroes() {
        //if the list is null
        if (heroList == null) {
            heroList = new MutableLiveData<List<Hero>>();
            //we will load it asynchronously from server in this method
            loadHeroes();
        }

        //finally we will return the list
        return heroList;
    }


    //This method is using Retrofit to get the JSON data from URL
    private void loadHeroes() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Api api = retrofit.create(Api.class);
        Call<List<Hero>> call = api.getHeroes();


        call.enqueue(new Callback<List<Hero>>() {
            @Override
            public void onResponse(Call<List<Hero>> call, Response<List<Hero>> response) {

                //finally we are setting the list to our MutableLiveData
                heroList.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Hero>> call, Throwable t) {

            }
        });
    }
}
*/