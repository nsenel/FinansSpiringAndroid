package com.example.numan.finans;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;
import com.example.numan.finans.frags.Kurlar;
import com.example.numan.finans.frags.Satis;
import com.example.numan.finans.frags.gecmisIslemler;


public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    FragmentManager fm;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                   moveTo(new Kurlar(),false);
                    return true;
                case R.id.navigation_dashboard:
                    moveTo(new Satis(),false);
                    return true;
                case R.id.navigation_notifications:
                    moveTo(new gecmisIslemler(),false);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        fm = getSupportFragmentManager();
        navigation.setSelectedItemId(R.id.navigation_home);
    }
    public void moveTo(Fragment f, boolean addToStack)
    {
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.content, f);
        if (addToStack)
            ft.addToBackStack("curr_frag");
        ft.commit();
    }

}
