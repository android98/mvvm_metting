package com.example.mvvmmeeting.Adapters;

import android.app.Activity;
import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mvvmmeeting.Activities.ActionsActivity;
import com.example.mvvmmeeting.Models.ActionModel;
import com.example.mvvmmeeting.R;

import io.realm.RealmResults;
import ir.hamsaa.persiandatepicker.util.PersianCalendar;

public class Recycler_Adapter_Show_Actions extends
        RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private static final int LAYOUT_ONE = 0;
    private static final int LAYOUT_TWO = 1;
    Activity activity;
    RealmResults<ActionModel> models;


    public Recycler_Adapter_Show_Actions(Activity activity, RealmResults<ActionModel> models) {
        this.activity = activity;
        this.models = models;
    }

    @Override
    public int getItemViewType(int position) {

        if( models.get(position).getState() == 0 ){
            return LAYOUT_ONE;
        }else {
            return LAYOUT_TWO;
        }

    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (i == LAYOUT_ONE) {
            View view = LayoutInflater.from(
                    viewGroup.getContext()
            ).inflate(R.layout.recycler_show_action, viewGroup, false);
            return new viewHolderAction(view);
        }else {
            View views = LayoutInflater.from(
                    viewGroup.getContext()
            ).inflate(R.layout.recycler_show_description, viewGroup, false);
            return new ViewHolderDescription(views);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        final ActionModel info = models.get(i);
        if( viewHolder instanceof viewHolderAction ){

            final PersianCalendar calendar = new PersianCalendar();
            calendar.setTime(info.getActionDate());

            ((viewHolderAction)viewHolder).txtTitle.setText(info.getActionTitle());
            ((viewHolderAction)viewHolder).txtPerformer.setText(info.getActionPerformerName());
            ((viewHolderAction)viewHolder).txtDate.setText(calendar.getPersianLongDate());

            //onClick listener for update
            ((viewHolderAction)viewHolder).btnAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    ActionsActivity activity = new ActionsActivity();
                    activity.updateActionItems(activity,info);
                    notifyDataSetChanged();
                }
            });

            // onClicklisetener for Remove
            ((viewHolderAction)viewHolder).btnAction.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    final Dialog dialog = new Dialog(activity);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.custom_dialog_remove);

                    TextView btnYes = dialog.findViewById(R.id.btnYes);
                    TextView btnNo = dialog.findViewById(R.id.btnNo);

                    btnYes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActionsActivity.removeItemFromRealm(info.getActionId());
                            dialog.dismiss();
                            notifyDataSetChanged();
                        }
                    });

                    btnNo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();

                    return true;
                }
            });

        }else {
            ((ViewHolderDescription)viewHolder).txtDescription.setText(info.getActionDescription());


            // onClick listener for update
            ((ViewHolderDescription)viewHolder).btnaAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    final Dialog dialog = new Dialog(activity);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.custom_dialog_get_description);

                    final EditText edtDescription = dialog.findViewById(R.id.edtDescription);
                    ImageView btnSave = dialog.findViewById(R.id.btnSave);

                    edtDescription.setText(info.getActionDescription());

                    //save data to realm
                    btnSave.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            ActionsActivity.updateItems(info.getActionId(),"","","",edtDescription.getText().toString());
                            notifyDataSetChanged();
                            dialog.dismiss();
                        }
                    });


                    Window windowAction = dialog.getWindow();
                    windowAction.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    dialog.show();
                }
            });

            // onclick listener for remove
            ((ViewHolderDescription)viewHolder).btnaAction.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    final Dialog dialog = new Dialog(activity);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.custom_dialog_remove);

                    TextView btnYes = dialog.findViewById(R.id.btnYes);
                    TextView btnNo = dialog.findViewById(R.id.btnNo);

                    btnYes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActionsActivity.removeItemFromRealm(info.getActionId());
                            dialog.dismiss();
                            notifyDataSetChanged();
                        }
                    });


                    btnNo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();

                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class ViewHolderDescription extends RecyclerView.ViewHolder {
        TextView txtDescription;
        RelativeLayout btnaAction;

        public ViewHolderDescription(@NonNull View itemView) {
            super(itemView);
            txtDescription = itemView.findViewById(R.id.txtDescription);
            btnaAction = itemView.findViewById(R.id.btnAction);


        }
    }

    public class viewHolderAction extends RecyclerView.ViewHolder{

        TextView txtTitle;
        TextView txtPerformer;
        TextView txtDate;
        LinearLayout btnAction;
        public viewHolderAction(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtPerformer = itemView.findViewById(R.id.txtPerformer);
            txtDate = itemView.findViewById(R.id.txtDate);
            btnAction = itemView.findViewById(R.id.btnAction);
        }
    }




}
