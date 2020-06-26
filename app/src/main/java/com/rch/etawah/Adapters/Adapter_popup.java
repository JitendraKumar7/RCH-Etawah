package com.rch.etawah.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.rch.etawah.ModelClass.varient_product;
import com.rch.etawah.R;

import java.util.List;

import static com.rch.etawah.Config.BaseURL.IMG_URL;

public class Adapter_popup extends RecyclerView.Adapter<Adapter_popup.holder> {

    List<varient_product> varientProductList;
    Context context;



    public Adapter_popup(List<varient_product> varientProductList) {
        this.varientProductList = varientProductList;
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.listitem_popup, viewGroup, false);
        context = viewGroup.getContext();
        return new holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final holder holder, int i) {

        final varient_product selectAreaModel = varientProductList.get(i);


        holder.unit.setText(selectAreaModel.getVariant_unit());

          holder.unitvalue.setText(selectAreaModel.getVariant_unit_value());

        holder.price.setText( " " +selectAreaModel.getVariant_price());
//        holder.mrp.setText("" + selectAreaModel.getVariant_mrp());
        Picasso.with(context).load(IMG_URL+selectAreaModel.getVarient_imqge()).into(holder.prodImage);

//        int varientprice= Integer.valueOf(selectAreaModel.getVariant_price());
//        int varientmrp= Integer.valueOf(selectAreaModel.getVariant_mrp());
//
//        String savingprice=String.valueOf(varientmrp-varientprice);
//
//        holder.varientdiscount.setText("Rs" + " " + savingprice +" " + "off");


    }

    @Override
    public int getItemCount() {
        return varientProductList.size();
    }

    public class holder extends RecyclerView.ViewHolder {

        TextView unit, unitvalue , price ,mrp;
        ImageView prodImage;
        LinearLayout btn_Add;

        public holder(@NonNull View itemView) {
            super(itemView);

            unit = itemView.findViewById(R.id.unit);
            unitvalue = itemView.findViewById(R.id.unitvalue);

            price = itemView.findViewById(R.id.price);
//            mrp.setPaintFlags(mrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//
//            mrp = itemView.findViewById(R.id.producrmrp);
            prodImage= itemView.findViewById(R.id.prodImage);
            btn_Add= itemView.findViewById(R.id.btn_Add);

            btn_Add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

        }
    }
}
