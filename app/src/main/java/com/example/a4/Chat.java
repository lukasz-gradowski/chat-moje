package com.example.a4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.HashMap;
import java.util.Map;

public class Chat extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        getMessageFromDb();
        TextView nick = findViewById(R.id.nick);
        nick.setText("Twój nick to: "+getUsername());
        ///->Widok
        final Button sendMessages = findViewById(R.id.sendMessage);
        sendMessages.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
    }

    public void sendMessage() {
        EditText textMessage = findViewById(R.id.textMessage);
        String messToDb = textMessage.getText().toString();
        sendMessageToDb(getUsername(), messToDb);
        textMessage.setText("");
    }

    public void createViewMessage(String msg){
        LinearLayout content = findViewById(R.id.content);
        TextView messages = new TextView(this);
        messages.setText(Html.fromHtml(msg));
        content.addView(messages);
        Log.d("widok", messages.toString());
    }

    public String getUsername() {
        SharedPreferences sharedPref = getSharedPreferences("dane", Context.MODE_PRIVATE);
        String username = sharedPref.getString("login", "");
        return username;
    }

    public void sendMessageToDb(String log, String messToDb) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> data = new HashMap<>();
        data.put("login", log);
        data.put("text", messToDb);
        data.put("time", Timestamp.now());
        db.collection("messages")
            .add(data)
            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                Log.d("Dodanie wiadomosci", "ID wiadomosci: " + documentReference.getId());
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                Log.w("Błąd", "Nie dodało wiadomości", e);
                Toast.makeText(getApplicationContext(), "Błąd", Toast.LENGTH_LONG).show();
                }
            });
    }
    public void getMessageFromDb(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("messages")
            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot snapshots,
                                    @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("TAG", "listen:error", e);
                    return;
                }

                for (DocumentChange dc : snapshots.getDocumentChanges()) {
                    switch (dc.getType()) {
                        case ADDED:
                            Log.d("TAG", "New Msg: " + dc.getDocument().toObject(Message.class));
                            String txt = dc.getDocument().getData().get("text").toString();
                            String login = dc.getDocument().getData().get("login").toString();
                            String time = dc.getDocument().getData().get("time").toString();
                            time = filteringTimestamp(time);
                            String toSend = "<b>&lt;"+login+"&gt;</b>: "+txt+" ||"+time;
                            createViewMessage(toSend);
                            break;
                        case MODIFIED:
                            Log.d("TAG", "Modified Msg: " + dc.getDocument().toObject(Message.class));
                            break;
                        case REMOVED:
                            Log.d("TAG", "Removed Msg: " + dc.getDocument().toObject(Message.class));
                            break;
                    }
                }

                }
            });
    }

    public String filteringTimestamp(String timestamp){
        String [] time = timestamp.split(",");
        time = time[0].split("=");

        Integer houer = 0;
        Integer minuts = 0;
        Integer seconds = 0;
        Integer temp_time = Integer.valueOf(time[1]);

        temp_time = Integer.valueOf(time[1]) - (Integer.valueOf(time[1])/3600/24)*24*3600;
        houer = temp_time/3600; //14
        minuts = (temp_time - (houer*3600))/60;
        seconds = temp_time - (houer*3600) - (minuts*60);
        houer++;
        return round_time(houer)+":"+round_time(minuts)+":"+round_time(seconds);
    }

    public String round_time(Integer number) {
        if(number<10){
            return "0"+number.toString();
        }else{
            return number.toString();
        }
    }


}

