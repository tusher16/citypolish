package com.example.tusher.cityppolish;

/**
 * Created by Tusher on 4/30/2017.
 */

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class citycom extends AppCompatActivity {
    EditText ed;
    Button btn;
    String username;
    RadioGroup comm;

    private String URL="https://citypolish.000webhostapp.com/community.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.city_com_layout);
        ed=(EditText) findViewById(R.id.usrname);
        btn=(Button) findViewById(R.id.butt);
        username= ed.getText().toString();
        comm=(RadioGroup)findViewById(R.id.commsel);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username= ed.getText().toString();
                upuname();
            }
        });


    }
    private void upuname(){
        int rbID=comm.getCheckedRadioButtonId();
        RadioButton rb=(RadioButton)findViewById(rbID);
        final String getRBOpt=rb.getText().toString();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            String Response= jsonObject.getString("response");
                            Toast.makeText(citycom.this, Response, Toast.LENGTH_LONG).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })
        {
            //@RequiresApi(api = Build.VERSION_CODES.FROYO)
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                params.put("user", username);
                params.put("community", getRBOpt);

                return params;
            }
        };
        MySingleton.getInstance(citycom.this).addToRequestQueue(stringRequest);


    }
}
