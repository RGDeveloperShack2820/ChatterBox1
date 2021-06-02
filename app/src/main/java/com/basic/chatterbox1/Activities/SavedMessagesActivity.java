package com.basic.chatterbox1.Activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.basic.chatterbox1.R;

public class SavedMessagesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_saved_messages);
        setTitle("Saved Messages");

    }


}