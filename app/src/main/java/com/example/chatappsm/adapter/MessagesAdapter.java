package com.example.chatappsm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatappsm.R;
import com.example.chatappsm.model.Messages;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class MessagesAdapter extends RecyclerView.Adapter {

    Context context;
    List<Messages> messages;

    int ITEM_SEND = 1;
    int ITEM_RECEIVE = 2;

    public MessagesAdapter(Context context, List<Messages> messages) {
        this.context = context;
        this.messages = messages;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == ITEM_SEND) {
            View view = LayoutInflater.from(context).inflate(R.layout.send_chat_layout, parent, false);
            return new SenderViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(context).inflate(R.layout.receive_chat_layout, parent, false);
            return new ReceiverViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Messages message = messages.get(position);
        if(holder.getClass() == SenderViewHolder.class) {
            SenderViewHolder viewHolder = (SenderViewHolder) holder;
            viewHolder.tvMessage.setText(message.getMessage());
            viewHolder.timeOfMessage.setText(message.getCurrenttime());
        }
        else {
            ReceiverViewHolder viewHolder = (ReceiverViewHolder) holder;
            viewHolder.tvMessage.setText(message.getMessage());
            viewHolder.timeOfMessage.setText(message.getCurrenttime());
        }
    }

    @Override
    public int getItemViewType(int position) {
        Messages message = messages.get(position);
        if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(message.getSenderId())) {
            return ITEM_SEND;
        }
        else {
            return ITEM_RECEIVE;
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class SenderViewHolder extends RecyclerView.ViewHolder {

        TextView tvMessage, timeOfMessage;

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);

            tvMessage = itemView.findViewById(R.id.send_message);
            timeOfMessage = itemView.findViewById(R.id.time_message_send);
        }
    }

    public class ReceiverViewHolder extends RecyclerView.ViewHolder {

        TextView tvMessage, timeOfMessage;

        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);

            tvMessage = itemView.findViewById(R.id.receive_message);
            timeOfMessage = itemView.findViewById(R.id.time_message_receive);
        }
    }
}

