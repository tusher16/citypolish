package com.example.tusher.cityppolish;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
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

public class FeedDetails extends AppCompatActivity {
    ImageView imgView;
    TextView txname, txloc, txtime;
    Button btnclick;
    String timing;
    String URL="https://citypolish.000webhostapp.com/time.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed_details_layout);
        imgView=(ImageView) findViewById(R.id.waste_image);
        txname=(TextView) findViewById(R.id.nametxt);
        txloc=(TextView) findViewById(R.id.locationtxt);
        txtime=(TextView) findViewById(R.id.timetxt);
        btnclick=(Button) findViewById(R.id.buttona);
        byte[] bc=getIntent().getExtras().getByteArray("image");
        Bitmap bmp= BitmapFactory.decodeByteArray(bc, 0, bc.length);
        BitmapDrawable newbm= new BitmapDrawable(bmp);
        imgView.setImageBitmap(bmp);
        txname.setText("Name: "+ getIntent().getStringExtra("name"));
        txloc.setText("Location: "+ getIntent().getStringExtra("location"));
        txtime.setText("Posted at: "+ getIntent().getStringExtra("time"));
        timing=getIntent().getStringExtra("time");
        btnclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uploadtime();

                Intent in= new Intent(FeedDetails.this, Cleanup.class);




                startActivity(in);

            }
        });


    }
    private void uploadtime(){
        StringRequest stringRequest= new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String Response = jsonObject.getString("response");
                    Toast.makeText(FeedDetails.this, Response, Toast.LENGTH_LONG).show();


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            //@RequiresApi(api = Build.VERSION_CODES.FROYO)
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>params=new HashMap<>();
                params.put("Time", timing);


                return params;
            }

        };
        MySingleton.getInstance(FeedDetails.this).addToRequestQueue(stringRequest);




    }
}
