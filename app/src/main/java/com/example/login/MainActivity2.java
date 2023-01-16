package com.example.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.login.fragment.nav_home;
import com.example.login.fragment.nav_search;
import com.example.login.fragment.notification;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity2 extends AppCompatActivity {
 Fragment selectedFragment = null;
 BottomNavigationView bottomNavigationView;
 ImageView profile;


 ImageButton add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        add = findViewById(R.id.add);
        profile=findViewById(R.id.profile);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity2.this, Postit.class);
                startActivity(intent);
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Profile.class);
                startActivity(intent);
            }
        });
        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        Bundle intent = getIntent().getExtras();
        if(intent!=null){
//            String publisher = intent.getString("publisherid");
//
//
//            SharedPreferences.Editor editor = getSharedPreferences("PREFS",MODE_PRIVATE).edit();
//            editor.putString("profileid",publisher);
//
//            editor.apply();

//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new profile()).commit();



        }
        else
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new nav_home()).commit();
        }







    }
    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            menuitem -> {


                switch (menuitem.getItemId()){

                    case R.id.home:
                        selectedFragment= new nav_home();
                        break;

                    case R.id.search:
                        selectedFragment = new nav_search();
                        break;

//                    case  R.id.profile:
//////                     flag=1;
////                     Intent intent = new Intent(getApplicationContext(),Profile.class);
////                     startActivity(intent);
//                        break;

                    case R.id.notification:
                        selectedFragment = new notification();
                        break;

                }
//
//                if(selectedFragment == null){
//                    selectedFragment = new nav_home();
//                }
                       getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();

//

                return true;
            };
}