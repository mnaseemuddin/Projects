package com.lgt.also_food_court_userApp.Adapters;

import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lgt.also_food_court_userApp.Models.modelBottomSheetAddProduct;
import com.lgt.also_food_court_userApp.R;

import java.util.List;

import static com.lgt.also_food_court_userApp.Fragments.BottomSheetAddProduct.selectedItem;
import static com.lgt.also_food_court_userApp.Fragments.BottomSheetAddProduct.tvPerPiecePriceBottomSheet;


public class AdapterBottomSheetAddProduct extends RecyclerView.Adapter<AdapterBottomSheetAddProduct.AddProductHolder> {
   private List<modelBottomSheetAddProduct>listBottomSheetAddProduct;
    private Context context;
    public static String ProductQuantityAmount;
    public static String ProductAmount;
    int CurrentPosition=0;

    public AdapterBottomSheetAddProduct(List<modelBottomSheetAddProduct> listBottomSheetAddProduct, Context context) {
        this.listBottomSheetAddProduct = listBottomSheetAddProduct;
        this.context = context;
    }

    @NonNull
    @Override
    public AddProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.bottom_sheet_addproduct_layout,parent,false);
        return new AddProductHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterBottomSheetAddProduct.AddProductHolder holder, final int position) {

        holder.tv_quantity.setText(listBottomSheetAddProduct.get(position).getQuantityType());
        holder.tv_Amount.setText(listBottomSheetAddProduct.get(position).getProductAmount());
        holder.tv_fullAmount.setText(listBottomSheetAddProduct.get(position).getMainPrice());
        holder.tv_fullAmount.setPaintFlags(holder.tv_fullAmount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
       //String B= listBottomSheetAddProduct.get(position).getProductAmount();

       // CurrentPosition=position;
       // notifyDataSetChanged();

        if (listBottomSheetAddProduct.get(position).getSelected()){
            holder.chkBoxType.setChecked(true);
        }else {
            holder.chkBoxType.setChecked(false);
           // Toast.makeText(context, "Please Select Amount", Toast.LENGTH_SHORT).show();

        }
        holder.chkBoxType.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton,boolean B) {
                if (B) {


                        for (int i=0;i<listBottomSheetAddProduct.size();i++){
                            if (i==position){
                                listBottomSheetAddProduct.get(i).setSelected(true);
                            }else {
                                listBottomSheetAddProduct.get(i).setSelected(false);
                            }
                        }
                        ProductAmount=listBottomSheetAddProduct.get(position).getProductAmount();
                        //Log.e("dkfhd",tvPerPiecePriceBottomSheet.toString()+"");
                        tvPerPiecePriceBottomSheet.setText(ProductAmount);
                    selectedItem=listBottomSheetAddProduct.get(position).getQuantityType();

                        notifyDataSetChanged();

                   /* }else{

                        tvPerPiecePriceBottomSheet.setText("");
                    }*/
                  /*  //selectedItem = "full_sprice";
                    //tvPerPiecePriceBottomSheet
                    ProductAmount=listBottomSheetAddProduct.get(position).getProductAmount();
                    Log.e("dkfhd",tvPerPiecePriceBottomSheet+"");
                    tvPerPiecePriceBottomSheet.setText(ProductAmount);*/
                }else {
/*                    for (int i=0;i<listBottomSheetAddProduct.size();i++){
                       listBottomSheetAddProduct.get(i).setSelected(false);
                    }*/
                }
            }
        });


    /*    holder.chkBoxType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              *//*ProductQuantityAmount=listBottomSheetAddProduct.get(position).getProductAmount();
                Log.e("dkfhd",ProductQuantityAmount+"");*//*
                if (CurrentPosition==position){
                    holder.chkBoxType.isChecked();
                    ProductAmount=listBottomSheetAddProduct.get(position).getProductAmount();
                    Log.e("dkfhd",tvPerPiecePriceBottomSheet+"");
                    tvPerPiecePriceBottomSheet.setText(ProductAmount);
                }else{

                    tvPerPiecePriceBottomSheet.setText("");
                }





            }
        });*/

    }

    @Override
    public int getItemCount() {
        return listBottomSheetAddProduct.size();
    }

    public class AddProductHolder extends RecyclerView.ViewHolder {
        private CheckBox chkBoxType;
        private TextView tv_quantity,tv_Amount,tv_fullAmount;
        public AddProductHolder(@NonNull View itemView) {
            super(itemView);

            chkBoxType=itemView.findViewById(R.id.chkBoxType);
            tv_quantity=itemView.findViewById(R.id.tv_quantity);
            tv_Amount=itemView.findViewById(R.id.tvAmount);
            tv_fullAmount=itemView.findViewById(R.id.tv_fullAmount);

        }
    }
}
