package com.example.chatappsm.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.chatappsm.R;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private EditText mGetPhoneNumberEdt;
    private Button mSendOtpBtn;
    private CountryCodePicker mCountryCodePicker;
    private String countryCode;
    private String phoneNumber;
    private FirebaseAuth firebaseAuth;
    private ProgressBar mProgressBarOfMain;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String codeSent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCountryCodePicker = findViewById(R.id.country_code_picker);
        mSendOtpBtn = findViewById(R.id.send_otp_button);
        mGetPhoneNumberEdt = findViewById(R.id.get_phone_nummber);
        mProgressBarOfMain = findViewById(R.id.progressbar_main);

        firebaseAuth = FirebaseAuth.getInstance();
        countryCode = mCountryCodePicker.getSelectedCountryCodeWithPlus();
        mCountryCodePicker.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                countryCode = mCountryCodePicker.getSelectedCountryCodeWithPlus();
            }
        });

        mSendOtpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number;
                number = mGetPhoneNumberEdt.getText().toString();
                if(number.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please Enter Your Number", Toast.LENGTH_SHORT).show();
                }
                else if(number.length() < 10) {
                    Toast.makeText(getApplicationContext(), "Please Enter Correct Number", Toast.LENGTH_SHORT).show();
                }
                else {
                    mProgressBarOfMain.setVisibility(View.VISIBLE);
                    phoneNumber = countryCode + number;


                    PhoneAuthOptions options = PhoneAuthOptions.newBuilder(firebaseAuth)
                            .setPhoneNumber(phoneNumber)
                            .setTimeout(60L, TimeUnit.SECONDS)
                            .setActivity(MainActivity.this)
                            .setCallbacks(mCallbacks)
                            .build();

                    PhoneAuthProvider.verifyPhoneNumber(options);

                }
            }
        });

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                Toast.makeText(getApplicationContext(), "OTP is Sent", Toast.LENGTH_SHORT).show();
                mProgressBarOfMain.setVisibility(View.INVISIBLE);
                codeSent = s;

                Intent intent = new Intent(MainActivity.this, OtpAuthentication.class);
                intent.putExtra("otp", codeSent);
                startActivity(intent);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

            }
        };

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            Intent intent = new Intent(MainActivity.this, ChatActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }
}