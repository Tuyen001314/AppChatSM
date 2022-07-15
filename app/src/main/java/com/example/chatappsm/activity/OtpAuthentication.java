package com.example.chatappsm.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatappsm.R;
import com.example.chatappsm.model.FirebaseModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class OtpAuthentication extends AppCompatActivity {

    private TextView mChangeNumberTv;
    private EditText mGetOtpEdt;
    private Button mVerifyOtpBtn;
    private String enteredOtp;
    private FirebaseFirestore firestore;
    private ListenerRegistration listenerRegistration;
    private FirebaseAuth firebaseAuth;
    private ProgressBar mProgressbarOfOtpAuth;
    private List<FirebaseModel> list;
    private int CHECK = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_authentication);

        mChangeNumberTv = findViewById(R.id.change_number);
        mGetOtpEdt = findViewById(R.id.get_opt);
        mVerifyOtpBtn = findViewById(R.id.verify_otp);
        mProgressbarOfOtpAuth = findViewById(R.id.progressbar_of_otp_auth);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        mChangeNumberTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OtpAuthentication.this, MainActivity.class));
            }
        });

        mVerifyOtpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enteredOtp = mGetOtpEdt.getText().toString();
                if(enteredOtp.isEmpty()) {
                    Toast.makeText(OtpAuthentication.this, "Enter Your OTP First", Toast.LENGTH_SHORT).show();
                }
                else {
                    mProgressbarOfOtpAuth.setVisibility(View.VISIBLE);
                    String codeReceived = getIntent().getStringExtra("otp");
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeReceived, enteredOtp);
                    signInWithPhoneAuthCredential(credential);
                }
            }
        });

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    mProgressbarOfOtpAuth.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "Login Success", Toast.LENGTH_SHORT).show();
                    //String user = task.getResult().getUser().getUid();
                    if(firestore.collection("Users").whereEqualTo("uid", firebaseAuth.getUid()) != null) {
                        startActivity(new Intent(OtpAuthentication.this, ChatActivity.class));
                        finish();
                    }
                    else {
                        startActivity(new Intent(OtpAuthentication.this, SetProfile.class));
                        finish();
                    }
                }
                else {
                    if(task.getException() instanceof FirebaseAuthInvalidCredentialsException);
                    mProgressbarOfOtpAuth.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}