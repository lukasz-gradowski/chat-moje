package com.example.a4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.io.IOException;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    Button zaloguj, zarejestruj;
    EditText login, haslo;
    String txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login = (EditText) findViewById(R.id.Login);
        haslo = (EditText) findViewById(R.id.Password);
        zaloguj = (Button) findViewById(R.id.button2);
        zarejestruj = (Button) findViewById(R.id.button);

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

        zarejestruj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(MainActivity.this, RejestracjaActivity.class);
                startActivity(intent);
            }
        });

        zaloguj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                String log = login.getText().toString();
                String password = haslo.getText().toString();
                // curl -X GET "http://127.0.0.1:81/PHP/login.php?login=janusz&password=test"
                // curl -X POST "http://127.0.0.1:81/PHP/registration.php" --data "login=bar1&password=bar2"
                // curl -X POST -d "login=janusz&password=bar2" "http://127.0.0.1:81/PHP/registration.php"

                OkHttpClient zapytanie = new OkHttpClient();
                RequestBody requestBody = new FormBody.Builder()
                        .add("login", "some_email")
                        .add("password", "some_password")
                        .build();
                final Request request = new Request.Builder()
                        .header("Content-Type", "application/x-www-form-urlencoded")
                        .url("http://127.0.0.1:81/PHPv2/registration.php")
                        .post(requestBody)
                        .build();
                try {
                    Response response = zapytanie.newCall(request).execute();
                    Toast.makeText(getApplicationContext(), "This is my Toast message!",
                            Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    //e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.toString(),
                            Toast.LENGTH_LONG).show();
                }

                if(log.equals("lukasz")&&password.equals("pluto12")){
                    intent = new Intent(MainActivity.this, WypActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences sharedPref = getSharedPreferences("dane", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("login", login.getText().toString());
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences sharedPref = getSharedPreferences("dane", Context.MODE_PRIVATE);
        txt = sharedPref.getString("login", "");
        login.setText(txt);
    }

    /*public void click(View view) {
        Intent intent;
        if(view.getId()==R.id.button2){
            intent=new Intent(MainActivity.this, wyp.class);
            startActivity(intent);
        }
    }*/
}
