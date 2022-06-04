package com.app.arthasattva.model;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.Exclude;

public class PostId {
    @Exclude
    public String post_id;

    public <T extends PostsModel> T withId(@NonNull final String id){
        this.post_id=id;
        return (T) this;
    }
}
