package com.basic.chatterbox1.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.basic.chatterbox1.R;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText et_name,et_phno;
    TextView tv_signUp;
    Button bt_signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        et_name=findViewById(R.id.et_name);
        et_phno=findViewById(R.id.et_phone);
        tv_signUp=findViewById(R.id.tv_signup);
        bt_signUp=findViewById(R.id.bt_signUp);

        et_name.setVisibility(View.GONE);
        bt_signUp.setText("Login..");
        tv_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });


    }


    public void signUpOnClick(View view) {

        String str_phno=et_phno.getText().toString().trim();
        String str_name=et_name.getText().toString().trim();

        if (str_phno.isEmpty()||str_phno.length()<10){

            Toast.makeText(this, "Please Enter A Valid Phone Number To Continue ", Toast.LENGTH_SHORT).show();
        }
        else{
            Intent intent = new Intent(LoginActivity.this, VerificationActivity.class);
            intent.putExtra("phno", str_phno);
            intent.putExtra("name","");
            startActivity(intent);
        }
    }
}