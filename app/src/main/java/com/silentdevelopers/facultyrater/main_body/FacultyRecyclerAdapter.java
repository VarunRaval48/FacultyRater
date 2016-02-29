package com.silentdevelopers.facultyrater.main_body;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.silentdevelopers.facultyrater.R;
import android.content.res.Resources;

import java.util.ArrayList;

/**
 * Created by varun on 28/2/16.
 */

public class FacultyRecyclerAdapter extends RecyclerView.Adapter<FacultyRecyclerAdapter.RecyclerViewHolder>{

    ArrayList<DataProviderFaculty> arrayList;

    public FacultyRecyclerAdapter(ArrayList<DataProviderFaculty> arrayList){
        this.arrayList = arrayList;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_faculty_item,parent,false);
        RecyclerViewHolder recyclerViewHolderM=new RecyclerViewHolder(view);

        return recyclerViewHolderM;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {

        DataProviderFaculty dataProvider=arrayList.get(position);
        holder.textVeiw_name.setText(dataProvider.getName());
        holder.textVeiw_des.setText(dataProvider.getDesignation());
        holder.textVeiw_exp.setText(dataProvider.getExperience());
        holder.textVeiw_time.setText(dataProvider.getTime());
//        holder.imageView.setImageBitmap(RoundedImageView.decodeSampledBitmapFromResource
//                (MainBodyActivity.getActivity().getResources(), dataProvider.getImg_res(), 300, 300));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView textVeiw_name, textVeiw_des, textVeiw_exp, textVeiw_time;
        ImageView imageView;

        public RecyclerViewHolder(View itemView) {
            super(itemView);

            imageView = (ImageView)itemView.findViewById(R.id.img_single_item);
            textVeiw_name = (TextView)itemView.findViewById(R.id.textView_name);
            textVeiw_des = (TextView)itemView.findViewById(R.id.textView_designation);
            textVeiw_exp = (TextView)itemView.findViewById(R.id.textView_experience);
            textVeiw_time = (TextView)itemView.findViewById(R.id.textView_last_rated_tv);

        }
    }
}