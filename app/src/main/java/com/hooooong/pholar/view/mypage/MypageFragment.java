package com.hooooong.pholar.view.mypage;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hooooong.pholar.R;
import com.hooooong.pholar.model.PostThumbnail;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class MypageFragment extends Fragment {
    private final String TAG = getClass().getSimpleName();
    MypageAdapter adapter;
    public MypageFragment() {
        // Required empty public constructor
    }

    public static MypageFragment newInstance(String name, String path) {
        MypageFragment f = new MypageFragment();
        Bundle args = new Bundle();
        args.putString("name", name);
        args.putString("path", path);
        f.setArguments(args);
        return f;
    }
    String name, path;
    public void readBundle(Bundle bundle) {
        if(bundle != null) {
            name = bundle.getString("name");
            path = bundle.getString("path");
        }
    }

    Context context;
    RecyclerView rv;
    CircleImageView civ_profile;
    TextView tv_name;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // instantiate view
        View view = inflater.inflate(R.layout.fragment_mypage, container, false);
        context = container.getContext();
        // setting name and path
        readBundle(getArguments());

        // profile setting
        civ_profile = view.findViewById(R.id.civ_profile);
        if(path != null)
            Glide.with(this).load(Uri.parse(path)).into(civ_profile);

        tv_name = view.findViewById(R.id.tv_name);
        if(name != null)
            tv_name.setText(name);

        // recyclerview
        rv = view.findViewById(R.id.post_recycler_view);
        adapter = new MypageAdapter(container.getContext());
        rv.setAdapter(adapter);
        if(map!=null && map.size() != 0)
            rv.setLayoutManager(new GridLayoutManager(context, 3));
        else
            rv.setLayoutManager(new LinearLayoutManager(context));

        return view;
    }

    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference userRef;

    Map<String, PostThumbnail> map;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getPostThumbnail();
        super.onActivityCreated(savedInstanceState);
    }

    private void getPostThumbnail() {
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        userRef = database.getReference("user");

        //TODO child 자리에 user uid 값 들어가야 한다!
        userRef.child("xWxHOdz9FcM4I3sbCKf5ubj5pWW2").child("post_thumbnail").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                map = new HashMap<>();
                for(DataSnapshot item : dataSnapshot.getChildren()) {
                    PostThumbnail thumbnail = new PostThumbnail();
                    thumbnail.first_pic_path = (String) item.child("first_pic_path").getValue();
                    thumbnail.post_id = item.getKey();
                    thumbnail.count_picture = (String) item.child("count_picture").getValue();
                    map.put(thumbnail.post_id, thumbnail);
                }
                if(map.size() != 0) {
                    rv.setLayoutManager(new GridLayoutManager(context, 3));
                }
                adapter.dataRefreshing(map);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}