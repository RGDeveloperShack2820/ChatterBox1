package com.basic.chatterbox1.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.basic.chatterbox1.Adapters.ChatsAdapter;
import com.basic.chatterbox1.ChatModal;
import com.basic.chatterbox1.R;
import com.basic.chatterbox1.RecyclerViewOnClickInterface;
import com.basic.chatterbox1.UserModal;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

public class ChatsActivity extends AppCompatActivity implements RecyclerViewOnClickInterface {

    public static final int CONTACTS_PERMISSION_CODE = 10;
    RecyclerView recyclerView;
    ArrayList<ChatModal> al_chats ;
    String str_message[]={"Hello How Are You","let's Part Tonight","up for dinner??","is presentation ready??","let's play CoD","Send Me the copy!!"};
    int[] int_userIds={R.drawable.user_image_02,R.drawable.user_image_04,R.drawable.user_image_06,R.drawable.user_image_01,R.drawable.user_image_03,R.drawable.user_image_05};
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    DatabaseReference rootRef;
    String str_name="";
    String str_phno="";
    String str_email="";
    FloatingActionButton fab;
    ChatsAdapter chatsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Chats");

        al_chats=new ArrayList<>() ;
        fab= findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askPermission("New Chat");
            }
        });
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        if (currentUser==null) {
            sendToLogin();
        }
        else{

                getUserDetails();
        }


        recyclerView=findViewById(R.id.rv_chats);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatsAdapter=new ChatsAdapter(this,al_chats,str_message,int_userIds);
        recyclerView.setAdapter(chatsAdapter);




    }

    private void askPermission(String title) {

        if ((ContextCompat.checkSelfPermission(ChatsActivity.this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED)||(ContextCompat.checkSelfPermission(ChatsActivity.this, Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED)){
            ActivityCompat.requestPermissions(ChatsActivity.this,new String[] {Manifest.permission.WRITE_CONTACTS,Manifest.permission.READ_CONTACTS}, CONTACTS_PERMISSION_CODE);

        }
        else {
            openContacts(title);
        }
    }

    private void openContacts(String title) {

        Intent intent =new Intent(ChatsActivity.this,ContactsActivity.class);
        Bundle args = new Bundle();
        args.putSerializable("ARRAYLIST",(Serializable) al_chats);
        intent.putExtra("BUNDLE",args);
        intent.putExtra("title",title);
        startActivity(intent);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode== CONTACTS_PERMISSION_CODE){
            if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                openContacts("Contacts");
            }
            else {
                Toast.makeText(this, "Contacts Permisssion Required", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (currentUser==null) {
            sendToLogin();
        }
    }

    private void  sendToLogin() {

        Intent intent =new Intent(ChatsActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.chats_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            Intent intent =new Intent(ChatsActivity.this,SettingsActivity.class);
            intent.putExtra("name",str_name);
            intent.putExtra("phno",str_phno);
            intent.putExtra("email",str_email);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_contacts) {

            askPermission("Contacts");
            return true;
        }

        if (id == R.id.action_logout) {

            mAuth.signOut();
            sendToLogin();
            return true;
        }

        if (id == R.id.action_Saved) {

            Intent intent =new Intent(ChatsActivity.this,SavedMessagesActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void Onclick(int position) {
        Intent intent =new Intent(ChatsActivity.this,ChattingActivity.class);
         intent.putExtra("headerName",al_chats.get(position).getChatName());
         intent.putExtra("dp",int_userIds[position]);
         intent.putExtra("chatId",al_chats.get(position).getChatID());
         intent.putExtra("phno",al_chats.get(position).getChatPhno());
        startActivity(intent);
    }

    private void getUserDetails() {

        String UserId= currentUser.getUid();
        rootRef= FirebaseDatabase.getInstance().getReference("Users").child(UserId);
        str_phno= currentUser.getPhoneNumber();

        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){

                    if (snapshot.child("name").getValue()!=null){
                        str_name=snapshot.child("name").getValue().toString();
                        Log.i("IMPORTANT inside if",""+str_name);
                        Log.i("IMPORTANT inside if",""+str_phno);

                    }
                    if (snapshot.child("email").getValue()!=null){
                        str_email=snapshot.child("email").getValue().toString();
                        Log.i("IMPORTANT inside if",""+str_email);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        rootRef=FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getUid()).child("Chats");
        rootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {
                if (snapshot.exists()){

                  for (DataSnapshot dataSnapshot: snapshot.getChildren()){


                        Log.i("CHATS IDS :",dataSnapshot.getValue().toString());
                        rootRef= FirebaseDatabase.getInstance().getReference("Users").child(dataSnapshot.getValue().toString());

                        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull  DataSnapshot snapshot) {

                                if (snapshot.exists()){
                                    String name="";
                                    String phno="";

                                    if (snapshot.child("name").getValue()!=null){
                                       name =snapshot.child("name").getValue().toString();

                                    }
                                    if (snapshot.child("phno").getValue()!=null){
                                        phno=snapshot.child("phno").getValue().toString();
                                        Log.i("MMMMMMMMMMMMMM CHAT", name+".........."+phno);
                                    }

                                    Log.i("MMMMMMMMMMMMMM CHAT", name+".........."+phno);
                                    ChatModal mchat=new ChatModal(dataSnapshot.getKey(),name,phno);
                                    boolean exists=false;
                                    for (ChatModal chatIterator: al_chats){
                                        if (chatIterator.getChatID().equals(mchat.getChatID())){
                                            exists=true;
                                        }
                                    }
                                    if (!exists){
                                        al_chats.add(mchat);
                                        chatsAdapter.notifyDataSetChanged();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull  DatabaseError error) {}
                        });
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull  DatabaseError error) {}
        });

    }
}