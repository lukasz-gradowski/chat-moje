package com.example.a4;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    Button zaloguj, zarejestruj;
    EditText login, haslo;
    String txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login = findViewById(R.id.Login);
        haslo = findViewById(R.id.Password);
        zaloguj = findViewById(R.id.button2);
        zarejestruj = findViewById(R.id.button);
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

                final FirebaseFirestore db = FirebaseFirestore.getInstance();
                final Map<String, Object> user = new HashMap<>();
                user.put("users", log);
                user.put("haslo", password);
                user.put("is_online", 1);

                if (log.equals("lukaszek") && password.equals("pluto13")) {
//                    db.collection("users")
//                            .add(user)
//                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                                @Override
//                                public void onSuccess(DocumentReference documentReference) {
//                                    Log.d("DODANIE", "DocumentSnapshot added with ID: " + documentReference.getId());
//                                }
//                            })
//                            .addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception e) {
//                                    Log.w("Dodanie", "Error adding document", e);
//                                }
//                            });

                    db.collection("users").document(log)
                            .set(user)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("Dodanie", "DocumentSnapshot successfully written!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("Dodanie", "Error writing document", e);
                                }
                            });

                    intent = new Intent(MainActivity.this, Chat.class);
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
}
