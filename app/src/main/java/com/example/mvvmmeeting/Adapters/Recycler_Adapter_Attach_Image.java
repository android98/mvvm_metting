package com.example.mvvmmeeting.Adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mvvmmeeting.Activities.AttachImageActivity;
import com.example.mvvmmeeting.Activities.FullScreenViewActivity;
import com.example.mvvmmeeting.Models.ImageModel;
import com.example.mvvmmeeting.R;

import java.io.File;

import io.realm.RealmResults;

public class Recycler_Adapter_Attach_Image extends RecyclerView.Adapter<
        Recycler_Adapter_Attach_Image.myViewHolder> {

    RealmResults<ImageModel> infos;
    Activity activity;
    int parentId;


    public Recycler_Adapter_Attach_Image(RealmResults<ImageModel> infos, Activity activity, int parentId) {
        this.infos = infos;
        this.activity = activity;
        this.parentId = parentId;
    }

    @NonNull
    @Override
    public Recycler_Adapter_Attach_Image.myViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_attach_image,
                viewGroup, false);
        Log.d("test", "onCreate: "+"come in recycler ADapter onCreateView");

        return new myViewHolder(inflate);

    }

    @Override
    public void onBindViewHolder(@NonNull Recycler_Adapter_Attach_Image.myViewHolder myViewHolder, final int i) {
        final File file = new File(infos.get(i).getImagePath());
        Log.d("akssssssssss", "onBindViewHolder: "+file.toString());
        Uri uri = Uri.fromFile(file);
        Glide.with(activity)
                .load(uri)
                .into(myViewHolder.imgSquire);
        myViewHolder.imgSquire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, FullScreenViewActivity.class);
                intent.putExtra("position", i);
                intent.putExtra("parentId",parentId);
                activity.startActivity(intent);
            }
        });


        myViewHolder.imgSquire.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {


                final Dialog dialog = new Dialog(activity);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.custom_dialog_remove_image);

                Window window = dialog.getWindow();
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);


                TextView btnYes = dialog.findViewById(R.id.btnYes);
                TextView btnNo = dialog.findViewById(R.id.btnNo);

                btnNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        int imageId = infos.get(i).getImageId();
                        AttachImageActivity.removeImageFroimRealm(imageId,file,parentId);
                        dialog.dismiss();

                        notifyDataSetChanged();
                    }
                });

                dialog.show();

                return true;
            }
        });


    }

    @Override
    public int getItemCount() {
        return infos.size();
    }


    public class myViewHolder extends RecyclerView.ViewHolder{

        ImageView imgSquire;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            imgSquire = itemView.findViewById(R.id.imgSquire);

        }
    }
}
