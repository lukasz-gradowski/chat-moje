package com.example.a4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class RejestracjaActivity extends AppCompatActivity {

    EditText login, haslo, potwierdz;
    Button zaloguj;
    CheckBox check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rejestracja);

        login = (EditText) findViewById(R.id.Login);
        haslo = (EditText) findViewById(R.id.Password);
        zaloguj = (Button) findViewById(R.id.button2);
        check = (CheckBox) findViewById(R.id.checkBox2);
        potwierdz = (EditText) findViewById(R.id.Confirm);

        check.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent;
                intent = new Intent(RejestracjaActivity.this, RegulaminActivity.class);
                startActivity(intent);
            }
        });

        zaloguj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                String pierwsza = login.getText().toString();
                String druga = haslo.getText().toString();
                String trzecia = potwierdz.getText().toString();
                if(pierwsza.equals("lukasz")&&druga.equals("pluto12")&&trzecia.equals(druga)&&check.isChecked()){

                    intent = new Intent(RejestracjaActivity.this, WypActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}
