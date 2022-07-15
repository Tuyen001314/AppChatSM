package com.example.chatappsm.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatappsm.R;
import com.example.chatappsm.activity.SpecificChat;
import com.example.chatappsm.model.FirebaseModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.NoteViewHolder> {

    List<FirebaseModel> listUser;

    public ChatAdapter(List<FirebaseModel> listUser) {
        this.listUser = listUser;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_chat_layout, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Picasso.get().load(Uri.parse(listUser.get(position).getImage())).into(holder.imageView);
        holder.nameOfUser.setText(listUser.get(position).getName());
        holder.statusOfUser.setText(listUser.get(position).getStatus());
        if(listUser.get(position).getStatus().equals("Online")) {
            holder.statusOfUser.setTextColor(Color.GREEN);
        }
        else {
            holder.statusOfUser.setTextColor(Color.GRAY);
        }

        FirebaseModel firebaseModel = listUser.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SpecificChat.class);
                intent.putExtra("name", firebaseModel.getName());
                intent.putExtra("receiveruid", firebaseModel.getUid());
                intent.putExtra("imageuri", firebaseModel.getImage());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listUser.size();
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView nameOfUser, statusOfUser;
        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image_user_mess);
            nameOfUser = itemView.findViewById(R.id.nameOfUser);
            statusOfUser = itemView.findViewById(R.id.statusOfUser);

        }
    }
}

