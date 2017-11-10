package com.hooooong.pholar.view.home;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hooooong.pholar.R;
import com.hooooong.pholar.util.BottomNavigationViewHelper;
import com.hooooong.pholar.view.gallery.GalleryActivity;
import com.hooooong.pholar.view.list.ListFragment;
import com.hooooong.pholar.view.mypage.MypageFragment;

public class HomeActivity extends AppCompatActivity {

    private BottomNavigationView navigation;

    FirebaseUser fUser;
    DatabaseReference userRef;
    String name, path;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        userRef = FirebaseDatabase.getInstance().getReference("user");
        userRef.child(fUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot item : dataSnapshot.getChildren()) {
                    if("nickname".equals(item.getKey())) {
                        name = (String)item.getValue();
                    }
                    if("profile_path".equals(item.getKey())) {
                        path = (String)item.getValue();
                    }

                }
                Log.d("here is", name + "/" + path);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void initView() {
        navigation = findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        getSupportFragmentManager().beginTransaction().add(R.id.frameLayout, new ListFragment()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frameLayout, new ListFragment())
                            .commit();
                    return true;
                case R.id.navigation_write:
                    Intent intent = new Intent(HomeActivity.this, GalleryActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_notification:
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frameLayout, new NotiFragment())
                            .commit();
                    return true;
                case R.id.navigation_mypage:
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frameLayout, MypageFragment.newInstance(name,path))
                            .commit();
                    return true;
            }
            return false;
        }
    };

}