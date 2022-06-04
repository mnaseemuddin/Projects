package com.lgt.Ace11.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lgt.Ace11.Bean.BeanHomeLive;
import com.lgt.Ace11.Config;
import com.lgt.Ace11.FragmentBottomMenu.MyContestFragment;
import com.lgt.Ace11.HomeActivity;
import com.lgt.Ace11.R;

import java.util.List;

import static com.lgt.Ace11.HomeActivity.custom_view_pager_container;

public class ResultMatchAdapter extends RecyclerView.Adapter<ResultMatchAdapter.MatchHolder> {

    Context mContext;
    List<BeanHomeLive> beanHome;

    public ResultMatchAdapter(Context mContext, List<BeanHomeLive> bean) {
        this.mContext = mContext;
        this.beanHome = bean;
    }

    @NonNull
    @Override
    public MatchHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.result_matchs_layout, parent, false);
        return new MatchHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull MatchHolder holder, int position) {
        final String match_id = beanHome.get(position).getMatch_id();
        String teamid1 = beanHome.get(position).getTeamid1();
        String match_status = beanHome.get(position).getMatch_status();
        String title = beanHome.get(position).getTitle();
        String type = beanHome.get(position).getType();
        final int time = beanHome.get(position).getTime();
        String teamid2 = beanHome.get(position).getTeamid2();
        String team_name1 = beanHome.get(position).getTeam_name1();
        final String team_image1 = beanHome.get(position).getTeam_image1();
        final String team_short_name1 = beanHome.get(position).getTeam_short_name1();
        String team_name2 = beanHome.get(position).getTeam_name2();
        final String team_image2 = beanHome.get(position).getTeam_image2();
        final String team_short_name2 = beanHome.get(position).getTeam_short_name2();
        final String team_one_score = beanHome.get(position).getTeam1Score();
        final String team_two_score = beanHome.get(position).getTeam2Score();
        final String team_one_over = beanHome.get(position).getTeam1Over();
        final String team_two_over = beanHome.get(position).getTeam2Over();
        final String match_status_note = beanHome.get(position).getMatch_status_note();
        // set data
        if (team_name1.length() > 3) {
            holder.tv_matchNameSort1.setText(team_name1.substring(0, 3));
        } else {
            holder.tv_matchNameSort1.setText(team_name1);
        }
        if (team_name2.length() > 3) {
            holder.tv_matchNameSort2.setText(team_name2.substring(0, 3));
        } else {
            holder.tv_matchNameSort2.setText(team_name2);
        }

        holder.tv_matchName.setText(title);
        holder.tv_matchTitleName.setText(type);

        Glide.with(mContext).load(Config.TEAMFLAGIMAGE + team_image1)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.iv_matchImage1);
        Glide.with(mContext).load(Config.TEAMFLAGIMAGE + team_image2)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.iv_matchImage2);
        if (match_status.equals("Live")) {
            holder.tv_statusResult.setText("Live");
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 //replaceFragment(new MyContestFragment());
                custom_view_pager_container.setCurrentItem(1);
             }
        });
    }

//    public void replaceFragment(Fragment fragment) {
//        FragmentManager fragmentManager = ((AppCompatActivity)mContext).getSupportFragmentManager();
//        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        transaction.replace(R.id.fragment_container, fragment);
//        transaction.commit();
//    }

    @Override
    public int getItemCount() {
        return beanHome.size();
    }

    public class MatchHolder extends RecyclerView.ViewHolder {

        ImageView iv_matchImage1, iv_matchImage2;
        TextView tv_matchName, tv_matchName1, tv_matchName2, tv_matchNameSort1, tv_statusResult, tv_matchNameSort2, tv_matchTitleName;

        public MatchHolder(@NonNull View itemView) {
            super(itemView);
            iv_matchImage1 = itemView.findViewById(R.id.iv_matchImage1);
            iv_matchImage2 = itemView.findViewById(R.id.iv_matchImage2);
            tv_matchName = itemView.findViewById(R.id.tv_matchName);
            tv_matchName1 = itemView.findViewById(R.id.tv_matchName1);
            tv_matchName2 = itemView.findViewById(R.id.tv_matchName2);
            tv_matchNameSort1 = itemView.findViewById(R.id.tv_matchNameSort1);
            tv_matchNameSort2 = itemView.findViewById(R.id.tv_matchNameSort2);
            tv_statusResult = itemView.findViewById(R.id.tv_statusResult);
            tv_matchTitleName = itemView.findViewById(R.id.tv_matchTitleName);
        }
    }
}
