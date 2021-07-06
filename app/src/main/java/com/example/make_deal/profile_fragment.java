package com.example.make_deal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link profile_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class profile_fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    FirebaseFirestore db;
    private FirebaseUser auth;

    FloatingActionButton logout;
    TextView name,email,sold_product,total_product;

    public profile_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment profile_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static profile_fragment newInstance(String param1, String param2) {
        profile_fragment fragment = new profile_fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_profile_fragment, container, false);

        name=v.findViewById(R.id.name);
        sold_product=v.findViewById(R.id.sold_product);
        total_product=v.findViewById(R.id.total_product);
        email=v.findViewById(R.id.email);

        logout=v.findViewById(R.id.logout);

        ArrayList<MyListData> myListData = new ArrayList<MyListData>();

        //   myListData.add(new MyListData("hi", "https://firebasestorage.googleapis.com/v0/b/freelancer-android.appspot.com/o/images%2Fe2c00363-e7dd-49d3-87cd-f3d3f93b2124?alt=media&token=979b7089-32c6-4d6d-82b5-82c041194b93"));

        auth=FirebaseAuth.getInstance().getCurrentUser();

        String email_val=auth.getEmail().toString();

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(),Login.class));
            }
        });

        email.setText(email_val);
        Toast.makeText(getActivity(), auth.getDisplayName(), Toast.LENGTH_SHORT).show();
        Toast.makeText(getActivity(), auth.getEmail(), Toast.LENGTH_SHORT).show();

        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.recyclerViewProfile);
        MyListAdapter adapter = new MyListAdapter(myListData);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);




        db= FirebaseFirestore.getInstance();
        final StringBuilder[] data1 = {new StringBuilder()};
        final StringBuilder[] id = {new StringBuilder()};


        db.collection("user")
                .whereEqualTo("email",email_val)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                    name.setText(document.get("name").toString());
                                    sold_product.setText(document.get("sold").toString());
                                    total_product.setText(document.get("product").toString());

                            };
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }

                    }

                });



        db.collection("product")
                .whereEqualTo("owner",email_val)
                .whereEqualTo("status","approve")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());


                                myListData.add(new MyListData(document.get("name").toString(),                                        document.get("duration").toString(),
                                        document.get("duration_type").toString(),
                                        document.get("selling_price").toString(),
                                        document.get("url").toString(),"view_my_product"));

                                adapter.notifyDataSetChanged();

                            };
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }

                    }

                });
        adapter.notifyDataSetChanged();

        return v;


    }
}