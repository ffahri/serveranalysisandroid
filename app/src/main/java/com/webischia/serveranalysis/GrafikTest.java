package com.webischia.serveranalysis;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class GrafikTest extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grafik_test);
    }
    public void veriCek(View view)
    {
        try {
            final RequestQueue queue = Volley.newRequestQueue(this);  // this = context

            String url = "http://10.0.2.2:8080/api/v1/metric/node_memory_MemFree";
            StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // response
                            Log.d("Response", response);

                            try {
                                Intent i;
                                JSONObject jsonObj = new JSONObject(response);
                                JSONObject jsonObj2 = jsonObj.getJSONObject("data");
                                JSONArray jsonArr = jsonObj2.getJSONArray("result");
                                JSONObject jsonArr2 = jsonArr.getJSONObject(0);
                                JSONArray jsonArr3 = jsonArr2.getJSONArray("value");
                                Date timestamp = new Date(jsonArr3.getLong(0)*1000L);
                                String axes_x = jsonArr3.getString(1);
                                Log.d("X", axes_x);
                                Log.d("Y",""+timestamp.toGMTString());





                            }
                            catch (Exception e)
                            {
                                Log.d("hata", e.getMessage());

                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // error
                            Log.d("Error.Response","error");


                        }
                    }
            ) {


                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    String token = getIntent().getExtras().getString("token");
                    if(token != null) {
                        params.put("Authorization", "Basic VXNlcjoxMjM0");

                        return params;
                    }
                    else
                        return null;
                }
            };
            queue.add(postRequest);
        }
        catch(Exception e)
        {
        }

    }
}
