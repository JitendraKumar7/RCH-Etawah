package com.rch.etawah.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.rch.etawah.activity.CategoryPage;
import com.rch.etawah.ModelClass.HomeCate;
import com.rch.etawah.ModelClass.SubCatModel;
import com.rch.etawah.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.rch.etawah.Config.BaseURL.IMG_URL;

public class HomeCategoryAdapter extends RecyclerView.Adapter<HomeCategoryAdapter.MyViewHolder> {

    private List<HomeCate> homeCateList;
    private Context context;
    private List<SubCatModel>subCatModels =  new ArrayList<>();

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView prodNAme,pdetails;
        ImageView image,pimage;
        boolean mines = true;
        RecyclerView recyclerSubCate;
        CardView cardView;


        public MyViewHolder(View view) {
            super(view);
            prodNAme = (TextView) view.findViewById(R.id.pNAme);
            pdetails = (TextView) view.findViewById(R.id.pDetails);
            pimage = view.findViewById(R.id.image);
            image = (ImageView) view.findViewById(R.id.Pimage);
            recyclerSubCate=view.findViewById(R.id.recyclerSubCate);
            cardView=view.findViewById(R.id.cardView);
        }
    }

    public HomeCategoryAdapter(List<HomeCate> homeCateList, Context context) {
        this.homeCateList = homeCateList;
        this.context = context;
    }

    @Override
    public HomeCategoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_homeshopcate, parent, false);

        return new HomeCategoryAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(HomeCategoryAdapter.MyViewHolder holder, int position) {
//        boolean mines = true ;

        HomeCate cc = homeCateList.get(position);

        holder.prodNAme.setText(cc.getName());
        holder.pdetails.setText(cc.getDetail());
        holder.image.setImageResource(R.drawable.splashicon);
        Glide.with(context)
                .load(IMG_URL+ cc.getImages())
                .centerCrop()
                .placeholder(R.drawable.splashicon)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(holder.image);

        if (cc.getSub_array()== null||cc.getSub_array().length()==0){


            holder.pimage.setVisibility(View.GONE);
        }


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.mines) {
                 holder.  mines = false;
                    if (cc.getSub_array() == null) {


                    } else {
                        holder.recyclerSubCate.setVisibility(View.VISIBLE);
                        get_subcateory(cc.getSub_array(), holder.recyclerSubCate, cc.getId());
                    }
                }
                else
                {
                holder.    mines = true;
                    holder.recyclerSubCate.setVisibility(View.GONE);
                }
            }

        });
    }

    @Override
    public int getItemCount() {
        return homeCateList.size();
    }


    private void get_subcateory(JSONArray response,RecyclerView  recyclerView,String cat_id) {

        subCatModels.clear();

        JSONArray array  = response;

        if (array.length()==0){
            Intent intent = new Intent(context, CategoryPage.class);
            intent.putExtra("cat_id",cat_id);
            context.startActivity(intent);
        }
        else {


            for (int i = 0; i < array.length(); i++) {

                try {
                    JSONObject object = array.getJSONObject(i);

                    Log.d("sdf", response.toString());
                    object = array.getJSONObject(i);

                    SubCatModel model = new SubCatModel();


                    model.setDetail(object.getString("description"));
                    model.setId(object.getString("cat_id"));
                    model.setImages(object.getString("image"));
                    model.setName(object.getString("title"));

                    if (object.has("subchild")) {
                        model.setSub_array(object.getJSONArray("subchild"));
                    }


                    subCatModels.add(model);

                    SubCatAdapter cateAdapter = new SubCatAdapter(subCatModels, context);
                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    recyclerView.setAdapter(cateAdapter);
                    cateAdapter.notifyDataSetChanged();


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


        }
    }

}
