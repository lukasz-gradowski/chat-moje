package com.example.a4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

public class Chat extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        SharedPreferences sharedPref = getSharedPreferences("dane", Context.MODE_PRIVATE);
        String txt = sharedPref.getString("login", "");
        TextView nick = findViewById(R.id.nick);
        nick.setText("Twój nick to: "+txt);
    }
}
