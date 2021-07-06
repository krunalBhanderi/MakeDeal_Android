package com.example.make_deal;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class viewProduct_category extends AppCompatActivity {

    private TextView mTextView;
    TextView type;
    EditText search;

    private FirebaseUser auth;

    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_product_category);

        auth= FirebaseAuth.getInstance().getCurrentUser();

        mTextView = (TextView) findViewById(R.id.text);
        search=(EditText) findViewById(R.id.search);

        type=(TextView) findViewById(R.id.type);

        Intent intent=getIntent();
        String type_val=intent.getStringExtra("type");

        type.setText(type_val);
        ArrayList<MyListData> myListData = new ArrayList<MyListData>();

        //   myListData.add(new MyListData("hi", "https://firebasestorage.googleapis.com/v0/b/freelancer-android.appspot.com/o/images%2Fe2c00363-e7dd-49d3-87cd-f3d3f93b2124?alt=media&token=979b7089-32c6-4d6d-82b5-82c041194b93"));


        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerViewCategory);
        MyListAdapter adapter = new MyListAdapter(myListData);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


        db= FirebaseFirestore.getInstance();
        final StringBuilder[] data1 = {new StringBuilder()};
        final StringBuilder[] id = {new StringBuilder()};
        db.collection("product")

             //   .whereEqualTo("type",type_val)

                .whereEqualTo("type",type_val)

                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());

                                if(!document.get("owner").equals(auth.getEmail())) {
                                    myListData.add(new MyListData(document.get("name").toString(), document.get("duration").toString(), document.get("duration_type").toString(), document.get("selling_price").toString(), document.get("url").toString(), "view_product"));
                                    adapter.notifyDataSetChanged();
                                }


                            };
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }

                    }

                });
        adapter.notifyDataSetChanged();



        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

       //         Toast.makeText(viewProduct_category.this, search.getText().toString(), Toast.LENGTH_SHORT).show();
                ArrayList<MyListData> myListData = new ArrayList<MyListData>();

                //   myListData.add(new MyListData("hi", "https://firebasestorage.googleapis.com/v0/b/freelancer-android.appspot.com/o/images%2Fe2c00363-e7dd-49d3-87cd-f3d3f93b2124?alt=media&token=979b7089-32c6-4d6d-82b5-82c041194b93"));


                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerViewCategory);
                MyListAdapter adapter = new MyListAdapter(myListData);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(viewProduct_category.this));
                recyclerView.setAdapter(adapter);


                String search_val=search.getText().toString();
                db.collection("product")
                        //   .whereEqualTo("type",type_val)
                        .orderBy("name")
                        .startAt(search_val)
                        .endAt(search_val+'\uf8ff')

                      //  .whereEqualTo("type",type_val)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Log.d(TAG, document.getId() + " => " + document.getData());
                                        if(type_val.equals(document.get("type").toString())) {
                                            if(!document.get("owner").toString().equals(auth.getEmail())) {
                                                if(document.get("status").toString().equals("approve")) {
                                                    myListData.add(new MyListData(document.get("name").toString(), document.get("duration").toString(), document.get("duration_type").toString(), document.get("selling_price").toString(), document.get("url").toString(), "view_product"));
                                                    adapter.notifyDataSetChanged();
                                                }
                                            }
                                        }


                                    };
                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }

                            }

                        });
                adapter.notifyDataSetChanged();

            }
        });




        // Enables Always-on
       // setAmbientEnabled();
    }
}