package com.example.tusher.cityppolish;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class report extends AppCompatActivity {
    Button btn, upBtn, selBtn;
    EditText descrip;
    ImageView imageView;
    private final int IMG_REQ = 1;
    private LocationManager locationManager;
    private LocationListener locationListener;
    public double lat;
    public double lon;


    static final int Cam_request = 1;
    private Bitmap bitmap, bmap, bitmap1;
    private String URL = "https://citypolish.000webhostapp.com/upload.php"; // server url

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        btn = (Button) findViewById(R.id.btnCamera);
        imageView = (ImageView) findViewById(R.id.imageView);
        upBtn = (Button) findViewById(R.id.btnUpload);
        descrip = (EditText) findViewById(R.id.descript);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                lat = location.getLatitude();
                lon = location.getLongitude();

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.INTERNET}, 10);

            }
        } else {
            confiqureButton();

        }


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File file = getFile();
                cameraintent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                startActivityForResult(cameraintent, Cam_request);

            }
        });

        upBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bitmap1 = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

                uploadImg();

            }
        });


    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 10:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    confiqureButton();


        }
    }

    private void confiqureButton() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0, locationListener);
            return;
        }
        else {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0, locationListener);
        }



    };


    private File getFile(){
        File folder= new File("sdcard/cityPolishCam");
        if(!folder.exists()){
            folder.mkdir();
        }
        File image_file =new File(folder, "cam_image.jpg");


        return image_file;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        File imgF= new File("sdcard/cityPolishCam/cam_image.jpg");
        bmap= BitmapFactory.decodeFile(imgF.getAbsolutePath());
        imageView.setImageBitmap(bmap);
        if(requestCode==IMG_REQ && resultCode==RESULT_OK && data!=null){
            Uri p=data.getData();
            try {
                bitmap=MediaStore.Images.Media.getBitmap(getContentResolver(),p);
                imageView.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void uploadImg(){
        confiqureButton();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            String Response= jsonObject.getString("response");
                            Toast.makeText(report.this, Response, Toast.LENGTH_LONG).show();

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
                Map<String,String>params=new HashMap<>();
                params.put("name", descrip.getText().toString().trim());
                params.put("image",imgToString(bitmap1));
                params.put("lat", Double.toString(lat));
                params.put("lon", Double.toString(lon));
                return params;
            }
        };
        MySingleton.getInstance(report.this).addToRequestQueue(stringRequest);

    }

    //@RequiresApi(api = Build.VERSION_CODES.FROYO)
    private String imgToString(Bitmap bitmap1){
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap1=((BitmapDrawable)imageView.getDrawable()).getBitmap();
        bitmap1.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imgBytes =byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgBytes, Base64.DEFAULT);

    }

}
