package com.app.cryptok.view_live_module;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.cryptok.R;
import com.app.cryptok.model.StreamCommentModel;

import java.util.List;

public class StreamCommentsAdapter extends RecyclerView.Adapter<StreamCommentsAdapter.ViewHolder> {

    private List<StreamCommentModel>list;
    private Context context;

    public StreamCommentsAdapter(List<StreamCommentModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public StreamCommentsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View textView = LayoutInflater.from(context).inflate(R.layout.live_comments_layout,parent, false);
        return new ViewHolder(textView);
    }

    @Override
    public void onBindViewHolder(@NonNull StreamCommentsAdapter.ViewHolder holder, int position) {

        StreamCommentModel model=list.get(position);

        holder.tv_comment.setText(model.getStream_comment());
        holder.tv_comment_username.setText(model.getStream_comment_user_name());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_comment_username;
        public TextView tv_comment;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_comment_username=itemView.findViewById(R.id.tv_comment_username);
            tv_comment=itemView.findViewById(R.id.tv_comment);

        }
    }
}
