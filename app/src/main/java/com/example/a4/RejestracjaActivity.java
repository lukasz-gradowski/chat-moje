package com.example.a4;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

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

                final FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference docRef = db.collection("users").document("0LjvqVzK0UjGXDc1w3Aj");
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d("Walidacja", "DocumentSnapshot data: " + document.getData());
                            } else {
                                Log.d("Walidacja", "No such document");
                            }
                        } else {
                            Log.w("Error", "get failed with ", task.getException());
                        }
                    }
                });


                final Map<String, Object> user = new HashMap<>();
                user.put("users", log);
                user.put("haslo", password);
                user.put("is_online", 1);

                if (confirm.equals(password) && check.isChecked()) {

                    db.collection("users")
                            .add(user)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Log.d("DODANIE", "DocumentSnapshot added with ID: " + documentReference.getId());
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("Dodanie", "Error adding document", e);
                                }
                            });

                    intent = new Intent(RejestracjaActivity.this, Chat.class);
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
        String txt = sharedPref.getString("login", "");
        login.setText(txt);
    }


}
