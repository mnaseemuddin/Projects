package com.app.cryptok.view_live_module;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.cryptok.R;

public class StreamCommentsHolder extends RecyclerView.ViewHolder {


    public TextView tv_comment_username;
    public TextView tv_comment,tv_user_level;

    public StreamCommentsHolder(@NonNull View itemView) {
        super(itemView);

        tv_comment_username=itemView.findViewById(R.id.tv_comment_username);
        tv_comment=itemView.findViewById(R.id.tv_comment);
        tv_user_level=itemView.findViewById(R.id.tv_user_level);
    }
}
