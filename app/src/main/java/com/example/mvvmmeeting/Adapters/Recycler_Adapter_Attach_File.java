package com.example.mvvmmeeting.Adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mvvmmeeting.Models.FileModel;
import com.example.mvvmmeeting.R;

import io.realm.RealmResults;

public class Recycler_Adapter_Attach_File extends
        RecyclerView.Adapter<Recycler_Adapter_Attach_File.ViewHolder> {


    Activity activity;
    RealmResults<FileModel> models;


    public Recycler_Adapter_Attach_File(Activity activity, RealmResults<FileModel> models) {
        this.activity = activity;
        this.models = models;
    }

    @NonNull
    @Override
    public Recycler_Adapter_Attach_File.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_attach_file
                , viewGroup, false);

        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull Recycler_Adapter_Attach_File.ViewHolder viewHolder, int i) {
        FileModel fileModel = models.get(i);
        viewHolder.txtFileName.setText(fileModel.getFileName());
        viewHolder.txtFilePath.setText(fileModel.getFilePath());

    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        RelativeLayout btnSelect;
        TextView txtFileName;
        TextView txtFilePath;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            btnSelect = itemView.findViewById(R.id.btnSelect);
            txtFileName = itemView.findViewById(R.id.txtFileName);
            txtFilePath = itemView.findViewById(R.id.txtFilePath);
        }
    }

}
