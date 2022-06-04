package com.lgt.also_food_court_userApp.Adapters;

import android.content.Context;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lgt.also_food_court_userApp.Models.ModelSupport;
import com.lgt.also_food_court_userApp.R;


import java.util.List;



public class AdapterSupport extends RecyclerView.Adapter {

    List<ModelSupport> list;
    Context context;

    public AdapterSupport(List<ModelSupport> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        switch (list.get(position).getContent_type()) {
            case 0:
                return ModelSupport.MESSAGE_RECEIVED;

            case 1:
                return ModelSupport.MESSAGE_SENT;


        }
        return -1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case ModelSupport.MESSAGE_RECEIVED: {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_received, parent, false);
                return new HolderMessageReceived(view);
            }


            case ModelSupport.MESSAGE_SENT: {
                View viewMsgSent = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_sent_new, parent, false);
                //View viewMsgSent = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_sent, parent, false);
                return new HolderMessageSent(viewMsgSent);
            }

        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        switch (list.get(position).getContent_type()) {

            //0 : received ----------- 1: sent ------------ 2: Image

            case ModelSupport.MESSAGE_RECEIVED:
                ((HolderMessageReceived) holder).tv_ReceivedMessage.setText(list.get(position).getChatMessage() + "");
                ((HolderMessageReceived) holder).tvNameMsgReceived.setText(list.get(position).getSenderName());
                ((HolderMessageReceived) holder).tvChatMsgReceivedTime.setText(list.get(position).getMessageTime());

                break;

            case ModelSupport.MESSAGE_SENT:
                Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
                ((HolderMessageSent) holder).tvMsgSent.setText(list.get(position).getChatMessage());
                ((HolderMessageSent) holder).tvMsgSent.setMaxWidth((int) (display.getWidth() / 1.5));

                ((HolderMessageSent) holder).tvChatSentTime.setText(list.get(position).getMessageTime());
                ((HolderMessageSent) holder).tvNameSender.setText(list.get(position).getSenderName());

                break;

        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class HolderMessageReceived extends RecyclerView.ViewHolder {

        TextView tv_ReceivedMessage,tvNameMsgReceived,tvChatMsgReceivedTime;

        public HolderMessageReceived(@NonNull View itemView) {
            super(itemView);
            tv_ReceivedMessage = itemView.findViewById(R.id.tv_ReceivedMessage);
            tvNameMsgReceived = itemView.findViewById(R.id.tvNameMsgReceived);
            tvChatMsgReceivedTime = itemView.findViewById(R.id.tvChatMsgReceivedTime);

        }
    }


    public static class HolderMessageSent extends RecyclerView.ViewHolder {
        TextView tvMsgSent, tvNameSender, tvChatSentTime;
        private LinearLayout ll_msg_sent;


        public HolderMessageSent(@NonNull View itemView) {
            super(itemView);
            tvMsgSent = itemView.findViewById(R.id.tv_msg_sent);
            tvNameSender = itemView.findViewById(R.id.tvNameSender);
            tvChatSentTime = itemView.findViewById(R.id.tvChatSentTime);

/*
            ll_msg_sent = itemView.findViewById(R.id.ll_msg_sent);
*/
        }
    }

   /* public static class HolderImage extends RecyclerView.ViewHolder {
        public HolderImage(@NonNull View itemView) {
            super(itemView);
        }
    }*/

}
