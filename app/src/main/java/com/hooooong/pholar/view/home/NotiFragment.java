package com.hooooong.pholar.view.home;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.hooooong.pholar.R;
import com.hooooong.pholar.view.sign.SigninActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class NotiFragment extends Fragment {
    private final String TAG = getClass().getSimpleName();

    public NotiFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_noti, container, false);

        view.findViewById(R.id.noti).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(v.getContext(), SigninActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

}
