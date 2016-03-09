package com.silentdevelopers.facultyrater.main_body;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.silentdevelopers.facultyrater.R;
import com.silentdevelopers.facultyrater.StartActivity;

import android.content.res.Resources;
import android.widget.Toast;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

/**
 * Created by varun on 28/2/16.
 */

public class FacultyRecyclerAdapter extends RecyclerView.Adapter<FacultyRecyclerAdapter.RecyclerViewHolder>{

    ArrayList<DataProviderFaculty> arrayList;

    Context context;
    MainBodyActivity parentAct;

    public FacultyRecyclerAdapter(ArrayList<DataProviderFaculty> arrayList, MainBodyActivity parentAct){
        this.arrayList = arrayList;
        this.parentAct = parentAct;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        context = parent.getContext();
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_faculty_item,parent,false);
        RecyclerViewHolder recyclerViewHolderM=new RecyclerViewHolder(view);

        final RecyclerView recyclerView = (RecyclerView)parent.findViewById(R.id.recycler_view_faculty_list);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int itemPosition = recyclerView.getChildLayoutPosition(v);
                DataProviderFaculty dataProviderFaculty = arrayList.get(itemPosition);
                Toast.makeText(context, dataProviderFaculty.getName(), Toast.LENGTH_SHORT).show();

                parentAct.handleRecycleClick(dataProviderFaculty.getId());
            }
        });

        return recyclerViewHolderM;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {

        DataProviderFaculty dataProvider=arrayList.get(position);
        holder.textVeiw_name.setText(dataProvider.getName());
        holder.textVeiw_des.setText(dataProvider.getDesignation());
        holder.textVeiw_exp.setText(dataProvider.getExperience());
        holder.textVeiw_time.setText(dataProvider.getTime());
        holder.imageView.setImageBitmap(RoundedImageView.decodeSampledBitmapFromResource
                (context.getResources(), dataProvider.getImg_res(), 300, 300));
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