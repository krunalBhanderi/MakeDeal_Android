package com.example.make_deal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class viewproduct_admin extends AppCompatActivity {

    private TextView mTextView,type,title,brand_name,original_price,selling_price,duration,duration_type,description;
    FirebaseFirestore db;
    ImageView imageView;
    Button approve,remove;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewproduct_admin);

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


        approve=(Button)findViewById(R.id.approve);
        remove=(Button)findViewById(R.id.remove);

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CollectionReference itemsRef = db.collection("product");
                Query query = itemsRef.whereEqualTo("url", url);
                query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                itemsRef.document(document.getId()).delete();
                   //             itemsRef.document(document.getId()).update(status);
                                Toast.makeText(viewproduct_admin.this, "Product Removed", Toast.LENGTH_SHORT).show();
                            }
                            Intent intent =new Intent(getApplicationContext(),admin_home.class);
                            startActivity(intent);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

            }
        });

        approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CollectionReference itemsRef = db.collection("product");
                Query query = itemsRef.whereEqualTo("url", url);
                Map status=new HashMap();
                status.put("status","approve");
                query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                //itemsRef.document(document.getId()).delete();
                                itemsRef.document(document.getId()).update(status);
                                Toast.makeText(viewproduct_admin.this, "Product Approved", Toast.LENGTH_SHORT).show();
                            }
                            Intent intent =new Intent(getApplicationContext(),home.class);
                            startActivity(intent);
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



        // Enables Always-on
    }
}