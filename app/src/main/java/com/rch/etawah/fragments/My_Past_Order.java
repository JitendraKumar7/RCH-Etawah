package com.rch.etawah.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rch.etawah.ui.ActivityOrderSummary;
import com.rch.etawah.Config.BaseURL;
import com.rch.etawah.ModelClass.My_Past_order_model;
import com.rch.etawah.R;
import com.rch.etawah.util.ConnectivityReceiver;
import com.rch.etawah.util.CustomVolleyJsonArrayRequest;
import com.rch.etawah.util.Session_management;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import static android.content.Context.MODE_PRIVATE;
import static com.rch.etawah.Config.BaseURL.OrderDoneUrl;

public class My_Past_Order extends Fragment {

    //  private static String TAG = Fragment.My_Past_Order.class.getSimpleName();

    private RecyclerView rv_myorder;

    private List<My_Past_order_model> my_order_modelList = new ArrayList<>();
    TabHost tHost;

    public My_Past_Order() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_past_order, container, false);

        // ((My_Order_activity) getActivity()).setTitle(getResources().getString(R.string.my_order));


        // handle the touch event if true
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // check user can press back button or not
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {

//                    Fragment fm = new Home_fragment();
//                    android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
//                    fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
//                            .addToBackStack(null).commit();
                    return true;
                }
                return false;
            }
        });

        rv_myorder = (RecyclerView) view.findViewById(R.id.rv_myorder);
        rv_myorder.setLayoutManager(new LinearLayoutManager(getActivity()));

        Session_management sessionManagement = new Session_management(getActivity());
        String user_id = sessionManagement.getUserDetails().get(BaseURL.KEY_ID);

        // check internet connection
        if (ConnectivityReceiver.isConnected()) {
            makeGetOrderRequest(user_id);
        } else {
           // ((MainActivity) getActivity()).onNetworkConnectionChanged(false);
        }

        // recyclerview item click listener
//        rv_myorder.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rv_myorder, new RecyclerTouchListener.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                String sale_id = my_order_modelList.get(position).getSale_id();
//                String date = my_order_modelList.get(position).getOn_date();
//                String time = my_order_modelList.get(position).getDelivery_time_from() + "-" + my_order_modelList.get(position).getDelivery_time_to();
//                String total = my_order_modelList.get(position).getTotal_amount();
//                String status = my_order_modelList.get(position).getStatus();
//                String deli_charge = my_order_modelList.get(position).getDelivery_charge();
//                Intent intent = new Intent(getContext(), MyOrderDetail.class);
//                intent.putExtra("sale_id", sale_id);
//                intent.putExtra("date", date);
//                intent.putExtra("time", time);
//                intent.putExtra("total", total);
//                intent.putExtra("status", status);
//                intent.putExtra("deli_charge", deli_charge);
//                startActivity(intent);
//
//            }
//
//            @Override
//            public void onLongItemClick(View view, int position) {
//
//            }
//        }));

        return view;
    }


    /**
     * Method to make json array request where json response starts wtih
     */
    private void makeGetOrderRequest(String userid) {
        // Tag used to cancel the request
        String tag_json_obj = "json_socity_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", userid);

        CustomVolleyJsonArrayRequest jsonObjReq = new CustomVolleyJsonArrayRequest(Request.Method.POST,
                OrderDoneUrl, params, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                Log.d("asdf",response.toString());

                try {
                    JSONObject object =response.getJSONObject(0);
                    String data = object.getString("data");
                    if (data.equals("no orders yet"))
                    {

                    }
                    else
                    {
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<My_Past_order_model>>() {
                        }.getType();
                        my_order_modelList = gson.fromJson(response.toString(), listType);
                        My_Past_Order_adapter adapter = new My_Past_Order_adapter(my_order_modelList);
                        rv_myorder.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        if (my_order_modelList.isEmpty()) {
                             Toast.makeText(getActivity(), "no_rcord_found", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> param = new HashMap<>();
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.getCache().clear();
        requestQueue.add(jsonObjReq);
    }


    public class My_Past_Order_adapter extends RecyclerView.Adapter<My_Past_Order_adapter.MyViewHolder> {

        private List<My_Past_order_model> modelList;
        private LayoutInflater inflater;
        private Fragment currentFragment;
        SharedPreferences preferences , valuepref;
        SharedPreferences.Editor editor;
        private Context context;
        String Used_Wallet_amount;
        private String getuser_id = "";


        public My_Past_Order_adapter(Context context, List<My_Past_order_model> modemodelList, final Fragment currentFragment) {

            this.context = context;
            this.modelList = modelList;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.currentFragment = currentFragment;
        }


        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView tv_orderno, tv_status, tv_date, tv_time, tv_price, tv_item, relativetextstatus, tv_tracking_date;
            public TextView tv_pending_date, tv_pending_time, tv_confirm_date, tv_confirm_time, tv_delevered_date, tv_delevered_time, tv_cancel_date, tv_cancel_time;
            public View view1, view2, view3, view4, view5, view6;
            public RelativeLayout relative_background;
            public ImageView Confirm, Out_For_Deliverde, Delivered;
            CardView cardView;
            public TextView tv_methid1;
            public String method;
            Button reorder_btn;


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
                cardView = view.findViewById(R.id.card_view);
                reorder_btn = view.findViewById(R.id.reorder_btn);


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

            }
        }

        public My_Past_Order_adapter(List<My_Past_order_model> modelList) {
            this.modelList = modelList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_my_past_order_rv, parent, false);
            context = parent.getContext();
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            final My_Past_order_model mList = modelList.get(position);
//
//            holder. reorder_btn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    valuepref =context.getSharedPreferences("valuepref",MODE_PRIVATE);
//
//                    editor=valuepref.edit();
//
//                    editor.putString("value","1");
//                    editor.commit();
//
//                    android.app.Fragment fm = new Delivery_fragment();
//                    FragmentManager fragmentManager = ((Activity) context).getFragmentManager();
//                    fragmentManager.beginTransaction().replace(R.id.container_12, fm)
//
//                            .addToBackStack(null).commit();
//
//
//                    Intent intent = new Intent(context, MainActivity.class);
//                    context.startActivity(intent);
//
//                    Usewalletfororder(getuser_id, Used_Wallet_amount);
//
//
//                }
//            });
            holder.reorder_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                   android.support.v4.app.Fragment fm = new Fragment();
//                    FragmentManager fragmentManager = getFragmentManager();
//                    fragmentManager.beginTransaction().replace(R.id.container_12, fm)
//                            .addToBackStack(null).commit();
                    context.startActivity(new Intent(context, ActivityOrderSummary.class));

                 //   Usewalletfororder(getuser_id, Used_Wallet_amount);

                }
            });
            holder.tv_orderno.setText(mList.getSale_id());

            if (mList.getStatus().equals("0")) {
                holder.tv_status.setText(context.getResources().getString(R.string.pending));
                holder.relativetextstatus.setText(context.getResources().getString(R.string.pending));
                holder.relative_background.setBackgroundColor(context.getResources().getColor(R.color.color_2));
            } else if (mList.getStatus().equals("1")) {
                holder.view1.setBackgroundColor(context.getResources().getColor(R.color.orange));
                holder.view2.setBackgroundColor(context.getResources().getColor(R.color.orange));
                holder.relative_background.setBackgroundColor(context.getResources().getColor(R.color.orange));
                holder.Confirm.setImageResource(R.color.orange);
                holder.tv_status.setText(context.getResources().getString(R.string.confirm));
                holder.relativetextstatus.setText(context.getResources().getString(R.string.confirm));
                holder.tv_status.setTextColor(context.getResources().getColor(R.color.orange));
            } else if (mList.getStatus().equals("2")) {
                holder.view1.setBackgroundColor(context.getResources().getColor(R.color.orange));
                holder.relative_background.setBackgroundColor(context.getResources().getColor(R.color.purple));
                holder.view2.setBackgroundColor(context.getResources().getColor(R.color.orange));
                holder.view3.setBackgroundColor(context.getResources().getColor(R.color.orange));
                holder.view4.setBackgroundColor(context.getResources().getColor(R.color.orange));
                holder.view5.setBackgroundColor(context.getResources().getColor(R.color.orange));
                holder.view6.setBackgroundColor(context.getResources().getColor(R.color.orange));
                holder.Confirm.setImageResource(R.color.orange);
                holder.Out_For_Deliverde.setImageResource(R.color.orange);
                holder.tv_status.setText(context.getResources().getString(R.string.outfordeliverd));
                holder.relativetextstatus.setText(context.getResources().getString(R.string.outfordeliverd));
                holder.tv_status.setTextColor(context.getResources().getColor(R.color.orange));
            } else if (mList.getStatus().equals("4")) {
                holder.view1.setBackgroundColor(context.getResources().getColor(R.color.orange));
                holder.relative_background.setBackgroundColor(context.getResources().getColor(R.color.orange));
                holder.view2.setBackgroundColor(context.getResources().getColor(R.color.orange));
                holder.view3.setBackgroundColor(context.getResources().getColor(R.color.orange));
                holder.view4.setBackgroundColor(context.getResources().getColor(R.color.orange));
                holder.view5.setBackgroundColor(context.getResources().getColor(R.color.orange));
                holder.view6.setBackgroundColor(context.getResources().getColor(R.color.orange));
                holder.Confirm.setImageResource(R.color.orange);
                holder.Out_For_Deliverde.setImageResource(R.color.orange);
                holder.Delivered.setImageResource(R.color.orange);
                holder.tv_status.setText(context.getResources().getString(R.string.delivered));
                holder.relativetextstatus.setText(context.getResources().getString(R.string.delivered));
                holder.tv_status.setTextColor(context.getResources().getColor(R.color.orange));
            }

            holder.tv_methid1.setText(mList.getPayment_method());
            holder.tv_date.setText(mList.getOn_date());
            holder.tv_tracking_date.setText(mList.getOn_date());

            preferences = context.getSharedPreferences("lan", MODE_PRIVATE);
            String language = preferences.getString("language", "");
            if (language.contains("spanish")) {
                String timefrom = mList.getDelivery_time_from();
                String timeto = mList.getDelivery_time_to();

                timefrom = timefrom.replace("pm", "م");
                timefrom = timefrom.replace("am", "ص");

                timeto = timeto.replace("pm", "م");
                timeto = timeto.replace("am", "ص");

                String time = timefrom + "-" + timeto;

                holder.tv_time.setText(time);
            } else {

                String timefrom = mList.getDelivery_time_from();
                String timeto = mList.getDelivery_time_to();
                String time = timefrom + "-" + timeto;

                holder.tv_time.setText(time);

            }

            holder.tv_price.setText(context.getResources().getString(R.string.currency) + mList.getTotal_amount());
            holder.tv_item.setText(context.getResources().getString(R.string.tv_cart_item) + mList.getTotal_items());
//        holder.tv_pending_time.setText(mList.getDelivery_time_from() + "-" + mList.getDelivery_time_to());
            holder.tv_pending_date.setText(mList.getOn_date());
//        holder.tv_confirm_time.setText(mList.getDelivery_time_from() + "-" + mList.getDelivery_time_to());
            holder.tv_confirm_date.setText(mList.getOn_date());
//        holder.tv_delevered_time.setText(mList.getDelivery_time_from() + "-" + mList.getDelivery_time_to());
            holder.tv_delevered_date.setText(mList.getOn_date());
//        holder.tv_cancel_time.setText(mList.getDelivery_time_from() + "-" + mList.getDelivery_time_to());
            holder.tv_cancel_date.setText(mList.getOn_date());

            holder.relative_background.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String sale_id = mList.getSale_id();
                    String date = mList.getOn_date();
                    String time = mList.getDelivery_time_from() + "-" + mList.getDelivery_time_to();
                    String total = mList.getTotal_amount();
                    String status = mList.getStatus();
                    String deli_charge = mList.getDelivery_charge();
                    Intent intent = new Intent(context.getApplicationContext(), ActivityOrderSummary.class);
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


        @Override
        public int getItemCount() {
            return modelList.size();
        }


/*
        private void Usewalletfororder(String userid, String Wallet) {
            String tag_json_obj = "json_add_order_req";
            Map<String, String> params = new HashMap<String, String>();
            params.put("user_id", userid);
            params.put("wallet_amount", Wallet);

            CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                    BaseURL.Wallet_CHECKOUT, params, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d(TAG, response.toString());

                    try {
                        String status = response.getString("responce");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                   */
/* if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        Toast.makeText(context.getApplicationContext(), context.getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                    }*//*

                }
            });
            AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
        }
*/
    }
}