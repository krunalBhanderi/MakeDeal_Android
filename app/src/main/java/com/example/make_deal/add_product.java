package com.example.make_deal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static android.content.ContentValues.TAG;


public class add_product extends AppCompatActivity implements AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {

    private TextView mTextView;
    Button add_product,add_image;
    EditText product_name,brand_name,original_price,selling_price,duration,product_description;
    String product_type="",duration_type_string="";
    Spinner spin,duration_type;
      String img_url="";
    ImageView imageView;
    private Uri filePath;
    Bitmap bitmap=null;
    private FirebaseUser auth;


    private StorageReference Folder;

    private final int PICK_IMAGE_REQUEST=22;

    FirebaseStorage storage;
    StorageReference storageReference;

    String Type[]={"----Select Product Type----","phone","laptop","camera","headphone","Accessories","Other"};
    String duration_Type[]={"Duration Type","Days","Month","Year"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        Folder=FirebaseStorage.getInstance().getReference().child("images");
        mTextView = (TextView) findViewById(R.id.text);

        ActionBar actionBar;
        actionBar=getSupportActionBar();
        ColorDrawable colorDrawable=new  ColorDrawable(Color.parseColor("#0F9D58"));

        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();


        imageView=(ImageView)findViewById(R.id.imageView);

        product_name=(EditText) findViewById(R.id.product_name);
        brand_name=(EditText) findViewById(R.id.brand_name);
        original_price=(EditText) findViewById(R.id.original_price);
        selling_price=(EditText) findViewById(R.id.selling_price);
        duration=(EditText) findViewById(R.id.duration);
        product_description=(EditText) findViewById(R.id.product_description);

        product_description=(EditText) findViewById(R.id.product_description);
        spin = (Spinner) findViewById(R.id.product_type);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Type);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);
        spin.setOnItemSelectedListener(this);


        duration_type = (Spinner) findViewById(R.id.duration_type);
        ArrayAdapter<String> adapter_duration = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, duration_Type);
        adapter_duration.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        duration_type.setAdapter(adapter_duration);
        duration_type.setOnItemSelectedListener(this);



        auth= FirebaseAuth.getInstance().getCurrentUser();


        add_product=findViewById(R.id.add_product);



                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(
                        Intent.createChooser(
                                intent,
                                "Select Image from here..."),
                        PICK_IMAGE_REQUEST);






        add_product.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                            uploadImage();



            }
        }));


        // Enables Always-on
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data)
    {

        super.onActivityResult(requestCode,resultCode,data);


        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            filePath = data.getData();
            try {
                 bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getContentResolver(),
                                filePath);


            }

            catch (IOException e) {
                e.printStackTrace();
            }

            Bitmap b=null;
            try {
                b = BitmapFactory.decodeStream(getContentResolver().openInputStream(filePath));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }




            Glide.with(this)
                    .load(b)
                    .into(imageView);

      //      Picasso.with(context).load(bitmap)
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
       // Toast.makeText(add_product.this, "Selected type: "+Type[position] ,Toast.LENGTH_SHORT).show();
        if(parent.getId()==R.id.product_type) {
            product_type = Type[position].toString();
        }

        else if(parent.getId()==R.id.duration_type){
            duration_type_string=duration_Type[position].toString();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }



private void uploadImage() {

    String name_value = product_name.getText().toString();
    String brand_name_value=brand_name.getText().toString();
    String original_price_value=original_price.getText().toString();
    String selling_price_value=selling_price.getText().toString();
    String duration_value=duration.getText().toString();
    String desc_value = product_description.getText().toString();


    if (TextUtils.isEmpty(name_value)) {
        product_name.setError("Product name cannot be empty");
    }
    else if (TextUtils.isEmpty(brand_name_value)) {
        brand_name.setError(" Bran Name cannot be empty");
    }
    else if (TextUtils.isEmpty(original_price_value)) {
        original_price.setError(" Original Price cannot be empty");
    }
    else if (Integer.parseInt(original_price_value)<=0) {
        original_price.setError(" Original Price cannot be 0 or less than 0");
    }

    else if (TextUtils.isEmpty(selling_price_value)) {
        selling_price.setError(" Selling Price cannot be empty");
    }
    else if (Integer.parseInt(selling_price_value)<=0) {
        selling_price.setError(" Selling Price cannot be 0 or less than 0");
    }
    else if (TextUtils.isEmpty(duration_value)) {
        duration.setError(" Time Duration cannot be empty");
    }
    else if(duration_type_string.equals("Duration Type"))
    {
        Toast.makeText(this, "Select Time Duration Type", Toast.LENGTH_SHORT).show();
    }
    else if(product_type.equals("----Select Product Type----"))
    {
        Toast.makeText(this, "Select Product Type", Toast.LENGTH_SHORT).show();
    }




    else if (TextUtils.isEmpty(desc_value)) {
        product_description.setError(" Projduct Desc cannot be empty");
    }

    else {


        if (filePath != null) {

            // Code for showing progressDialog while uploading
            ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();


            // Defining the child of storageReference
            StorageReference ref
                    = storageReference
                    .child(
                            "images/"
                                    + UUID.randomUUID().toString());

            // adding listeners on upload
            // or failure of image
            StorageTask<UploadTask.TaskSnapshot> taskSnapshotStorageTask = ref.putFile(filePath)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot) {

                                    progressDialog.dismiss();

                                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {


                                            String imgurl = uri.toString();
                                            // Toast.makeText(add_product.this, ""+imgurl, Toast.LENGTH_SHORT).show();
                                            img_url = imgurl;
                                            //                    product_description.setText(imgurl);


                                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                                            Map<String, Object> product = new HashMap<>();


                                            product.put("name", name_value);
                                            product.put("brand_name",brand_name_value);
                                            product.put("original_price",original_price_value);
                                            product.put("selling_price",selling_price_value);
                                            product.put("duration",duration_value);
                                            product.put("type", product_type);
                                            product.put("desc", desc_value);
                                            product.put("url", img_url);
                                            product.put("duration_type",duration_type_string);
                                            product.put("owner",auth.getEmail());
                                            product.put("status","reject");

                                            db.collection("product").document().set(product)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void Void) {
                                                            Toast.makeText(add_product.this, "Product Added Successfully", Toast.LENGTH_SHORT).show();
                                                            Intent intent = new Intent(getApplicationContext(), home.class);
                                                            startActivity(intent);


                                                            CollectionReference itemsRef = db.collection("user");
                                                            Query query = itemsRef.whereEqualTo("email",auth.getEmail());
                                                            Map status=new HashMap();
                                                            status.put("status","sold");
                                                            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                    if (task.isSuccessful()) {
                                                                        for (DocumentSnapshot document : task.getResult()) {
                                                                            //  itemsRef.document(document.getId()).delete();
                                                                           // itemsRef.document(document.getId()).update("sold", FieldValue.increment(1));
                                                                            itemsRef.document(document.getId()).update("product", FieldValue.increment(1));

                                                                            Toast.makeText(add_product.this, "increment", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                        Intent intent =new Intent(getApplicationContext(),home.class);
                                                                        startActivity(intent);
                                                                    } else {
                                                                        Log.d(TAG, "Error getting documents: ", task.getException());
                                                                    }
                                                                }
                                                            });


                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(add_product.this, "Product not Added Please try again" + e, Toast.LENGTH_LONG).show();
                                                        }
                                                    });
                                        }


                                    });

                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast
                                    .makeText(add_product.this,
                                            "Failed " + e.getMessage(),
                                            Toast.LENGTH_SHORT)
                                    .show();
                        }
                    })
                    .addOnProgressListener(new com.google.firebase.storage.OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                            {
                                double progress
                                        = (100.0
                                        * snapshot.getBytesTransferred()
                                        / snapshot.getTotalByteCount());
                                progressDialog.setMessage(
                                        "Uploaded "
                                                + (int) progress + "%");

                            }
                        }
                    });


        }
    }
}
}