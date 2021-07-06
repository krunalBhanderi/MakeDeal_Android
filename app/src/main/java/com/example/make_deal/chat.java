package com.example.make_deal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link chat#newInstance} factory method to
 * create an instance of this fragment.
 */
public class chat extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    FirebaseFirestore db= FirebaseFirestore.getInstance();
    FirebaseUser auth=FirebaseAuth.getInstance().getCurrentUser();
    ArrayList user=new ArrayList();



    public chat() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment chat.
     */
    // TODO: Rename and change types and number of parameters
    public static chat newInstance(String param1, String param2) {
        chat fragment = new chat();
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
        View v=inflater.inflate(R.layout.fragment_chat, container, false);


        ListView listView=(ListView) v.findViewById(R.id.user_list);

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity().getBaseContext(), R.layout.list_white_text,user);

        listView.setAdapter(adapter);

        db.collection("chat")
                .orderBy("stamp", Query.Direction.ASCENDING)

                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                if(document.get("sender").toString().equals(auth.getEmail()))    {
                                    if(!user.contains(document.get("receiver").toString())){


                                    user.add(document.get("receiver"));
                                    adapter.notifyDataSetChanged();
                                    }
                                    }
                                if((document.get("receiver").toString().equals(auth.getEmail()) )){
                                    if(!user.contains(document.get("sender").toString())) {


                                        user.add(document.get("sender"));
                                        adapter.notifyDataSetChanged();
                                    }
                                    }
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }

                    }

                });
        adapter.notifyDataSetChanged();





        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String val= (String) user.get(position);
                Toast.makeText(getActivity(), val, Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getContext(),chat_room.class);
                intent.putExtra("owner",val);
                startActivity(intent);
            }
        });
        return v;
    }
}