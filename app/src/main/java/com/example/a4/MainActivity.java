package com.example.a4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button zaloguj;
    EditText login, haslo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login = (EditText) findViewById(R.id.Login);
        haslo = (EditText) findViewById(R.id.Password);
        zaloguj = (Button) findViewById(R.id.button2);

        /*zaloguj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (login.getText().toString().isEmpty() || haslo.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Enter the Data", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Login - " + login.getText().toString() + " \n" + "Hasło - " + haslo.getText().toString()
                           , Toast.LENGTH_SHORT).show();
                }
            }
        });*/

        zaloguj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                String pierwsza = login.getText().toString();
                String druga = haslo.getText().toString();
                if(pierwsza.equals("lukasz")&&druga.equals("pluto12")){

                    intent = new Intent(MainActivity.this, WypActivity.class);
                    startActivity(intent);
                }
            }
        });

    }

    /*public void click(View view) {
        Intent intent;
        if(view.getId()==R.id.button2){
            intent=new Intent(MainActivity.this, wyp.class);
            startActivity(intent);


        }
    }*/
}
