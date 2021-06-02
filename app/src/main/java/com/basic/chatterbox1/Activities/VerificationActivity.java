package com.basic.chatterbox1.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.basic.chatterbox1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class VerificationActivity extends AppCompatActivity {

    EditText et_otp;
    String str_phno,str_name;
    TextView tv_otpMessage;
    FirebaseAuth mAuth;
    String str_codeSent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        tv_otpMessage=findViewById(R.id.tv_otpMessage);

        Intent intent=getIntent();
        str_phno=intent.getStringExtra("phno");
        str_name=intent.getStringExtra("name");
        Log.i("IMPORTANT",str_phno);

        tv_otpMessage.setText("A 6 Digits OTP is Sent at +91 "+str_phno+"\nIt may take few seconds to arrive...\nEnter the otp and press continue to complete the Sign up.");

        et_otp=findViewById(R.id.et_otp);

        mAuth=FirebaseAuth.getInstance();

        sendVerificationCode(str_phno);


    }

    private void sendVerificationCode(String str_phno) {

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+91"+str_phno)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(VerificationActivity.this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            str_codeSent=s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            String str_codeRecieved= phoneAuthCredential.getSmsCode();
            if (str_codeRecieved!=null){

                verifyCode(str_codeRecieved);
            }

        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {

            Toast.makeText(VerificationActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    };

    private void verifyCode(String str_codeRecieved_or_typed) {

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(str_codeSent, str_codeRecieved_or_typed);

        signInWithUserCredentials(credential);
    }

    private void signInWithUserCredentials(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user=mAuth.getCurrentUser();
                            if (user!=null){
                                String UserId=user.getUid();
                                final DatabaseReference rootRef= FirebaseDatabase.getInstance().getReference("Users").child(UserId);
                                rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (!snapshot.exists()){

                                            Map<String,Object> userMap=new HashMap<>();
                                            userMap.put("name",str_name);
                                            userMap.put("phno",user.getPhoneNumber());
                                            rootRef.updateChildren(userMap);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }


                            Toast.makeText(VerificationActivity.this, "SignUp Successfull", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(VerificationActivity.this,ChatsActivity.class);
                            intent.putExtra("source","verification");
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                        } else {
                            Toast.makeText(VerificationActivity.this, "SignUp Not Successfull", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void continueOnClick(View view) {

        String str_codeTyped=et_otp.getText().toString().trim();
        if (str_codeTyped.length()<6){

            Toast.makeText(this, "Enter A Valid OTP (6-digits)", Toast.LENGTH_SHORT).show();
        }
        else {
            verifyCode(str_codeTyped);
        }

    }
}