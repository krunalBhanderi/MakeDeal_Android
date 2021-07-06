package com.example.make_deal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    private TextView mTextView;
    EditText email,password;
    Button login,signup;
    private FirebaseAuth auth;
    SharedPreferences sharedpreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        auth=FirebaseAuth.getInstance();

        if(auth.getCurrentUser()!=null)
        {
            Toast.makeText(this, "Existing user", Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(getApplicationContext(),home.class);
            startActivity(intent);
        }


        signup=(Button)findViewById(R.id.signup);
        mTextView = (TextView) findViewById(R.id.text);
        login=(Button) findViewById(R.id.button1);
        //b1.setBackgroundColor(Color.alpha());
        login.getBackground().setAlpha(100);

        //b1.setOnClickListener();


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),register.class));
            }
        });


        email=(EditText)findViewById(R.id.email);
        password=(EditText)findViewById(R.id.password);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                String emailvalue=email.getText().toString();
                String passwordvalue=password.getText().toString();

                if(emailvalue.equals("admin") && passwordvalue.equals("admin"))
                {
                    startActivity(new Intent(getApplicationContext(),admin_home.class));
                }
                else if(TextUtils.isEmpty(emailvalue)){
                  //  Toast.makeText(Login.this, "Enter valid email address", Toast.LENGTH_SHORT).show();
                    email.setError("Enter valid email address");
                }
                else if(TextUtils.isEmpty(passwordvalue)){
                    //Toast.makeText(Login.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    password.setError("Enter Password");
                }
//                if (TextUtils.isEmpty(fullnamevalue)) {
//                    fullname.setError("Enter Full Name!");
//                }
//                else if (TextUtils.isEmpty(emailvalue)) {
//                    email.setError("Enter Valid Email Address!");
//                }




                else{
                auth.signInWithEmailAndPassword(emailvalue,passwordvalue)
                        .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    if (passwordvalue.length() < 6) {
                                        Toast.makeText(Login.this, "password length should be at least 6", Toast.LENGTH_SHORT).show();
                                    }
                                 else {
                                        Toast.makeText(Login.this, emailvalue, Toast.LENGTH_SHORT).show();
                                        Toast.makeText(Login.this, passwordvalue, Toast.LENGTH_SHORT).show();
                                    Toast.makeText(Login.this, "auth failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                                else{
                                    Toast.makeText(Login.this, "signin sucessfull", Toast.LENGTH_SHORT).show();
                                    Intent intent=new Intent(getApplicationContext(),home.class);
                                    startActivity(intent);
                                }
                            }
                        });
            }
            }
        });
    }
}