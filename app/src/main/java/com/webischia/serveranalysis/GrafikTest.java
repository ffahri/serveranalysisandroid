package com.webischia.serveranalysis;

import android.content.Intent;
import android.graphics.Color;
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
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class GrafikTest extends AppCompatActivity {

    LineChart linechart1; //xml den grafik ekranını çıktı aldık
    //  JSONObject jsonObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grafik_test);
//        linechart1.setDragEnabled(true);
//        linechart1.setScaleEnabled(false);

    }
    public void veriCek(View view)
    {
        try {
            final RequestQueue queue = Volley.newRequestQueue(this);  // this = context

            String url = "http://192.168.122.160:9090/api/v1/query?query=node_cpu{mode=\"idle\"}[1m]";
            StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // response
                            Log.d("Response", response);

                            try {
                                ArrayList<Entry> yValues = new ArrayList<>();
                                Intent i;
                                JSONObject jsonObj = new JSONObject(response);
                                JSONObject jsonObj2 = jsonObj.getJSONObject("data");
                                JSONArray jsonArr = jsonObj2.getJSONArray("result");
                                JSONObject jsonArr2 = jsonArr.getJSONObject(0);
                                JSONArray jsonArr3 = jsonArr2.getJSONArray("values");
                                for(int ii = 0 ; ii < 5 ; ii++)
                                {
                                    JSONArray js3 = jsonArr3.getJSONArray(ii);
                                    Log.d("js3", js3.toString());

                                    String axes_x = js3.getString(1);
                                    Date timestamp = new Date(js3.getLong(0)*1000L);
                                    Float x = Float.parseFloat(axes_x);
                                   // x = x/100000000;
                                    yValues.add(new Entry((float)timestamp.getSeconds(),x)); //x 0 y 60 olsun f de float f si

                                }
//                                Date timestamp = new Date(jsonArr3.getLong(0)*1000L);
//                                String axes_x = jsonArr3.getString(1);
//                                Float x = Float.parseFloat(axes_x);
//                                Log.d("X", axes_x);
//                                Log.d("Y",""+timestamp.toGMTString());
                            ///////////////// GRAFIK
                                linechart1 = (LineChart) findViewById(R.id.linechart); //xml den java classına çağırdık

                                //yValues.add(new Entry((float)timestamp.getMinutes(),x)); //x 0 y 60 olsun f de float f si
                                LineDataSet set1 = new LineDataSet(yValues,"CPU KULLANIMI");// sol altta yazan yazı

                                set1.setFillAlpha(110);
                                set1.setColor(Color.RED);// çizgi reng

                                ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                                dataSets.add(set1);// çizginin oluştuğu kısım heralde tam kontrol etmedim

                                LineData data = new LineData(dataSets);
                                linechart1.setData(data); // programa ekliyor



                                        ////////////////// GRAFIK

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
    public void grafikYap(String date,Long a)
    {

    }
}
