package com.app.arthasattva.Interface;

public interface OnCommentSelection {
    public void OnGifSelection(String gif_url);

    public void OnReplySend(boolean shouldReply,String username,String comment_id);
}
