package com.rch.etawah.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.rch.etawah.ModelClass.CategoryGrid;
import com.rch.etawah.R;

import java.util.List;

public class DealsAdapter extends RecyclerView.Adapter<DealsAdapter.MyViewHolder> {

    private List<CategoryGrid> CategoryGridList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView prodNAme;
        ImageView image;

        public MyViewHolder(View view) {
            super(view);
            prodNAme = (TextView) view.findViewById(R.id.listName);
            image = (ImageView) view.findViewById(R.id.listImages);
        }
    }


    public DealsAdapter(List<CategoryGrid> CategoryGridList) {
        this.CategoryGridList = CategoryGridList;
    }

    @Override
    public DealsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_deals, parent, false);

        return new DealsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DealsAdapter.MyViewHolder holder, int position) {
        CategoryGrid cc = CategoryGridList.get(position);
        holder.prodNAme.setText(cc.getName());
        holder.image.setImageResource(R.drawable.splashicon);
    }

    @Override
    public int getItemCount() {
        return CategoryGridList.size();
    }
}
