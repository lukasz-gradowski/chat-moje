package com.example.a4;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import org.mindrot.jbcrypt.BCrypt;
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
        zarejestruj.setOnClickListener(v -> {
            Intent intent;
            intent = new Intent(MainActivity.this, RejestracjaActivity.class);
            startActivity(intent);
        });

        zaloguj.setOnClickListener(v -> {
            String log = login.getText().toString();
            final String password = haslo.getText().toString();
            final FirebaseFirestore db = FirebaseFirestore.getInstance();
            if (isConnected()) {
                DocumentReference docRef = db.collection("users").document(log);
                docRef.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d("Dane usera", "DocumentSnapshot data: " + document.getData().get("password"));
                            String password_db = document.getData().get("password").toString();
                            //Log.d("Nie znaleziono usera", password_db+password);
                            if (!BCrypt.checkpw(password, password_db)) {
                                Toast.makeText(getApplicationContext(), "Złe hasło", Toast.LENGTH_LONG).show();
                            } else {
                                is_online(log);
                            }
                        } else {
                            //Log.d("Nie znaleziono usera", "No such document");
                            Toast.makeText(getApplicationContext(), "Nie ma takiego użytkownika", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        //Log.w("Błąd", "get failed with ", task.getException());
                        Toast.makeText(getApplicationContext(), "Błąd logowania", Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                    Toast.makeText(getApplicationContext(), "Brak dostępu do internetu", Toast.LENGTH_LONG).show();
            }
        });
    }
    public void toChat() {
        Intent intent;
        intent = new Intent(MainActivity.this, Chat.class);
        startActivity(intent);
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

    public boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public void is_online(String log) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> data = new HashMap<>();
        data.put("is_online", 1);
        data.put("last_time_login", Timestamp.now());
        db.collection("users").document(log)
                .update(data)
                .addOnSuccessListener(aVoid -> Log.d("Zalogowanie", log+"Zalogował sie"))
                .addOnFailureListener(e -> {
                    Log.w("Zalogowanie", "Błąd", e);
                    Toast.makeText(getApplicationContext(), "Błąd", Toast.LENGTH_LONG).show();
                });
        toChat();;
    }


}
