package com.daniel.appdaniel.activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.daniel.appdaniel.R;
import com.daniel.appdaniel.fragments.ChatFragment;
import com.daniel.appdaniel.fragments.FiltersFragment;
import com.daniel.appdaniel.fragments.HomeFragment;
import com.daniel.appdaniel.fragments.ProfileFragment;
import com.daniel.appdaniel.providers.AuthProvider;
import com.daniel.appdaniel.providers.TokenProvider;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigation;
    TokenProvider mTokenProvider;
    AuthProvider mAuthProvider;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        openFragment(new HomeFragment());
        mTokenProvider = new TokenProvider();
        mAuthProvider = new AuthProvider();
        createToken();
    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.itemhome)
                {
                    openFragment(new HomeFragment());
                }
                else if (item.getItemId() == R.id.itemChat)
                {
                    openFragment(new ChatFragment());
                }
                else if (item.getItemId() == R.id.itemFilters)
                {
                    openFragment(new FiltersFragment());
                }
                else if (item.getItemId() == R.id.itemProfile)
                {
                    openFragment(new ProfileFragment());
                }
                return true;
                }
            };
    private void createToken()
    {
        mTokenProvider.create(mAuthProvider.getUid());
    }

}