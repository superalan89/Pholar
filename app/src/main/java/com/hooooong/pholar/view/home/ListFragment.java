package com.hooooong.pholar.view.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hooooong.pholar.R;

/**
 * Created by Android Hong on 2017-11-06.
 */

public class ListFragment extends android.support.v4.app.Fragment {

    public ListFragment() {
        // Required empty public constructor
    }

    // Fragment 의 경우 onCreate 에서 View 의 작업을 하면 안되고
    // onCreateView 에서 View 의 작업을 해줘야 한다.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false);
    }
}