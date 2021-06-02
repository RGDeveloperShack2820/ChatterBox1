package com.basic.chatterbox1.Activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.basic.chatterbox1.Adapters.ChattingAdapter;
import com.basic.chatterbox1.MessageModal;
import com.basic.chatterbox1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChattingActivity extends AppCompatActivity {

    RecyclerView rv_chattingMsg;
    EditText et_typeHere;
    ImageView iv_sendBt;
    CircleImageView civ_dp;
    ArrayList<MessageModal> al_messages;
    TextView tv_name,tv_phno;
    String str_chatId;
    ChattingAdapter chattingAdapter;
    DatabaseReference mChatDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatting_activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tv_name=findViewById(R.id.tv_name);
        tv_phno=findViewById(R.id.tv_phno);
        iv_sendBt=findViewById(R.id.iv_sendBt);
        et_typeHere=findViewById(R.id.et_type_here);
        rv_chattingMsg=findViewById(R.id.rv_chatting);
        civ_dp=findViewById(R.id.civ_dp);


        Intent intent =getIntent();
        String str_name=intent.getStringExtra("headerName");
        String str_phno=intent.getStringExtra("phno");
        str_chatId=intent.getStringExtra("chatId");
        int dp_id=intent.getIntExtra("dp",0);
        tv_name.setText(str_name);
        tv_phno.setText(str_phno);
        civ_dp.setImageResource(dp_id);

        mChatDb=FirebaseDatabase.getInstance().getReference().child("Chats").child(str_chatId);

        al_messages=new ArrayList<>();
        getUserChats();
        chattingAdapter=new ChattingAdapter(al_messages);
        rv_chattingMsg.setLayoutManager(new LinearLayoutManager(this));
        rv_chattingMsg.setAdapter(chattingAdapter);


        et_typeHere.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                rv_chattingMsg.scrollToPosition(al_messages.size()-1);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {}
        });

    }

    private void getUserChats() {
        mChatDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot,  String previousChildName) {
                if (snapshot.exists()){
                    String creator="";
                    String text="";

                    if (snapshot.child("text").getValue()!=null)
                        text=snapshot.child("text").getValue().toString();

                    if (snapshot.child("creator").getValue()!=null)
                        creator=snapshot.child("creator").getValue().toString();

                    MessageModal mMsg= new MessageModal(snapshot.getKey(),creator,text);
                    al_messages.add(mMsg);
                    chattingAdapter.notifyDataSetChanged();
                    rv_chattingMsg.scrollToPosition(al_messages.size()-1);
                }
            }

            @Override
            public void onChildChanged(@NonNull  DataSnapshot snapshot, String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull  DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull  DataSnapshot snapshot,  String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.chatting_menu_main, menu);

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

            Intent intent = new Intent(ChattingActivity.this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void sendBtOnClick(View view) {

        String str_currentMsg=et_typeHere.getText().toString().trim();

        if (!str_currentMsg.isEmpty()){
            DatabaseReference newMsdRef= FirebaseDatabase.getInstance().getReference().child("Chats").child(str_chatId).push();
            Map mMsg = new HashMap<>();
            mMsg.put("text",str_currentMsg);
            mMsg.put("creator", FirebaseAuth.getInstance().getCurrentUser().getUid());
            newMsdRef.updateChildren(mMsg);
            et_typeHere.setText("");

        }
    }



}