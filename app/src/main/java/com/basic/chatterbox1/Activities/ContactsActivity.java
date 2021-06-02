package com.basic.chatterbox1.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.basic.chatterbox1.Adapters.ContactsAdapter;
import com.basic.chatterbox1.ChatModal;
import com.basic.chatterbox1.NameComparator;
import com.basic.chatterbox1.R;
import com.basic.chatterbox1.RecyclerViewOnClickInterface;
import com.basic.chatterbox1.UserModal;
import com.basic.chatterbox1.countryToPhonePrefix;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class  ContactsActivity extends AppCompatActivity implements RecyclerViewOnClickInterface {

    RecyclerView rv_contacts;
    ArrayList<UserModal> al_userList,al_contactList;
    ArrayList<ChatModal> al_userChats;
    ContactsAdapter contactsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

       Intent intent=getIntent();
       String title=intent.getStringExtra("title");
       setTitle(title);

        al_userList=new ArrayList<>();
        al_contactList=new ArrayList<>();
        al_userChats=new ArrayList<>();

        Bundle args = intent.getBundleExtra("BUNDLE");

        al_userChats = (ArrayList<ChatModal>) args.getSerializable("ARRAYLIST");


        rv_contacts=findViewById(R.id.rv_contacts);
        rv_contacts.setLayoutManager(new LinearLayoutManager(this));
        contactsAdapter=new ContactsAdapter(al_userList , this);
        rv_contacts.setAdapter(contactsAdapter);
        readContacts();

    }

    public String getCountryISO(){

        String iso= null;
        TelephonyManager telephonyManager=(TelephonyManager) getApplicationContext().getSystemService(getApplicationContext().TELEPHONY_SERVICE);
        if (telephonyManager.getNetworkCountryIso()!=null){
            if (!telephonyManager.getNetworkCountryIso().toString().equals("")){

                iso= telephonyManager.getNetworkCountryIso().toString();
            }
        }
        Log.i("ISO",countryToPhonePrefix.getPhone(iso));
        return countryToPhonePrefix.getPhone(iso);
    }

    private void readContacts() {

        String ISOPrefix= getCountryISO();
        Cursor contacts_cursor= getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,null);
        while (contacts_cursor.moveToNext()){

            String str_name= contacts_cursor.getString(contacts_cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String str_phno= contacts_cursor.getString(contacts_cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            str_phno= str_phno.replace(" ","");
            str_phno= str_phno.replace("-","");
            str_phno= str_phno.replace("(","");
            str_phno= str_phno.replace(")","");

            if (!String.valueOf(str_phno.charAt(0)).equals("+")){
                str_phno= "+91" + str_phno;
            }

            Log.i("Read Contacts",str_name+"...."+str_phno);
            UserModal um_user= new UserModal(str_name,str_phno,"");
            al_contactList.add(um_user);
            getUserDetails(um_user);


        }

    }

    private void getUserDetails(UserModal um_user) {

        DatabaseReference mUserDB= FirebaseDatabase.getInstance().getReference("Users");
        Query query = mUserDB.orderByChild("phno").equalTo(um_user.getphno());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Log.i("GET USER DETAILS",snapshot.exists()+"");

                if (snapshot.exists()){

                    String phno="",
                           name="";

                    for (DataSnapshot childSnaposhot : snapshot.getChildren()){
                        if (childSnaposhot.child("phno").getValue()!=null){
                            phno=childSnaposhot.child("phno").getValue().toString();
                        }
                        if (childSnaposhot.child("name").getValue()!=null){
                            name=childSnaposhot.child("name").getValue().toString();
                        }

                        Log.i("GET USER DETAILS",name+"...."+phno);
                        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();

                        if (!phno.equals(user.getPhoneNumber())) {
                            UserModal mUser = new UserModal(name, phno, childSnaposhot.getKey());
                            if (name.equals("")){

                                for (UserModal userContactIterator : al_contactList){
                                    if (mUser.getphno().equals(userContactIterator.getphno())){
                                        mUser.setName(userContactIterator.getName());
                                    }
                                }
                            }
                           boolean exists=false;
                            for(int i=0;i<al_userList.size();i++){
                                if(mUser.getphno().equals(al_userList.get(i).getphno())){
                                    exists=true;
                                    break;
                                }
                            }
                            if (!exists) {
                                al_userList.add(mUser);
                            }
                            Collections.sort(al_userList, new NameComparator());
                            contactsAdapter.notifyDataSetChanged();
                        }
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void Onclick(int position) {

        boolean exists=false;


        for(int i=0;i<al_userChats.size();i++){

            if (al_userList.get(position).getphno().equals(al_userChats.get(i).getChatPhno())) {
                exists = true;
                break;
            }
        }

        if (exists==false) {

            String chatID= FirebaseDatabase.getInstance().getReference().child("Chats").push().getKey();

            FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).child("Chats").child(chatID).setValue(al_userList.get(position).getUserId());
            FirebaseDatabase.getInstance().getReference().child("Users").child(al_userList.get(position).getUserId()).child("Chats").child(chatID).setValue(FirebaseAuth.getInstance().getUid());
            Log.i("Contact CLICK",al_userList.get(position).getUserId());
            Log.i("Contact CLICK",FirebaseAuth.getInstance().getUid());
            finish();
        }else{
            Toast.makeText(this, "Chat Already Exists", Toast.LENGTH_SHORT).show();
        }
    }
}