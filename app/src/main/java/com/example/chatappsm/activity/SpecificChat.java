package com.example.chatappsm.activity;

import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatappsm.R;
import com.example.chatappsm.adapter.MessagesAdapter;
import com.example.chatappsm.model.Messages;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentChange;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SpecificChat extends AppCompatActivity {

    private EditText mGetMessage;
    private ImageButton mSendMessageBtn, mBackMessageBtn;
    private CardView mSendMessageCardview;
    private Toolbar mToolbarOfSpecifiChat;
    private ImageView mImageviewOfSpecifiChat;
    private TextView mNameOfSpecifiChat;
    private String enterdMessage, mReceiverName, mReceiverUid, mSenderUid, senderRoom, receiverRoom, currentTime;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private RecyclerView recyclerViewMessage;
    private Calendar calendar;
    private Intent intent;
    private SimpleDateFormat simpleDateFormat;
    private MessagesAdapter messagesAdapter;
    private List<Messages> messagesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_chat);

        mGetMessage = findViewById(R.id.edt_chat);
        mSendMessageBtn = findViewById(R.id.image_btn_chat);
        mBackMessageBtn = findViewById(R.id.image_btn_back_chat);
        mSendMessageCardview = findViewById(R.id.cardview_chat);
        mToolbarOfSpecifiChat = findViewById(R.id.toolbar_chat);
        mImageviewOfSpecifiChat = findViewById(R.id.image_user_chat);
        mNameOfSpecifiChat = findViewById(R.id.tv_username_chat);

       // setSupportActionBar(mToolbarOfSpecifiChat);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        messagesList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerViewMessage = findViewById(R.id.recycle_chat);
        recyclerViewMessage.setLayoutManager(linearLayoutManager);


        intent = getIntent();
        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("hh:mm a");
        mSenderUid = firebaseAuth.getUid();
        mReceiverUid = getIntent().getStringExtra("receiveruid");
        mReceiverName = getIntent().getStringExtra("name");
        senderRoom = mSenderUid + mReceiverUid;
        receiverRoom = mReceiverUid + mSenderUid;

        DatabaseReference databaseReference = firebaseDatabase.getReference().child("chats").child(senderRoom)
                        .child("messages");
        messagesAdapter = new MessagesAdapter(SpecificChat.this, messagesList);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagesList.clear();
                for (DataSnapshot snapshot1: snapshot.getChildren()) {
                    Messages messages = snapshot1.getValue(Messages.class);
                    messagesList.add(messages);
                }
                messagesAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        recyclerViewMessage.setAdapter(messagesAdapter);

        mBackMessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mNameOfSpecifiChat.setText(mReceiverName);

        String uri = intent.getStringExtra("imageuri");
        if(uri.isEmpty()) {
            Toast.makeText(getApplicationContext(), "null is received", Toast.LENGTH_SHORT).show();
        }
        else {
            Picasso.get().load(uri).into(mImageviewOfSpecifiChat);
        }

        mSendMessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterdMessage = mGetMessage.getText().toString();
                if(enterdMessage.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Enter meassage first", Toast.LENGTH_SHORT).show();
                }
                else {
                    Date date = new Date();
                    currentTime = simpleDateFormat.format(calendar.getTime());
                    Messages message = new Messages(enterdMessage, firebaseAuth.getUid(), date.getTime(), currentTime);
                    firebaseDatabase = FirebaseDatabase.getInstance();
                    firebaseDatabase.getReference().child("chats")
                            .child(senderRoom)
                            .child("messages")
                            .push().setValue(message).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    firebaseDatabase.getReference()
                                            .child("chats")
                                            .child(receiverRoom)
                                            .child("messages")
                                            .push()
                                            .setValue(message).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                }
                                            });
                                    }
                            });
                    mGetMessage.setText(null);
                }
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        messagesAdapter.notifyDataSetChanged();


    }

    @Override
    protected void onStop() {
        super.onStop();
        if(messagesAdapter != null) {
            messagesAdapter.notifyDataSetChanged();
        }
    }
}