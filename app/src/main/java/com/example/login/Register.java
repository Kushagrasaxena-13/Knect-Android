package com.example.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class Register extends AppCompatActivity {


    EditText username,name,email,password;
    Button register;
    TextView link;

    FirebaseAuth auth;
    DatabaseReference reference;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.username);
        name = findViewById(R.id.name);
        email= findViewById(R.id.email);
        password= findViewById(R.id.password);

        register = findViewById(R.id.sign_up);
        link = findViewById(R.id.link);

        auth= FirebaseAuth.getInstance();

        link.setOnClickListener(v -> startActivity(new Intent(Register.this,MainActivity.class)));

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pd = new ProgressDialog(Register.this);
                pd.setMessage("Please wait...");
                pd.show();

                String str_username = username.getText().toString();
                String str_name = name.getText().toString();
                String str_email = email.getText().toString();
                String str_password = password.getText().toString();

                if(TextUtils.isEmpty(str_username) || TextUtils.isEmpty(str_name) || TextUtils.isEmpty(str_email) || TextUtils.isEmpty(str_password) ){
                    Toast.makeText(Register.this,"All fileds are required!!",Toast.LENGTH_SHORT).show();
                }
                else {
                      register(str_username,str_name,str_email,str_password);

                }

            }
        });
    }

    private void register(String username, String name, String email, String password){
        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser firebaseUser= auth.getCurrentUser();
                            String userid = firebaseUser.getUid();


                            reference = FirebaseDatabase.getInstance().getReference().child("Users").child(userid);

                            HashMap<String, Object> hashmap = new HashMap<>();
                            hashmap.put("id",userid);
                            hashmap.put("username",username.toLowerCase());
                            hashmap.put("fullname",name);
                            hashmap.put("bio","");
                            hashmap.put("imageurl","https://firebasestorage.googleapis.com/v0/b/meet-us-1313.appspot.com/o/anarchy-android-computer-cyber-wallpaper-preview.png?alt=media&token=a2bb6a9f-f418-4f6b-9184-7f856cc7a67c");

                            reference.setValue(hashmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        pd.dismiss();
                                        Intent intent = new Intent(Register.this,MainActivity2.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                }
                            });

                        }
                        else{
                            pd.dismiss();
                            Toast.makeText(Register.this,"You can't register with this email or password..",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}