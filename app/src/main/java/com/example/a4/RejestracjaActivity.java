package com.example.a4;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
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

        check.setOnClickListener(v -> {
            Intent intent;
            intent = new Intent(RejestracjaActivity.this, RegulaminActivity.class);
            startActivity(intent);
        });

        zaloguj.setOnClickListener(v -> {
            final String log = login.getText().toString();
            final String password = haslo.getText().toString();
            final String confirm = potwierdz.getText().toString();
            final String password_hash = BCrypt.hashpw(password, BCrypt.gensalt());

            final FirebaseFirestore db = FirebaseFirestore.getInstance();

            DocumentReference docRef = db.collection("users").document(log);
            docRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Toast.makeText(getApplicationContext(), "Istnieje taki użytkownik", Toast.LENGTH_LONG).show();
                    } else {
                        if (confirm.equals(password) && check.isChecked()) {
                            toRegistration(log, password_hash, db);
                        } else if(!password.equals(confirm)){
                            Toast.makeText(getApplicationContext(), "Hasła nie są takie same", Toast.LENGTH_LONG).show();
                        } else if(!check.isChecked()){
                            Toast.makeText(getApplicationContext(), "Proszę potwierdzić regulamin", Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
                    //Log.w("Błąd", "get failed with ", task.getException());
                    Toast.makeText(getApplicationContext(), "Błąd połączenia", Toast.LENGTH_LONG).show();
                }
            });
        });
    }

    public void toRegistration(String log, String password, FirebaseFirestore db) {
        final Map<String, Object> user = new HashMap<>();
        //user.put("login", log);
        user.put("password", password);
        user.put("is_online", true);
        user.put("last_time_login", Timestamp.now());

        db.collection("users").document(log)
            .set(user)
            .addOnSuccessListener(aVoid -> Log.d("Dodanie", "DocumentSnapshot successfully written!"))
            .addOnFailureListener(e -> Log.w("Dodanie", "Error writing document", e));

        Intent intent;
        intent = new Intent(RejestracjaActivity.this, Chat.class);
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
        String txt = sharedPref.getString("login", "");
        login.setText(txt);
    }
}
