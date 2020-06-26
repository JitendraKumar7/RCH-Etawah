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

import com.rch.etawah.activity.CategoryPage;
import com.rch.etawah.ModelClass.SubChildCatModel;
import com.rch.etawah.R;

import java.util.ArrayList;
import java.util.List;

public class SubChildCatAdapter extends RecyclerView.Adapter<SubChildCatAdapter.MyViewHolder> {

    private List<SubChildCatModel> subChildCatModels = new ArrayList<>();
    Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView prodNAme, pdetails;
        ImageView image;
        RecyclerView recyclerSubCate;
        CardView cardView;

        public MyViewHolder(View view) {
            super(view);
            prodNAme = (TextView) view.findViewById(R.id.pNAme);
            pdetails = (TextView) view.findViewById(R.id.pDetails);
            image = (ImageView) view.findViewById(R.id.Pimage);
            recyclerSubCate = view.findViewById(R.id.recyclerSubCate);
            cardView = view.findViewById(R.id.cardView);
        }
    }


    public SubChildCatAdapter(List<SubChildCatModel> subChildCatModels, Context context) {
        this.subChildCatModels = subChildCatModels;
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
        SubChildCatModel cc = subChildCatModels.get(position);
        holder.prodNAme.setText(cc.getName());
        holder.pdetails.setText(cc.getDetail());
        holder.image.setImageResource(R.drawable.splashicon);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(context, CategoryPage.class);
                intent.putExtra("cat_id",cc.getId());
                context.startActivity(intent);

            }

        });
    }

    @Override
    public int getItemCount() {
        return subChildCatModels.size();

    }
}
