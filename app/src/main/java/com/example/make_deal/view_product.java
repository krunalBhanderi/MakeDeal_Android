package com.example.make_deal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class view_product extends AppCompatActivity {

    private TextView mTextView,type,title,brand_name,original_price,selling_price,duration,duration_type,description;
    Button chat,add_cart;
    ImageView imageView;
    FirebaseFirestore db;
    Integer val=0;
    private FirebaseUser auth;
    String owner="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_product);

        db=FirebaseFirestore.getInstance();
        mTextView = (TextView) findViewById(R.id.text);

        Intent intent=getIntent();
        String url=intent.getStringExtra("url");

        type=(TextView) findViewById(R.id.type);
        title=(TextView) findViewById(R.id.title);
        brand_name=(TextView) findViewById(R.id.brand_name);
        original_price=(TextView) findViewById(R.id.original_price);
        selling_price=(TextView) findViewById(R.id.selling_price);
        duration=(TextView) findViewById(R.id.duration);
        description=(TextView)findViewById(R.id.description);



        auth= FirebaseAuth.getInstance().getCurrentUser();

        chat=(Button)findViewById(R.id.chat);

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("product")
                        .whereEqualTo("url",url)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Log.d(TAG, document.getId() + " => " + document.getData());

                                            owner=document.get("owner").toString();
                                            Intent intent=new Intent(getApplicationContext(),chat_room.class);
                                            intent.putExtra("owner",owner);
                                            startActivity(intent);
                                    }

                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }

                            }

                        });


            }
        });





        final Integer[] count = {0};
        add_cart=(Button)findViewById(R.id.add_cart);

        add_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                db.collection("cart")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Log.d(TAG, document.getId() + " => " + document.getData());

                                        if(auth.getEmail().equals(document.get("email").toString()) && url.equals(document.get("product").toString()))
                                        {
                                            val=1;
                                        }

                                      };
                                    if(val.equals(0)) {
                                        Map cart = new HashMap<>();
                                        cart.put("email", auth.getEmail());
                                        cart.put("product", url);


                                        db.collection("cart")
                                                .add(cart)
                                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                    @Override
                                                    public void onSuccess(DocumentReference documentReference) {
                                                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());

                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.w(TAG, "Error adding document", e);
                                                    }
                                                });

                                    }

                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }

                            }

                        });




            }
        });


        imageView=(ImageView) findViewById(R.id.imageView);
        Picasso.with(imageView.getContext()).load(url).into(imageView);



        db.collection("product")
                .whereEqualTo("url",url)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());

                                title.setText(document.get("name").toString());
                                brand_name.setText(document.get("brand_name").toString());
                                duration.setText(document.get("duration").toString()+ " " + document.get("duration_type"));
                                original_price.setText(document.get("original_price").toString());
                                selling_price.setText(document.get("selling_price").toString());
                                description.setText(document.get("desc").toString());
                            };
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }

                    }

                });





    }
}