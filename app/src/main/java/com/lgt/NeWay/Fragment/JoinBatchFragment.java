package com.lgt.NeWay.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lgt.NeWay.Fragment.Adapter.AdapterJoinBatch;
import com.lgt.NeWay.Fragment.Adapter.ModelJoinBatch;
import com.lgt.NeWay.Neway.R;

import java.util.ArrayList;
import java.util.List;


public class JoinBatchFragment extends Fragment {


    RecyclerView rv_join_batch;

    AdapterJoinBatch adapterJoinBatch;
    List<ModelJoinBatch>list=new ArrayList<>();
    Context context;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_join_batch, container, false);

        rv_join_batch=view.findViewById(R.id.rv_join_batch);

     loadApiData();


        return view;
    }

    private void loadApiData() {
       // list.clear();

        list.add(new ModelJoinBatch("Raj","pending","8604111232","Math","MOnthly","20Apr2021",""));
        list.add(new ModelJoinBatch("","","","","","",""));
        list.add(new ModelJoinBatch("","","","","","",""));
        list.add(new ModelJoinBatch("","","","","","",""));
        list.add(new ModelJoinBatch("","","","","","",""));
        list.add(new ModelJoinBatch("","","","","","",""));
        list.add(new ModelJoinBatch("","","","","","",""));
        list.add(new ModelJoinBatch("","","","","","",""));
        list.add(new ModelJoinBatch("","","","","","",""));


      //  adapterJoinBatch.notifyDataSetChanged();
        adapterJoinBatch=new AdapterJoinBatch(list,getContext());
        rv_join_batch.setAdapter(adapterJoinBatch);
        rv_join_batch.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        rv_join_batch.setHasFixedSize(true);
    }


}