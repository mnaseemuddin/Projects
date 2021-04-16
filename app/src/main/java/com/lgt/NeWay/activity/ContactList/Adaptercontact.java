package com.lgt.NeWay.activity.ContactList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lgt.NeWay.Neway.R;

import java.util.List;

public class Adaptercontact extends RecyclerView.Adapter<Adaptercontact.Cityholder> {

    List<ModelContact> mlist;
    Context context;
    DeleteContactInterface deleteContactInterface;

    public Adaptercontact(List<ModelContact> mlist, Context context,DeleteContactInterface deleteContactInterface) {
        this.mlist = mlist;
        this.context = context;
        this.deleteContactInterface = deleteContactInterface;
    }

    @NonNull
    @Override
    public Adaptercontact.Cityholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.adapter_contactlist,parent,false);
        return new Cityholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adaptercontact.Cityholder holder, int position) {
        String Contact_id=mlist.get(position).getTbl_coaching_user_contact_number_id();
       holder.tv_Contact_Number.setText(mlist.get(position).getTv_Contact_Number());
        holder.tv_Contact_Name.setText(mlist.get(position).getTv_Contact_Name());

        holder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteContactInterface.deleteContact(Contact_id);

            }
        });


    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    public class Cityholder extends RecyclerView.ViewHolder {
        TextView tv_Contact_Name,tv_Contact_Number;
        Spinner sp_Statuspending;
        ImageView iv_delete;
        public Cityholder(@NonNull View itemView) {
            super(itemView);
            tv_Contact_Name=itemView.findViewById(R.id.tv_Contact_Name);
            tv_Contact_Number=itemView.findViewById(R.id.tv_Contact_Number);
            sp_Statuspending=itemView.findViewById(R.id.sp_Statuspending);
            iv_delete=itemView.findViewById(R.id.iv_delete);
        }
    }
}
