package com.hooooong.pholar;

import android.*;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.hooooong.pholar.view.gallery.BaseActivity;
import com.hooooong.pholar.view.home.HomeActivity;

import java.io.File;
import java.io.IOException;

public class SignupActivity extends BaseActivity{

    private RelativeLayout layoutTop;
    private RelativeLayout layoutBottom;
    private TextView titleProfile;
    private EditText txtNickname;
    private Button btnCommit;
    private ImageView imgProfile;

    private FirebaseUser user;
    private Uri profileUri;


    public SignupActivity() {
        super(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE});
    }

    /*@Override
    public void init() {
        setContentView(R.layout.activity_main);
        imgProfile = (ImageView) findViewById(R.id.imgProfile);
    }*/

    @Override
    public void init() {
        setContentView(R.layout.activity_signup);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        initView();
        setListener();
    }

/*    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        initView();
        setListener();
    }*/

    private void initView() {
        layoutTop = findViewById(R.id.layoutTop);
        layoutBottom = findViewById(R.id.layoutBottom);
        titleProfile = findViewById(R.id.titleProfile);
        txtNickname = findViewById(R.id.txtNickname);
        btnCommit = findViewById(R.id.btnCommit);
        imgProfile = findViewById(R.id.imgProfile);
    }

    private void setListener(){
        btnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                        .setDisplayName(txtNickname.getText().toString())
                        .setPhotoUri(profileUri)
                        .build();

                // 추가 데이터베이스 관리
                user.updateProfile(profile);

                Intent intent = new Intent(v.getContext(), HomeActivity.class);
                startActivity(intent);
            }
        });
    }

    // 프로필 이미지 업로드

    private static final int REQ_GALLERY = 333;

    public void clickedImageProfile(View view) {
        Log.e("Test", "==================clickedImageProfile: " );
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQ_GALLERY);
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            Uri imageUri = null;
            switch (requestCode) {
                case REQ_GALLERY:
                    if(data != null) {
                        imageUri = data.getData();
                        imgProfile.setImageURI(imageUri);
                    }
                    break;
            }
        }
    }
}