package com.lgt.NeWay.activity.UserBatchRequest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.lgt.NeWay.Neway.R;

import java.util.ArrayList;
import java.util.List;

public class UserBatchRequest extends AppCompatActivity {
    RecyclerView rv_Userbatchreqlist;
    List<ModelUserBatchRequest>mlist=new ArrayList<>();
    AdapterUserbatchRequest adapterUserbatchRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_batch_request);

        iniViews();
        loaduserrequestbatch();
    }


    private void iniViews() {
        rv_Userbatchreqlist=findViewById(R.id.rv_Userbatchreqlist);
    }
    private void loaduserrequestbatch() {
        mlist.add(new ModelUserBatchRequest("","",""));
        mlist.add(new ModelUserBatchRequest("","",""));
        mlist.add(new ModelUserBatchRequest("","",""));

        adapterUserbatchRequest=new AdapterUserbatchRequest(mlist,getApplicationContext());
        rv_Userbatchreqlist.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));
        rv_Userbatchreqlist.setAdapter(adapterUserbatchRequest);
        rv_Userbatchreqlist.setHasFixedSize(true);
    }

}