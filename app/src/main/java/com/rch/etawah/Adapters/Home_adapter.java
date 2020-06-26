package com.rch.etawah.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.rch.etawah.Config.BaseURL;
import com.rch.etawah.ModelClass.Category_model;
import com.rch.etawah.R;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Rajesh Dabhi on 22/6/2017.
 */

public class Home_adapter extends RecyclerView.Adapter<Home_adapter.MyViewHolder> {

    private List<Category_model> modelList;
    private Context context;
    String language;
    SharedPreferences preferences;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView image;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.tv_home_title);
            image = (ImageView) view.findViewById(R.id.iv_home_img);
        }
    }

    public Home_adapter(List<Category_model> modelList) {
        this.modelList = modelList;
    }

    @Override
    public Home_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_home_rv, parent, false);

        context = parent.getContext();

        return new Home_adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(Home_adapter.MyViewHolder holder, int position) {
        Category_model mList = modelList.get(position);

        Glide.with(context)
                .load(BaseURL.IMG_URL + mList.getImage())
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(holder.image);
        preferences = context.getSharedPreferences("lan", MODE_PRIVATE);
        language=preferences.getString("language","");
            holder.title.setText(mList.getTitle());


    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

}

