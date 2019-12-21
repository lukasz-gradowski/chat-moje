package com.example.a4;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class RejestracjaActivity extends AppCompatActivity {

    EditText login, haslo, potwierdz;
    Button zaloguj;
    CheckBox check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rejestracja);

        login = findViewById(R.id.Login);
        haslo = findViewById(R.id.Password);
        zaloguj = findViewById(R.id.button2);
        check = findViewById(R.id.checkBox2);
        potwierdz = findViewById(R.id.Confirm);

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(RejestracjaActivity.this, RegulaminActivity.class);
                startActivity(intent);
            }
        });

        zaloguj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                String log = login.getText().toString();
                String password = haslo.getText().toString();
                String confirm = potwierdz.getText().toString();
                if (log.equals("lukasz") && password.equals("pluto12") && confirm.equals(password) && check.isChecked()) {

                    intent = new Intent(RejestracjaActivity.this, WypActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}
