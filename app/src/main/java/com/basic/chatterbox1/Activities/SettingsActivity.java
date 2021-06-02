package com.basic.chatterbox1.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.basic.chatterbox1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SettingsActivity extends AppCompatActivity {

    String str_name="";
    String str_phno="";
    String str_email;
    TextView tv_name,tv_phno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        tv_name=findViewById(R.id.tv_name);
        tv_phno=findViewById(R.id.tv_phno);

        Intent intent=getIntent();
        str_name=intent.getStringExtra("name");
        str_phno=intent.getStringExtra("phno");
        str_email=intent.getStringExtra("email");

        if (str_name!=null&&str_phno!=null){

            tv_name.setText(str_name);
            tv_phno.setText(str_phno);
        }



        Log.i("IMPORTANT",""+str_name);
        Log.i("IMPORTANT",""+str_phno);
        Log.i("IMPORTANT",""+str_email);

    }


    public void profileOnClick(View view) {

        Intent intent =new Intent(SettingsActivity.this,ProfileActivity.class);
        intent.putExtra("name",str_name);
        intent.putExtra("phno",str_phno);
        intent.putExtra("email",str_email);
        startActivity(intent);
        finish();
    }
}