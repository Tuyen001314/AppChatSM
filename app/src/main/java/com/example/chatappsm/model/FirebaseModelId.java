package com.example.chatappsm.model;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.Exclude;

public class FirebaseModelId {
    @Exclude
    public String FirebaseModelId;

    public <T extends FirebaseModelId> T WithId (@NonNull final String id) {
        this.FirebaseModelId = id;
        return (T) this;
    }
}
