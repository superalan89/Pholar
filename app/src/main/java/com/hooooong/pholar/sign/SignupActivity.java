package com.hooooong.pholar.sign;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hooooong.pholar.R;
import com.hooooong.pholar.model.User;
import com.hooooong.pholar.view.gallery.BaseActivity;
import com.hooooong.pholar.view.home.HomeActivity;

import java.lang.reflect.Field;

public class SignupActivity extends BaseActivity{

    private RelativeLayout layoutTop;
    private RelativeLayout layoutBottom;
    private TextView titleProfile;
    private EditText txtNickname;
    private Button btnCommit;
    private ImageView imgProfile;

    private DatabaseReference userRef;
    private Uri profileUri;


    public SignupActivity() {
        super(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE});
    }

    /*@Override
    public void init() {
        setContentView(R.layout.activity_main);
        imgProfile = (ImageView) findViewById(R.id.imgProfile);
    }*/
    FirebaseAuth mAuth;
    FirebaseUser fUser;
    @Override
    public void init() {
        setContentView(com.hooooong.pholar.R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();
        fUser = mAuth.getCurrentUser();

//        Toast.makeText(this, user.getEmail() + "=email "+user.getDisplayName(),Toast.LENGTH_LONG).show();

        // email, phonenumber, token, profile_path
        userRef = FirebaseDatabase.getInstance().getReference("user");
        userRef.child(fUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String nickname = "";
                for(DataSnapshot item : dataSnapshot.getChildren()) {
                    if("nickname".equals(item.getKey())) {
                        nickname = (String) item.getValue();
                    }
                }

                if(!"".equals(nickname)) {
                    Intent intent = new Intent(getBaseContext(), HomeActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getBaseContext(), "닉네임을 입력해주세요.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

//        FirebaseAuth mAuth = FirebaseAuth.getInstance();
//        user = mAuth.getCurrentUser();
//        Log.d("nick", user.getDisplayName());
//        if(user.getDisplayName() != null && user.getPhotoUrl() != null) {
//            Log.d("hhhhhhhhhhhhhhhhhh", "=============================");
//            Intent intent = new Intent(getBaseContext(), HomeActivity.class);
//            startActivity(intent);
//            finish();
//        }

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
                String nickname = txtNickname.getText().toString();
                UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                        .setDisplayName(nickname)
                        .setPhotoUri(profileUri)
                        .build();

                // 추가 데이터베이스 관리
                fUser.updateProfile(profile);

                User user = new User();
                user.nickname = nickname;
                user.profile_path = profileUri.toString();
                userRef.child(fUser.getUid()).setValue(user);

                Intent intent = new Intent(v.getContext(), HomeActivity.class);
                startActivity(intent);
                finish();
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
            switch (requestCode) {
                case REQ_GALLERY:
                    if(data != null) {
                        profileUri = data.getData();
                        imgProfile.setImageURI(profileUri);
                    }
                    break;
            }
        }
    }
}