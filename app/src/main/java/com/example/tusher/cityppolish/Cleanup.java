package com.example.tusher.cityppolish;


import android.content.Intent;
import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.graphics.drawable.BitmapDrawable;
        import android.media.Image;
        import android.net.Uri;
        import android.provider.MediaStore;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.support.v7.widget.RecyclerView;
        import android.util.Base64;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.Button;
        import android.widget.ImageView;
        import android.widget.RadioButton;
        import android.widget.RadioGroup;
        import android.widget.Spinner;
        import android.widget.Toast;

        import com.android.volley.AuthFailureError;
        import com.android.volley.Request;
        import com.android.volley.Response;
        import com.android.volley.VolleyError;
        import com.android.volley.toolbox.StringRequest;
        import android.widget.AdapterView.OnItemSelectedListener;


        import org.json.JSONException;
        import org.json.JSONObject;

        import java.io.ByteArrayOutputStream;
        import java.io.File;
        import java.io.IOException;
        import java.io.StringReader;
        import java.util.ArrayList;
        import java.util.HashMap;
        import java.util.List;
        import java.util.Map;

public class Cleanup extends AppCompatActivity implements OnItemSelectedListener{
    RadioGroup rg;

    Button upbtn, picbtn;
    ImageView imgView;
    private Bitmap bmap, bmap1;
    private String URL="https://citypolish.000webhostapp.com/cleanup.php";
    Spinner waste_select;
    String getRBOpt, getspinner;
    static final int Cam_req=1;
    private final int IMG_REQ = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cleanup_layout);
        rg=(RadioGroup)findViewById(R.id.rguser);
        upbtn=(Button) findViewById(R.id.btnUpload);
        picbtn=(Button) findViewById(R.id.btnTakePic);
        imgView=(ImageView) findViewById(R.id.imgView);
        waste_select=(Spinner) findViewById(R.id.spinnersel);


        waste_select.setOnItemSelectedListener(this);
        List<String> cats= new ArrayList<String>();
        cats.add("Polythene");
        cats.add("Banana split");
        cats.add("Plastic bottle");
        cats.add("Wrappers");
        ArrayAdapter<String> dataAdapter=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,cats);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        waste_select.setAdapter(dataAdapter);

        upbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bmap1=((BitmapDrawable)imgView.getDrawable()).getBitmap();
                upImage();
            }
        });
        picbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opencam();
            }

        });





    }

    public void onItemSelected(AdapterView<?> parent, View v, int pos, long id){
        getspinner=parent.getItemAtPosition(pos).toString();
    }




    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //empty
    }
    private void opencam(){
        Intent camIntent= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file=getFile();
        camIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        startActivityForResult(camIntent,Cam_req);
    }

    private File getFile(){
        File folder=new File("sdcard/cityPolishCam");
        if(!folder.exists()){
            folder.mkdir();
        }
        File img_file= new File(folder, "cam_img.jpg");
        return  img_file;
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        File imgF=new File("sdcard/cityPolishCam/cam_img.jpg");
        bmap= BitmapFactory.decodeFile(imgF.getAbsolutePath());
        imgView.setImageBitmap(bmap);
        if(requestCode==IMG_REQ && resultCode==RESULT_OK && data!=null){
            Uri p=data.getData();
            try{
                bmap1=MediaStore.Images.Media.getBitmap(getContentResolver(),p);
                imgView.setImageBitmap(bmap1);
            }catch(IOException e){
                e.printStackTrace();
            }


        }
    }


    private void upImage(){
        int rbID=rg.getCheckedRadioButtonId();
        RadioButton rb=(RadioButton)findViewById(rbID);
        getRBOpt=rb.getText().toString();

        StringRequest stringRequest= new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String Response = jsonObject.getString("response");
                    Toast.makeText(Cleanup.this, Response, Toast.LENGTH_LONG).show();


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
                params.put("worked", getRBOpt);
                params.put("image",imgToString(bmap1));
                params.put("waste_type", getspinner);

                return params;
            }

        };
        MySingleton.getInstance(Cleanup.this).addToRequestQueue(stringRequest);





    }
    private String imgToString(Bitmap bitmap1){
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap1=((BitmapDrawable)imgView.getDrawable()).getBitmap();
        bitmap1.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imgBytes =byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgBytes, Base64.DEFAULT);

    }






}
