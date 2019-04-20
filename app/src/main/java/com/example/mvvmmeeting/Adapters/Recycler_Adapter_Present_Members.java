package com.example.mvvmmeeting.Adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mvvmmeeting.Activities.MembersActivity;
import com.example.mvvmmeeting.MemberModel;
import com.example.mvvmmeeting.R;

import io.realm.Realm;
import io.realm.RealmResults;

public class Recycler_Adapter_Present_Members extends
        RecyclerView.Adapter<Recycler_Adapter_Present_Members.myViewHoler> {


    Activity activity;
    RealmResults<MemberModel> memebrs;
    int parentId;


    public Recycler_Adapter_Present_Members(Activity activity, RealmResults<MemberModel> memebrs, int parentId) {
        this.activity = activity;
        this.memebrs = memebrs;
        this.parentId = parentId;
    }

    @NonNull
    @Override
    public Recycler_Adapter_Present_Members.myViewHoler onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.recycler_presenet_member_list
                , viewGroup, false);
        return new myViewHoler(inflate);

    }

    @Override
    public void onBindViewHolder(@NonNull Recycler_Adapter_Present_Members.myViewHoler myViewHoler, final int i) {
        myViewHoler.chbMember.setText(memebrs.get(i).getMemberName());
        if (memebrs.get(i).isMemberPeresent()) {
            myViewHoler.chbMember.setChecked(true);
        }
        myViewHoler.chbMember.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                        Realm realm = Realm.getDefaultInstance();
                        realm.executeTransactionAsync(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                MemberModel model = realm.where(MemberModel.class)
                                        .equalTo("memberId", memebrs.get(i).getMemberId())
                                        .findFirst();
                                if (isChecked) {
                                    model.setMemberPeresent(true);
                                }else{
                                    model.setMemberPeresent(false);
                                }
                            }
                        });
                    }
                }
        );




        myViewHoler.linearMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(activity);

                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.custom_dialog_show_members_info);

                ImageView btnCall = dialog.findViewById(R.id.btnCall);
                ImageView btnMessage = dialog.findViewById(R.id.btnMessage);
                ImageView btnDelete = dialog.findViewById(R.id.btnDelete);
                TextView txtName = dialog.findViewById(R.id.txtName);
                TextView txtNumber = dialog.findViewById(R.id.txtNumber);

                txtName.setText(memebrs.get(i).getMemberName());
                txtNumber.setText(memebrs.get(i).getMemberNumber());


                // btn call
                btnCall.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String uri = "tel:" + memebrs.get(i).getMemberNumber().trim();
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse(uri));
                        activity.startActivity(intent);
                        dialog.dismiss();
                    }
                });

                //btn message
                btnMessage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(Intent.ACTION_SENDTO);
                        intent.setData(Uri.parse("smsto:" + Uri.encode(memebrs.get(i).getMemberNumber())));
                        activity.startActivity(intent);

                        dialog.dismiss();
                    }
                });


                //btn delete
                btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        removeMemberFromRealm(memebrs.get(i).getMemberId());

                        RealmResults<MemberModel> newMembers = getContactFromRealm();
                        updateRecycler(newMembers);
                        dialog.dismiss();
                    }
                });


                dialog.show();

            }
        });



    }

    @Override
    public int getItemCount() {
        return memebrs.size();
    }

    public class myViewHoler extends RecyclerView.ViewHolder {
        CheckBox chbMember;
        LinearLayout linearMember;

        public myViewHoler(@NonNull View itemView) {
            super(itemView);
            chbMember = itemView.findViewById(R.id.chbMember);
            linearMember = itemView.findViewById(R.id.linearMember);
        }

    }


    private void removeMemberFromRealm(final int memberId){

        Realm realm = Realm.getDefaultInstance();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<MemberModel> rows = realm.where(MemberModel.class).
                        equalTo("memberId",memberId).findAll();
                rows.deleteFirstFromRealm();
            }
        });

    }

    public void updateRecycler(RealmResults<MemberModel> members)
    {
        this.memebrs=members;
        notifyDataSetChanged();
    }

    private RealmResults<MemberModel> getContactFromRealm(){

        MembersActivity.txtNoMember = activity.findViewById(R.id.txtNoMember);
        MembersActivity.recyclerMembers = activity.findViewById(R.id.recyclerMembers);

        Realm realm = Realm.getDefaultInstance();
        RealmResults<MemberModel> infos = realm.where(MemberModel.class).
                equalTo("parentId",parentId).findAll();

        if ( infos.size() <= 0 ){

            MembersActivity.txtNoMember.setVisibility(View.VISIBLE);
            MembersActivity.recyclerMembers.setVisibility(View.INVISIBLE);
        }

        return infos;

    }


}
