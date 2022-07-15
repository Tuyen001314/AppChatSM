package com.example.chatappsm.model;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.Exclude;

public class MessagesId {

    @Exclude
    public String MessagesId;

    public <T extends MessagesId> T WithId (@NonNull final String id) {
        this.MessagesId = id;
        return (T) this;
    }
}
