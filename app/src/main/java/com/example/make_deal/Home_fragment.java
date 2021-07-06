package com.example.make_deal;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Home_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Home_fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    CardView phone,laptop,camera,headphone,accesories,other;
    FloatingActionButton add;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Home_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Home_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Home_fragment newInstance(String param1, String param2) {
        Home_fragment fragment = new Home_fragment();
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

        View v=inflater.inflate(R.layout.fragment_home_fragment, container, false);

        phone=v.findViewById(R.id.phone);
        laptop=v.findViewById(R.id.laptop);
        camera=v.findViewById(R.id.camera);
        headphone=v.findViewById(R.id.headphone);
        accesories=v.findViewById(R.id.accesories);
        other=v.findViewById(R.id.other);

        add=v.findViewById(R.id.add);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Select Product Image", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), add_product.class);
                startActivity(intent);

            }
        });

        Intent intent=new Intent(getContext(),viewProduct_category.class);

        phone.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                intent.putExtra("type","phone");
                startActivity(intent);
                // Toast.makeText(getActivity(), "phone", Toast.LENGTH_SHORT).show();
            }
        });

        laptop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("type","laptop");
                startActivity(intent);
                //Toast.makeText(getActivity(), "laptop", Toast.LENGTH_SHORT).show();
            }
        });
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("type","camera");
                startActivity(intent);
                //Toast.makeText(getActivity(), "camera", Toast.LENGTH_SHORT).show();
            }
        });
        headphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("type","headphone");
                startActivity(intent);
                //Toast.makeText(getActivity(), "headphone", Toast.LENGTH_SHORT).show();
            }
        });
        accesories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("type","accessories");
                startActivity(intent);
                // Toast.makeText(getActivity(), "accessories", Toast.LENGTH_SHORT).show();
            }
        });
        other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("type","other");
                startActivity(intent);
              //  Toast.makeText(getActivity(), "other", Toast.LENGTH_SHORT).show();
            }
        });


        // Inflate the layout for this fragment
        return v;
    }
}