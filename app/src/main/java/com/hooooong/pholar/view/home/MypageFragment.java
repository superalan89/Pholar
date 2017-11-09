package com.hooooong.pholar.view.home;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hooooong.pholar.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MypageFragment extends Fragment {
    private final String TAG = getClass().getSimpleName();

    public MypageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mypage, container, false);
    }

}
