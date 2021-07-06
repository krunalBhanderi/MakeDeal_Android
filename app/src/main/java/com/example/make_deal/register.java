package com.example.make_deal;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class register extends AppCompatActivity {

    Button b1,register;
    EditText fullname,email,password;
    private FirebaseAuth auth;
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        register=findViewById(R.id.register);
        register.getBackground().setAlpha(100);

        auth=FirebaseAuth.getInstance();

        fullname=findViewById(R.id.fullname);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);

        register=findViewById(R.id.register);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String emailvalue=email.getText().toString();
                String passwordvalue=password.getText().toString();
                String fullnamevalue=fullname.getText().toString();

//                if(emailvalue.isEmpty()){
//                    Toast.makeText(getApplicationContext(),"Enter Valid Email Address!",Toast.LENGTH_SHORT).show();
//                }
//                if(passwordvalue.isEmpty()){
//                    Toast.makeText(register.this, "Enter password", Toast.LENGTH_SHORT).show();
//                }
//                if(passwordvalue.length()<6){
//                    Toast.makeText(getApplicationContext(),"Password Too short",Toast.LENGTH_SHORT).show();
//                }

                if (TextUtils.isEmpty(fullnamevalue)) {
                    fullname.setError("Enter Full Name!");
                }
                else if (TextUtils.isEmpty(emailvalue)) {
                    email.setError("Enter Valid Email Address!");
                }
                else if (TextUtils.isEmpty(passwordvalue)) {
                    password.setError(" Enter Valid Email Address!");
                }
                else if (passwordvalue.length()<6) {
                    password.setError("Password Too short");
                }
                else {

                    auth.createUserWithEmailAndPassword(emailvalue, passwordvalue)
                            .addOnCompleteListener(register.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    //    Toast.makeText(register.this, "createUserWithEmail"+task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                    if (!task.isSuccessful()) {
                                        Toast.makeText(register.this, "Authentication failed" + task.getException(), Toast.LENGTH_SHORT).show();

                                    } else {

                                        Map<String, Object> product = new HashMap<>();


                                        product.put("email", emailvalue);
                                        product.put("password", passwordvalue);
                                        product.put("name", fullnamevalue);
                                        product.put("product", 0);
                                        product.put("sold", 0);

                                        db.collection("user").document().set(product)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void Void) {
                                                        Toast.makeText(register.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                                        startActivity(new Intent(register.this, Login.class));

                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(register.this, "Registration Failed" + e, Toast.LENGTH_LONG).show();
                                                    }
                                                });


                                    }
                                }
                            });
                }
            }
        });




    }
}