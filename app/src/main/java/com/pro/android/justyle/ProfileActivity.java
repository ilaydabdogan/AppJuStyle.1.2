package com.pro.android.justyle;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";


    private TextView mTextViewNickname;
    private Button mButtonLogOut;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseFirestore mFirebaseFirestore;
    private EditText mEditName;
    private EditText mEditAddress;
    private Button mButtonAddInfo;
    private FirebaseUser mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mTextViewNickname = (TextView) findViewById(R.id.Nickname);
        mButtonLogOut = (Button) findViewById(R.id.LogOut);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mCurrentUser = mFirebaseAuth.getCurrentUser();



        if (mFirebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
        mFirebaseFirestore = FirebaseFirestore.getInstance();
        mEditName = (EditText) findViewById(R.id.EditName);
        mEditAddress = (EditText) findViewById(R.id.EditAddress);
        mButtonAddInfo =(Button) findViewById(R.id.addInfo);
        mFirebaseFirestore = FirebaseFirestore.getInstance();


            mTextViewNickname.setText(mCurrentUser.getEmail());

        mButtonLogOut.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mFirebaseAuth.signOut();
                finish();
                startActivity(new Intent(ProfileActivity.this, LoginActivity.class));

            }
        });
        mButtonAddInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserInformation();
            }
        });

    }
    private void saveUserInformation(){
        Map<String, Object> user = new HashMap<>();

        String name = mEditName.getText().toString().trim();
        String address = mEditAddress.getText().toString().trim();
        user.put("name", name);
        user.put("address", address);

        mFirebaseFirestore.collection("users").document(mCurrentUser.getUid()).set(user);
    }
}
