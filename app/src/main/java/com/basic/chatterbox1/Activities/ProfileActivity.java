package com.basic.chatterbox1.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.basic.chatterbox1.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    public static final int CAMERA_PERMISSION_CODE = 100;
    public static final int CAMERA_REQUEST_CODE = 101;
    public static final int CAMERA_ACTION_CODE = 1;
    public static final int GALLERY_ACTION_CODE = 2;
    public static final int GALLERY_PERMISSION_CODE = 200;
    public static final int GALLERY_REQUEST_CODE = 201;

    EditText et_name,et_phno,et_email;
    int edit=0;
    FloatingActionButton fab;
    TextView tv_name,tv_phno,tv_email;
    LinearLayout ll_details,ll_details_edit;
    CircleImageView iv_image;
    Bitmap image;
    String str_name="";
    String str_phno="";
    String str_email;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        et_name= findViewById(R.id.et_name);
        et_phno= findViewById(R.id.et_phno);
        et_email= findViewById(R.id.et_email);
        iv_image=findViewById(R.id.iv_image);
        ll_details=findViewById(R.id.ll_details);
        ll_details_edit=findViewById(R.id.ll_details_edit);
        tv_name=findViewById(R.id.tv_name);
        tv_phno=findViewById(R.id.tv_phno);
        tv_email=findViewById(R.id.tv_email);

        iv_image.setImageResource(R.drawable.user_image_06);
        user= FirebaseAuth.getInstance().getCurrentUser();

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (edit==0){

                    et_name.setText(tv_name.getText().toString().trim());
                    et_phno.setText(tv_phno.getText().toString().trim());
                    et_email.setText(tv_email.getText().toString().trim());

                    fab.setImageResource(R.drawable.ic_action_tick);

                    ll_details.setVisibility(View.GONE);
                    ll_details_edit.setVisibility(View.VISIBLE);

                    edit=1;
                }
                else if (edit==1){

                    String new_name=et_name.getText().toString().trim();
                    String new_ph=et_phno.getText().toString().trim();
                    String new_email=et_email.getText().toString().trim();

                    String name=tv_name.getText().toString().trim();
                    String ph=tv_phno.getText().toString().trim();
                    String email=tv_email.getText().toString().trim();

                    if (!name.equals(new_name)||!ph.equals(new_ph)||!email.equals(new_email)) {

                        updateDetails(new_name, new_ph, new_email);
                        tv_name.setText(new_name);
                        tv_phno.setText(new_ph);
                        tv_email.setText(new_email);
                    }

                    Snackbar.make(view, "Details Saved", Snackbar.LENGTH_SHORT).show();
                    fab.setImageResource(R.drawable.ic_action_edit);

                    ll_details_edit.setVisibility(View.GONE);
                    ll_details.setVisibility(View.VISIBLE);

                    edit=0;

                }


            }
        });


        iv_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edit == 1) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                    builder.setTitle("Insert Image");
                    builder.setCancelable(true);
                    builder.setMessage("Select a source :");
                    builder.setPositiveButton("Capture", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            askPermission(CAMERA_ACTION_CODE);
                        }
                    });

                    builder.setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            askPermission(GALLERY_ACTION_CODE);
                        }
                    });

                    builder.show();
                }
            }
        });

        Intent intent=getIntent();
        str_name=intent.getStringExtra("name");
        str_phno=intent.getStringExtra("phno");
        str_email=intent.getStringExtra("email");

        if (str_name!=null&&str_phno!=null&&str_email!=null){

            tv_name.setText(str_name);
            tv_phno.setText(str_phno);
            tv_email.setText(str_email);

        }

    }

    private void updateDetails(String name, String ph, String email) {

        String UserId= user.getUid();
        DatabaseReference rootRef= FirebaseDatabase.getInstance().getReference("Users").child(UserId);

        rootRef.child("name").setValue(name);
        rootRef.child("phno").setValue(ph);
        rootRef.child("email").setValue(email);



    }

    private void askPermission(int action_code) {

        switch(action_code){

            case CAMERA_ACTION_CODE:
            {
                if (ContextCompat.checkSelfPermission(ProfileActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(ProfileActivity.this,new String[] {Manifest.permission.CAMERA},CAMERA_PERMISSION_CODE);

                }
                else {
                    openCamera();
                }

                break;
            }

            case GALLERY_ACTION_CODE:
            {
                if (ContextCompat.checkSelfPermission(ProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(ProfileActivity.this,new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, GALLERY_PERMISSION_CODE);

                }
                else {
                    openGallery();
                }

                break;
            }
        }



    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode== CAMERA_PERMISSION_CODE){
            if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                openCamera();

            }
            else {

                Toast.makeText(this, "Camera Permisssion Required", Toast.LENGTH_SHORT).show();
            }
        }
        else if (requestCode==GALLERY_PERMISSION_CODE){
            if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                openGallery();

            }
            else {

                Toast.makeText(this, "Storage Permisssion Required", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openCamera() {

        Intent camera_intent= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera_intent, CAMERA_REQUEST_CODE);
    }

    private void openGallery() {
        Intent gallery_intent=new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(gallery_intent, GALLERY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==CAMERA_REQUEST_CODE && resultCode== RESULT_OK) {

            image=(Bitmap)data.getExtras().get("data");
            iv_image.setImageBitmap(image);
        }
        else if (requestCode==GALLERY_REQUEST_CODE && resultCode== RESULT_OK){

            Uri image_uri= data.getData();
            iv_image.setImageURI(image_uri);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        String str_name=tv_name.getText().toString().trim();
        String str_phno=tv_phno.getText().toString().trim();
        String str_email=tv_email.getText().toString().trim();
        Intent intent = new Intent(ProfileActivity.this,SettingsActivity.class);
        intent.putExtra("name",str_name);
        intent.putExtra("phno",str_phno);
        intent.putExtra("email",str_email);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}