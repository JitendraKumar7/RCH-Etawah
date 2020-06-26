package com.rch.etawah.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.rch.etawah.activity.PaymentDetails;
import com.rch.etawah.ModelClass.CoupunModel;
import com.rch.etawah.R;
import java.util.List;

public class Coupun_Listing_Adapter extends RecyclerView.Adapter<Coupun_Listing_Adapter.MyViewHolder> {

    private List<CoupunModel> modelList;

    private Context context;

    public Coupun_Listing_Adapter(List<CoupunModel> coupunModelList) {
        this.modelList = coupunModelList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView coupun_heading, discrip_coupon, copen_text, applybtn;
        LinearLayout linearLayout ;

        public MyViewHolder(View view) {
            super(view);

            coupun_heading = (TextView) view.findViewById(R.id.coupun_heading);
            discrip_coupon = (TextView) view.findViewById(R.id.discrip_coupon);
            linearLayout = view.findViewById(R.id.layout);

            copen_text = (TextView) view.findViewById(R.id.copen_text);

            applybtn = (TextView) view.findViewById(R.id.applybtn);


        }

    }

    @Override
    public Coupun_Listing_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_couponlist, parent, false);


        context = parent.getContext();

        return new Coupun_Listing_Adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(Coupun_Listing_Adapter.MyViewHolder holder, final int position) {
        CoupunModel mList = modelList.get(position);

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PaymentDetails.class);

                intent.putExtra("code",mList.getCoupon_code());
                Log.d("ff",mList.getCoupon_code());
                context.startActivity(intent);
            }
        });

        holder.coupun_heading.setText(mList.getCoupon_name());
        holder.discrip_coupon.setText(mList.getCoupon_description());
        holder.copen_text.setText(mList.getCoupon_code());
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


}