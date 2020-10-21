package com.pt.accidenttrigger;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.nio.charset.CharacterCodingException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final RequestQueue queue = Volley.newRequestQueue(this);




        final Button button = (Button) findViewById(R.id.accident_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                volley_send();
                Log.d("APP", "after send");
                button.setEnabled(false);
            }
        });

        button.setBackgroundColor(getResources().getColor(R.color.colorGreen));

    }

    private void volley_send(){
        RequestQueue queue = Volley.newRequestQueue(this);
        EditText people_text = findViewById(R.id.people_text);
        final String people = people_text.getText().toString();
        CheckBox overturned_box = findViewById(R.id.overturned_box);
        final String overturned = Boolean.toString(overturned_box.isChecked());
        CheckBox airbags_box = findViewById(R.id.airbags_check);
        final String airbags = Boolean.toString(airbags_box.isChecked());
        CheckBox all_seatbelts_box = findViewById(R.id.seatbelts_check);
        final String all_seatbelts = Boolean.toString(all_seatbelts_box.isChecked());
        final Button button = (Button) findViewById(R.id.accident_button);

        button.setBackgroundColor(getResources().getColor(R.color.colorRed));


        StringRequest sr = new StringRequest(Request.Method.POST, "http://11.0.0.1:9001" ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("HttpClient", "success! response: " + response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("HttpClient", "error: " + error.toString());
                    }
                })
        {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("persons",people);
                params.put("overturned",overturned);
                params.put("airbags",airbags);
                params.put("all_seatbelts",all_seatbelts);
                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        queue.add(sr);

        Handler handler=new Handler();
        Runnable r=new Runnable() {
            public void run() {
                button.setBackgroundColor(getResources().getColor(R.color.colorGreen));
            }
        };

        Handler enabler = new Handler();

        Runnable enable = new Runnable() {
            @Override
            public void run() {
                button.setEnabled(true);
            }
        };
        enabler.postDelayed(enable,2*60*1000);
        handler.postDelayed(r, 10000);
    }


}
