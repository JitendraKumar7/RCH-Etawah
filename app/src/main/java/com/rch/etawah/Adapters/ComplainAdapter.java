package com.rch.etawah.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.rch.etawah.ModelClass.ComplainModel;
import com.rch.etawah.R;

import java.util.List;

public class ComplainAdapter extends RecyclerView.Adapter<ComplainAdapter.MyViewHolder> {

    private List<ComplainModel> modelList;

    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;

        public MyViewHolder(View view) {

            super(view);
            title = (TextView) view.findViewById(R.id.ask);

        }
    }

    public ComplainAdapter(List<ComplainModel> modelList) {
        this.modelList = modelList;
    }

    @Override
    public ComplainAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cancel_listitem, parent, false);

        context = parent.getContext();

        return new ComplainAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ComplainAdapter.MyViewHolder holder, final int position) {
        ComplainModel mList = modelList.get(position);





        holder.title.setText(mList.getReason());
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
