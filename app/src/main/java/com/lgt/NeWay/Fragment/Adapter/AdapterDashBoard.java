package com.lgt.NeWay.Fragment.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.api.Batch;
import com.lgt.NeWay.Fragment.BatchListFragment;
import com.lgt.NeWay.Fragment.Model.ModelDashBoard;
import com.lgt.NeWay.Neway.R;
import com.lgt.NeWay.activity.BoardList.BaordList;
import com.lgt.NeWay.activity.ClassList.ClassList;
import com.lgt.NeWay.activity.JobList.Jobs;
import com.lgt.NeWay.activity.SubjectList.SubjectList;
import com.lgt.NeWay.activity.TeacherList.TeacherList;
import com.lgt.NeWay.activity.UserBatchRequest.UserBatchRequest;

import java.util.List;

public class AdapterDashBoard extends RecyclerView.Adapter<AdapterDashBoard.Cityholder> {
    List<ModelDashBoard> modelDashBoardList;
    Context context;

    public AdapterDashBoard(List<ModelDashBoard> modelDashBoardList, Context context) {
        this.modelDashBoardList = modelDashBoardList;
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterDashBoard.Cityholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dashboardtabs, parent, false);
        return new Cityholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterDashBoard.Cityholder holder, int position) {

        String title_id = modelDashBoardList.get(position).getId();

        holder.tv_countoflistitem.setText(modelDashBoardList.get(position).getTv_countoflistitem());
        holder.list.setText(modelDashBoardList.get(position).getList());
        Glide.with(context).load(modelDashBoardList.get(position).getIv_dash_listimage()).into(holder.iv_dash_listimage);


        holder.rv_Dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (title_id.equals("0")) {
                    Intent intent = new Intent(context, BaordList.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
                if (title_id.equals("1")) {
                    Intent intent = new Intent(context, SubjectList.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
                if (title_id.equals("2")) {
                    Intent intent = new Intent(context, ClassList.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
                if (title_id.equals("3")) {
                    Intent intent = new Intent(context, Jobs.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
                if (title_id.equals("4")) {
                    Intent intent = new Intent(context, TeacherList.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
                if (title_id.equals("5")) {
//                    FragmentManager manager = ((AppCompatActivity) context).getSupportFragmentManager();
//                    FragmentTransaction fragmentTransaction = manager.beginTransaction();
//                    fragmentTransaction.replace(R.id.FragmentDashboard, new BatchListFragment());

                    context.sendBroadcast(new Intent("start.fragment.action"));

                }
                if (title_id.equals("6")) {
                    Intent intent = new Intent(context, UserBatchRequest.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            }
        });


        if (title_id.equals("4")) {
            holder.iv_dash_listimage.setColorFilter(ContextCompat.getColor(context, R.color.black), android.graphics.PorterDuff.Mode.MULTIPLY);
        }


    }

    @Override
    public int getItemCount() {
        return modelDashBoardList.size();
    }

    public class Cityholder extends RecyclerView.ViewHolder {
        RelativeLayout rv_Dashboard;

        ImageView iv_dash_listimage, iv_dashmore;
        TextView tv_countoflistitem, list, tv_more;


        public Cityholder(@NonNull View itemView) {
            super(itemView);

            rv_Dashboard = itemView.findViewById(R.id.rv_Dashboard);
            iv_dash_listimage = itemView.findViewById(R.id.iv_dash_listimage);
            iv_dashmore = itemView.findViewById(R.id.iv_dashmore);
            tv_countoflistitem = itemView.findViewById(R.id.tv_countoflistitem);
            list = itemView.findViewById(R.id.list);
            tv_more = itemView.findViewById(R.id.tv_more);
        }
    }
}
