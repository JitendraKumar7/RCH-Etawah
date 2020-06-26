package com.rch.etawah.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.rch.etawah.activity.CategoryPage;
import com.rch.etawah.ModelClass.SubCatModel;
import com.rch.etawah.ModelClass.SubChildCatModel;
import com.rch.etawah.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SubCatAdapter extends RecyclerView.Adapter<SubCatAdapter.MyViewHolder> {

    private List<SubChildCatModel>  subChildCatModels = new ArrayList<>();
    private List<SubCatModel> homeCateList;
    Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView prodNAme,pdetails;
        ImageView image;
        RecyclerView recyclerSubCate;
        CardView cardView;

        public MyViewHolder(View view) {
            super(view);
            prodNAme = (TextView) view.findViewById(R.id.pNAme);
            pdetails = (TextView) view.findViewById(R.id.pDetails);
            image = (ImageView) view.findViewById(R.id.Pimage);
            recyclerSubCate=view.findViewById(R.id.recyclerSubCate);
            cardView=view.findViewById(R.id.cardView);
        }
    }


    public SubCatAdapter(List<SubCatModel> homeCateList, Context context) {
        this.homeCateList = homeCateList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_sub_cat_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        SubCatModel cc = homeCateList.get(position);
        holder.prodNAme.setText(cc.getName());
        holder.pdetails.setText(cc.getDetail());
        holder.image.setImageResource(R.drawable.splashicon);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get_subcateory(cc.getSub_array(),holder.recyclerSubCate,cc.getId());
            }

        });
    }

    @Override
    public int getItemCount() {
        return homeCateList.size();
    }


    private void get_subcateory(JSONArray response,RecyclerView  recyclerView,String cat_id) {


        JSONArray array  = response;

        if (array.length()==0){
            Intent intent = new Intent(context, CategoryPage.class);
            intent.putExtra("cat_id",cat_id);
            context.startActivity(intent);
        }
        else {


            for (int i = 0; i < array.length(); i++) {

                try {
                    JSONObject object = response.getJSONObject(i);

                    object = array.getJSONObject(i);

                    SubChildCatModel model = new SubChildCatModel();


                    model.setDetail(object.getString("description"));
                    model.setId(object.getString("cat_id"));
                    model.setImages(object.getString("image"));
                    model.setName(object.getString("title"));

                    subChildCatModels.add(model);

                    SubChildCatAdapter cateAdapter = new SubChildCatAdapter(subChildCatModels, context);
                    recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                    recyclerView.setAdapter(cateAdapter);
                    cateAdapter.notifyDataSetChanged();


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }

    }

}
