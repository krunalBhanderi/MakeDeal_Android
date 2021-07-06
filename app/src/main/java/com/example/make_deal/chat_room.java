package com.example.make_deal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class chat_room extends AppCompatActivity {

    private TextView mTextView;
    ListView listView;
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    FirebaseUser auth=FirebaseAuth.getInstance().getCurrentUser();
    TextView name;
    EditText msg;
    ImageButton send;
    String owner="";
    String msg_value="h";
    ArrayList chat=new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);


        mTextView = (TextView) findViewById(R.id.text);
        listView=(ListView)findViewById(R.id.chat);


        name=(TextView)findViewById(R.id.name);
        msg=(EditText)findViewById(R.id.msg);
        send=(ImageButton)findViewById(R.id.send);

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, R.layout.list_right_text,chat);

        listView.setAdapter(adapter);
        Intent intent=getIntent();
        owner=intent.getStringExtra("owner");


        name.setText(owner);



        send.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                msg_value=msg.getText().toString();

                Toast.makeText(chat_room.this, msg_value, Toast.LENGTH_SHORT).show();

                msg.setText("");


                SimpleDateFormat s = new SimpleDateFormat("ddMMyyyyhhmmss");
                String stamp = s.format(new Date());
                                        Map cart = new HashMap<>();

                                        cart.put("sender", auth.getEmail());
                                        cart.put("receiver", owner);
                                        cart.put("message",msg_value);
                                        cart.put("stamp",stamp);

                                        db.collection("chat")
                                                .add(cart)
                                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                    @Override
                                                    public void onSuccess(DocumentReference documentReference) {
                                                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());

                                                  //      chat.add(document.get("message").toString());




                                                        db.collection("chat")
                                                                .orderBy("stamp", Query.Direction.ASCENDING)
                                                                .get()
                                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                        if (task.isSuccessful()) {
                                                                            ArrayList chat=new ArrayList();
                                                                            ArrayAdapter<String> adapter=new ArrayAdapter<String>(getApplicationContext(), R.layout.list_right_text,chat);

                                                                            listView.setAdapter(adapter);
                                                                            Intent intent=getIntent();
                                                                            owner=intent.getStringExtra("owner");


                                                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                                                Log.d(TAG, document.getId() + " => " + document.getData());
                                                                                if((document.get("sender").toString().equals(auth.getEmail()) || document.get("sender").toString().equals(owner) ) && (document.get("receiver").toString().equals(auth.getEmail()) || document.get("receiver").toString().equals(owner) )) {
                                                                                    chat.add(document.get("message").toString()+" "+":"+" "+document.get("sender"));
                                                                                    adapter.notifyDataSetChanged();
                                                                                }
                                                                            }
                                                                        } else {
                                                                            Log.d(TAG, "Error getting documents: ", task.getException());
                                                                        }

                                                                    }

                                                                });
                                                        adapter.notifyDataSetChanged();

                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.w(TAG, "Error adding document", e);
                                                    }
                                                });




            }
        });




        db.collection("chat")
                .orderBy("stamp", Query.Direction.ASCENDING)

                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                if((document.get("sender").toString().equals(auth.getEmail()) || document.get("sender").toString().equals(owner) ) && (document.get("receiver").toString().equals(auth.getEmail()) || document.get("receiver").toString().equals(owner) )) {
                                    chat.add(document.get("message").toString()+" "+":"+" "+document.get("sender"));
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }

                    }

                });
        adapter.notifyDataSetChanged();



        // Enables Always-on
    }
}