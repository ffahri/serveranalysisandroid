package com.webischia.serveranalysis;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void girisYap(View view)
    {
        final EditText username = (EditText) findViewById(R.id.username);
        final EditText password = (EditText) findViewById(R.id.password);
        final TextView durum = (TextView) findViewById(R.id.durum);

        try {
            final RequestQueue queue = Volley.newRequestQueue(this);  // this = context

            String url = "http://10.0.2.2:8080/oauth/token";
            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // response
                            Log.d("Response", response);

                            try {
                                Intent i;
                                JSONObject jsonObj = new JSONObject(response);

                                // Getting JSON Array node
//                                JSONArray token = jsonObj.getJSONArray("token");

                                String jti = jsonObj.getString("access_token");
                                durum.setText("BAŞARILI\nJTI = " + jti);


                                Toast.makeText(MainActivity.this, "Başarıyla giriş yaptınız", Toast.LENGTH_SHORT).show();
                                i = new Intent(MainActivity.this,GrafikTest.class);
                                i.putExtra("token",jti);
                                startActivity(i);
                                finish();
                            }
                            catch (Exception e)
                            {

                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // error
                            Log.d("Error.Response","error");
                            durum.setText("HATA = " + error.networkResponse.statusCode);

                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("grant_type", "password");
                    params.put("username", username.getText().toString());
                    params.put("password", password.getText().toString());

                    return params;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Authorization", "Basic U3lzdGVtQW5hbHlzaXM6WFk3a216b056bDEwMA==");

                    return params;
                }
            };
            queue.add(postRequest);
        }
        catch(Exception e)
        {
        }
    }
}
