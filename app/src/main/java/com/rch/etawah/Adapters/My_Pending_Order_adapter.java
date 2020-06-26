package com.rch.etawah.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.rch.etawah.activity.Cancel_Order;
import com.rch.etawah.activity.MainActivity;
import com.rch.etawah.activity.Myorderdetails;
import com.rch.etawah.ModelClass.My_Pending_order_model;
import com.rch.etawah.R;

import java.util.List;


import static android.content.Context.MODE_PRIVATE;

public class My_Pending_Order_adapter extends RecyclerView.Adapter<My_Pending_Order_adapter.MyViewHolder> {

    private List<My_Pending_order_model> modelList;
    private LayoutInflater inflater;
    private Fragment currentFragment;
    SharedPreferences preferences;
    private Context context;

    public My_Pending_Order_adapter(Context context, List<My_Pending_order_model> modemodelList, final Fragment currentFragment) {

        this.context = context;
        this.modelList = modelList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.currentFragment = currentFragment;

    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_orderno, tv_status, tv_date, tv_time, tv_price, tv_item, relativetextstatus, tv_tracking_date;
        public TextView tv_pending_date, tv_pending_time, tv_confirm_date, tv_confirm_time, tv_delevered_date, tv_delevered_time, tv_cancel_date, tv_cancel_time;
        public View view1, view2, view3, view4, view5, view6;
        public RelativeLayout relative_background , rr;
        public ImageView Confirm, Out_For_Deliverde, Delivered;
        CardView cardView;
        public TextView tv_methid1;
        public String method;
        Button canclebtn , reorder_btn;
        LinearLayout linearLayout;

        public MyViewHolder(View view) {

            super(view);
            tv_orderno = (TextView) view.findViewById(R.id.tv_order_no);
            tv_status = (TextView) view.findViewById(R.id.tv_order_status);
            relativetextstatus = (TextView) view.findViewById(R.id.status);
            tv_tracking_date = (TextView) view.findViewById(R.id.tracking_date);
            tv_date = (TextView) view.findViewById(R.id.tv_order_date);
            tv_time = (TextView) view.findViewById(R.id.tv_order_time);
            tv_price = (TextView) view.findViewById(R.id.tv_order_price);
            tv_item = (TextView) view.findViewById(R.id.tv_order_item);
            canclebtn = view.findViewById(R.id.canclebtn);
            reorder_btn = view.findViewById(R.id.reorder_btn);
            rr = view.findViewById(R.id.rrrr);

            cardView = view.findViewById(R.id.card_view);

            linearLayout=view.findViewById(R.id.l2);
//            //Payment Method
            tv_methid1 = (TextView) view.findViewById(R.id.method1);
            //Date And Time
            tv_pending_date = (TextView) view.findViewById(R.id.pending_date);
//            tv_pending_time = (TextView) view.findViewById(R.id.pending_time);
            tv_confirm_date = (TextView) view.findViewById(R.id.confirm_date);
//            tv_confirm_time = (TextView) view.findViewById(R.id.confirm_time);
            tv_delevered_date = (TextView) view.findViewById(R.id.delevered_date);
//            tv_delevered_time = (TextView) view.findViewById(R.id.delevered_time);
            tv_cancel_date = (TextView) view.findViewById(R.id.cancel_date);
//            tv_cancel_time = (TextView) view.findViewById(R.id.cancel_time);
            //Oredre Tracking
            view1 = (View) view.findViewById(R.id.view1);
            view2 = (View) view.findViewById(R.id.view2);
            view3 = (View) view.findViewById(R.id.view3);
            view4 = (View) view.findViewById(R.id.view4);
            view5 = (View) view.findViewById(R.id.view5);
            view6 = (View) view.findViewById(R.id.view6);
            relative_background = (RelativeLayout) view.findViewById(R.id.relative_background);

            Confirm = (ImageView) view.findViewById(R.id.confirm_image);
            Out_For_Deliverde = (ImageView) view.findViewById(R.id.delivered_image);
            Delivered = (ImageView) view.findViewById(R.id.cancal_image);

            reorder_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context.getApplicationContext(), MainActivity.class));
                }
            });

         canclebtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 context.startActivity(new Intent(context.getApplicationContext(),Cancel_Order.class));
             }
         });
        }
    }

    public My_Pending_Order_adapter(List<My_Pending_order_model> modelList) {
        this.modelList = modelList;
    }

    @Override
    public My_Pending_Order_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listtem_pendingorder, parent, false);
        context = parent.getContext();
        return new My_Pending_Order_adapter.MyViewHolder(itemView);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(My_Pending_Order_adapter.MyViewHolder holder, int position) {
        My_Pending_order_model mList = modelList.get(position);

        holder.tv_orderno.setText(mList.getCart_id());

//        if (mList.getOrder_status().equals("0")) {
            holder.relativetextstatus.setText(mList.getOrder_status());
            holder.tv_status.setText("Payment:-" + " " + mList.getPayment_status());
//            holder.relativetextstatus.setText(context.getResources().getString(R.string.pending));
//            holder.relative_background.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
//        } else if (mList.getOrder_status().equals("1")) {
//            holder.view1.setBackgroundColor(context.getResources().getColor(R.color.green));
//            holder.view2.setBackgroundColor(context.getResources().getColor(R.color.green));
//            holder.relative_background.setBackgroundColor(context.getResources().getColor(R.color.orange));
//            holder.Confirm.setImageResource(R.color.green);
//            holder.tv_status.setText(context.getResources().getString(R.string.confirm));
//            holder.relativetextstatus.setText(context.getResources().getString(R.string.confirm));
//            holder.tv_status.setTextColor(context.getResources().getColor(R.color.green));
//        } else if (mList.getOrder_status().equals("2")) {
//            holder.view1.setBackgroundColor(context.getResources().getColor(R.color.green));
//            holder.relative_background.setBackgroundColor(context.getResources().getColor(R.color.purple));
//            holder.view2.setBackgroundColor(context.getResources().getColor(R.color.green));
//            holder.view3.setBackgroundColor(context.getResources().getColor(R.color.green));
//            holder.view4.setBackgroundColor(context.getResources().getColor(R.color.green));
//            holder.Confirm.setImageResource(R.color.green);
//            holder.Out_For_Deliverde.setImageResource(R.color.green);
//            holder.tv_status.setText(context.getResources().getString(R.string.outfordeliverd));
//            holder.relativetextstatus.setText(context.getResources().getString(R.string.outfordeliverd));
//            holder.tv_status.setTextColor(context.getResources().getColor(R.color.green));
//        }
//           else if (mList.getOrder_status().equals("4")) {
//holder.linearLayout.setVisibility(View.GONE);
//        }

//        if (mList.getPayment_method().equals("Store Pick Up")) {
            holder.tv_methid1.setText(mList.getPayment_method());
//        } else if (mList.getPayment_method().equals("Cash On Delivery")) {
//            holder.tv_methid1.setText("Cash On Delivery");
//        } else if (mList.getPayment_method().equals("Debit / Credit Card")) {
//            holder.tv_methid1.setText("PrePaid");
//        } else if (mList.getPayment_method().equals("Net Banking")) {
//            holder.tv_methid1.setText("PrePaid");
//        }
        holder.tv_date.setText(mList.getDelivery_date());
        holder.tv_tracking_date.setText(mList.getDelivery_date());

        preferences = context.getSharedPreferences("lan", MODE_PRIVATE);
        String language=preferences.getString("language","");

        if (language.contains("spanish")) {
            String timefrom=mList.getTime_slot();


            timefrom=timefrom.replace("pm","م");
            timefrom=timefrom.replace("am","ص");


            String time=timefrom;

            holder.tv_time.setText(time);
        }else {

            String timefrom=mList.getTime_slot();
            String time=timefrom;

            holder.tv_time.setText(time);

        }

        holder.tv_price.setText(context.getResources().getString(R.string.currency) + mList.getPrice());
        holder.tv_item.setText(context.getResources().getString(R.string.tv_cart_item) + mList.getTotal_items());
//        holder.tv_pending_time.setText(mList.getDelivery_time_from() + "-" + mList.getDelivery_time_to());
        holder.tv_pending_date.setText(mList.getDelivery_date());
//        holder.tv_confirm_time.setText(mList.getDelivery_time_from() + "-" + mList.getDelivery_time_to());
        holder.tv_confirm_date.setText(mList.getDelivery_date());
//        holder.tv_delevered_time.setText(mList.getDelivery_time_from() + "-" + mList.getDelivery_time_to());
        holder.tv_delevered_date.setText(mList.getDelivery_date());
//        holder.tv_cancel_time.setText(mList.getDelivery_time_from() + "-" + mList.getDelivery_time_to());
        holder.tv_cancel_date.setText(mList.getDelivery_date());

       holder. rr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sale_id = modelList.get(position).getCart_id();
                String date = modelList.get(position).getDelivery_date();
                String time = modelList.get(position).getTime_slot();
                String total = modelList.get(position).getPrice();
                String status = modelList.get(position).getOrder_status();
                String deli_charge = modelList.get(position).getDelivery_charge();
                Intent intent=new Intent(context.getApplicationContext(), Myorderdetails.class);
                intent.putExtra("sale_id", sale_id);
                intent.putExtra("date", date);
                intent.putExtra("time", time);
                intent.putExtra("total", total);
                intent.putExtra("status", status);
                intent.putExtra("deli_charge", deli_charge);
                context.startActivity(intent);
            }
        });

    }
    public void removeddata(int postion){
        modelList.remove(postion);
        notifyItemRemoved(postion);
        notifyItemRangeChanged(postion,getItemCount());
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

}

