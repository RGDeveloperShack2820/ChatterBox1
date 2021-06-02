package com.basic.chatterbox1.Activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.basic.chatterbox1.R;

public class SignUpActivity extends AppCompatActivity {

    EditText et_name,et_phno;
    TextView  tv_signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        et_name=findViewById(R.id.et_name);
        et_phno=findViewById(R.id.et_phone);
        tv_signUp=findViewById(R.id.tv_signup);
        tv_signUp.setVisibility(View.GONE);


    }


    public void signUpOnClick(View view) {

        String str_phno=et_phno.getText().toString().trim();
        String str_name=et_name.getText().toString().trim();

        if (str_phno.isEmpty()||str_phno.length()<10){

            Toast.makeText(this, "Please Enter A Valid Phone Number To Continue ", Toast.LENGTH_SHORT).show();
        }
        else{
            Intent intent = new Intent(SignUpActivity.this, VerificationActivity.class);
            intent.putExtra("phno", str_phno);
            intent.putExtra("name",str_name);
            startActivity(intent);
        }
    }
}