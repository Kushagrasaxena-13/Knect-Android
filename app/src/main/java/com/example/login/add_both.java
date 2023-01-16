package com.example.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.example.login.fragment.PostFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class add_both extends AppCompatActivity {
    Fragment selectedFragment = null;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_both);

        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

//        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new BannerFragment()).commit();
//


//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new BannerFragment()).commit();


    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            menuitem -> {


                switch (menuitem.getItemId()){

//                    case R.id.banner:
//                        selectedFragment= new BannerFragment();
//                        break;

                    case R.id.post:
                        selectedFragment = new PostFragment();
                        break;


                }

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();



                return true;
            };

}