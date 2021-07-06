package com.example.make_deal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class admin_home extends AppCompatActivity{

    private TextView mTextView;

    FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

//        Intent intent=new Intent(this,viewproduct_admin.class);
  //      startActivity(intent);


        ArrayList<MyListData> myListData = new ArrayList<MyListData>();

        //   myListData.add(new MyListData("hi", "https://firebasestorage.googleapis.com/v0/b/freelancer-android.appspot.com/o/images%2Fe2c00363-e7dd-49d3-87cd-f3d3f93b2124?alt=media&token=979b7089-32c6-4d6d-82b5-82c041194b93"));


        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        MyListAdapter adapter = new MyListAdapter(myListData);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


        ExtendedFloatingActionButton fab=findViewById(R.id.user);
    //FloatingActionButton fab=findViewById(R.id.user);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), userListAdmin.class);
                startActivity(intent);

            }
        });


        db= FirebaseFirestore.getInstance();


        db.collection("product")
                .whereEqualTo("status","reject")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                //     Toast.makeText(getActivity(), cart[0], Toast.LENGTH_SHORT).show();

                                myListData.add(new MyListData(document.get("name").toString(),document.get("duration").toString(),document.get("duration_type").toString(), document.get("selling_price").toString(), document.get("url").toString(),"viewproduct_admin"));

                                adapter.notifyDataSetChanged();

                            };
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }

                    }

                });
        adapter.notifyDataSetChanged();







        mTextView = (TextView) findViewById(R.id.text);

        // Enables Always-on
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}