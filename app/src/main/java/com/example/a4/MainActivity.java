package com.example.a4;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import org.mindrot.jbcrypt.BCrypt;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSIONS_REQUEST_READ_PHONE_STATE = 999;
    private java.lang.String sharedIMEI;
    private boolean sharedIsOnline;
    Button zaloguj, zarejestruj;
    EditText login, haslo;

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
                            //Log.d("Dane usera", "hasło z bazy: " + document.getData().get("password"));
                            String password_db = document.getData().get("password").toString();
                            if (!BCrypt.checkpw(password, password_db)) {
                                Toast.makeText(getApplicationContext(), "Złe hasło", Toast.LENGTH_LONG).show();
                            } else {
                                switchToChat(log);
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

        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            Log.d("onPause:", "is not granted permissions to read IMEI");
        } else {
            editor.putString("IMEI", getDeviceIMEI());
        }

        editor.apply();

        Log.d("onPause:", "start");
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPref = getSharedPreferences("dane", Context.MODE_PRIVATE);
        String sharedLogin = sharedPref.getString("login", "");
        sharedIsOnline = sharedPref.getBoolean("is_online", false);
        sharedIMEI = sharedPref.getString("IMEI", "");

        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, PERMISSIONS_REQUEST_READ_PHONE_STATE);
        } else {
            Log.d("onResume:", "shared IMEI:"+sharedIMEI);
            autoSingIn(sharedLogin);
        }

        login.setText(sharedLogin);
    }

    public boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public void switchToChat(String log) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> data = new HashMap<>();
        data.put("is_online", true);
        data.put("last_time_login", Timestamp.now());
        db.collection("users").document(log)
            .update(data)
            .addOnSuccessListener(aVoid -> Log.d("Zalogowanie", log+"Zalogował sie"))
            .addOnFailureListener(e -> {
                Log.w("Zalogowanie", "Błąd", e);
                Toast.makeText(getApplicationContext(), "Błąd", Toast.LENGTH_LONG).show();
            });
        toChat();
    }

    private String getDeviceIMEI() {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String IMEI = telephonyManager.getDeviceId();
        Log.d("msg", "IMEI: " + IMEI);
        return IMEI;
    }

    private void autoSingIn(String username) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if (isConnected()) {
            DocumentReference docRef = db.collection("users").document(username);
            docRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists() && "true" == document.getData().get("is_online").toString()){
                        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED && sharedIMEI.equals(getDeviceIMEI())){
                            switchToChat(username);
                        } else
                            Toast.makeText(getApplicationContext(), "Brak uprawnień do autologowania", Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "Brak dostępu do internetu", Toast.LENGTH_LONG).show();
        }
    }
}
