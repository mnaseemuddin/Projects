package com.app.cryptok.go_live_module;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.cryptok.R;
import com.app.cryptok.adapter.CustomHolder;

import java.util.ArrayList;

public class LiveChatAdapter extends RecyclerView.Adapter<LiveChatAdapter.LiveChatHolder> {
    ArrayList<String> messageList;
    Context context;

    public LiveChatAdapter(ArrayList<String> messageList, Context context) {
        this.messageList = messageList;
        this.context = context;
    }

    @NonNull
    @Override
    public LiveChatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.inflate_live_chat,parent,false);
        return new LiveChatHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LiveChatHolder holder, int position) {
        try {
            holder.tv_live_message.setText(messageList.get(position));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class LiveChatHolder extends CustomHolder {
     TextView tv_live_message;
        public LiveChatHolder(@NonNull View itemView) {
            super(itemView);
            tv_live_message=find(R.id.tv_live_message);
        }
    }
}
