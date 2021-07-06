package com.example.make_deal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;



public class viewUser_admin extends AppCompatActivity {

    FirebaseFirestore db=FirebaseFirestore.getInstance();
    private TextView mTextView;
    TextView name,sold,total,email_user;
    ExtendedFloatingActionButton remove;
    String email="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user_admin);

        //mTextView = (TextView) findViewById(R.id.text);

        name=(TextView)findViewById(R.id.name);
        sold=(TextView)findViewById(R.id.sold);
        total=(TextView)findViewById(R.id.total);
        email_user=(TextView)findViewById(R.id.user_email);

        remove=(ExtendedFloatingActionButton) findViewById(R.id.user) ;


        ArrayList<MyListData> myListData = new ArrayList<MyListData>();

        //   myListData.add(new MyListData("hi", "https://firebasestorage.googleapis.com/v0/b/freelancer-android.appspot.com/o/images%2Fe2c00363-e7dd-49d3-87cd-f3d3f93b2124?alt=media&token=979b7089-32c6-4d6d-82b5-82c041194b93"));

        Intent intent = getIntent();
         email=intent.getStringExtra("email");



        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CollectionReference itemsRef = db.collection("user");
                Query query = itemsRef.whereEqualTo("email", email);

                query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                itemsRef.document(document.getId()).delete();
                                Toast.makeText(viewUser_admin.this, "delete", Toast.LENGTH_SHORT).show();
                            }
                            Intent intent =new Intent(getApplicationContext(),home.class);
                            startActivity(intent);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });



                CollectionReference itemproduct = db.collection("product");
                Query queryProduct = itemproduct.whereEqualTo("owner",email);

                queryProduct.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {

                                String url_val=document.get("url").toString();

                                CollectionReference itemscart = db.collection("cart");
                                Query querycart = itemscart.whereEqualTo("url", url_val);

                                querycart.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (DocumentSnapshot document : task.getResult()) {
                                                itemscart.document(document.getId()).delete();
                                            //    Toast.makeText(viewProduct_cart.this, "delete", Toast.LENGTH_SHORT).show();
                                            }
                                  //          Intent intent =new Intent(getApplicationContext(),home.class);
                                    //        startActivity(intent);
                                        } else {
                                            Log.d(TAG, "Error getting documents: ", task.getException());
                                        }
                                    }
                                });



                                itemproduct.document(document.getId()).delete();
                                Toast.makeText(viewUser_admin.this, "User and it's product delete", Toast.LENGTH_SHORT).show();


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

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerViewProfile);
        MyListAdapter adapter = new MyListAdapter(myListData);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


        db= FirebaseFirestore.getInstance();
        final StringBuilder[] data1 = {new StringBuilder()};
        final StringBuilder[] id = {new StringBuilder()};


        db.collection("user")
                .whereEqualTo("email",email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());

                                name.setText(document.get("name").toString());
                                sold.setText(document.get("sold").toString());
                                total.setText(document.get("product").toString());
                                email_user.setText(document.get("email").toString());

                            };
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }

                    }

                });



        db.collection("product")
                .whereEqualTo("owner",email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());


                                myListData.add(new MyListData(document.get("name").toString(),document.get("duration").toString(), document.get("duration_type").toString(), document.get("selling_price").toString(), document.get("url").toString(),"viewProduct_admin.class"));

                                adapter.notifyDataSetChanged();

                            };
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }

                    }

                });
        adapter.notifyDataSetChanged();


        // Enables Always-on

    }
}