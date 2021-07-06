package com.example.make_deal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class userListAdmin extends AppCompatActivity {

    private TextView mTextView;
    ListView listView;
    FirebaseFirestore db=FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list_admin);

        mTextView = (TextView) findViewById(R.id.text);

        listView=(ListView) findViewById(R.id.user);
        //String chat[]={"you : hello ","yash : hi ","you : i want to buy product  " };
        ArrayList<String> user_list=new ArrayList<>();
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, R.layout.list_right_text,user_list);

        listView.setAdapter(adapter);



        db.collection("user")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                //     Toast.makeText(getActivity(), cart[0], Toast.LENGTH_SHORT).show();
                                user_list.add(document.get("email").toString());

                                adapter.notifyDataSetChanged();

                            };
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }

                    }

                });
        adapter.notifyDataSetChanged();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(userListAdmin.this, user_list.get(position), Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getApplicationContext(),viewUser_admin.class);
                intent.putExtra("email",user_list.get(position));
                startActivity(intent);
            }
        });





    }
}