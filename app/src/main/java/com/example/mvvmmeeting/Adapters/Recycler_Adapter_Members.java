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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mvvmmeeting.Activities.MembersActivity;
import com.example.mvvmmeeting.Models.MemberModel;
import com.example.mvvmmeeting.R;

import io.realm.Realm;
import io.realm.RealmResults;

public class Recycler_Adapter_Members extends RecyclerView.Adapter<
        Recycler_Adapter_Members.myViewHolder> {


    Activity activity;
    RealmResults<MemberModel> members;
    int parentId;

    public Recycler_Adapter_Members(Activity activity, RealmResults<MemberModel> members, int parentId) {
        this.activity = activity;
        this.members = members;
        this.parentId = parentId;
    }

    @NonNull
    @Override
    public Recycler_Adapter_Members.myViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.recycler_members_list, viewGroup, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Recycler_Adapter_Members.myViewHolder myViewHolder, final int i) {
        myViewHolder.txtMemberName.setText(members.get(i).getMemberName());
        myViewHolder.linearMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(activity);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.custom_dialog_show_members_info);

                ImageView btnCall = dialog.findViewById(R.id.btnCall);
                ImageView btnMessage = dialog.findViewById(R.id.btnMessage);
                ImageView btnDelete = dialog.findViewById(R.id.btnDelete);
                TextView txtName = dialog.findViewById(R.id.txtName);
                TextView txtNumber = dialog.findViewById(R.id.txtNumber);

                txtName.setText(members.get(i).getMemberName());
                txtNumber.setText(members.get(i).getMemberNumber());


                // btn call
                btnCall.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String uri = "tel:" + members.get(i).getMemberNumber().trim();
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
                        intent.setData(Uri.parse("smsto:" + Uri.encode(members.get(i).getMemberNumber())));
                        activity.startActivity(intent);

                        dialog.dismiss();
                    }
                });


                //btn delete
                btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        removeMemberFromRealm(members.get(i).getMemberId());

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
        return members.size();
    }


    public class myViewHolder extends RecyclerView.ViewHolder{

        TextView txtMemberName;
        LinearLayout linearMember;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            txtMemberName = itemView.findViewById(R.id.txtMemberName);
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
        this.members=members;
        notifyDataSetChanged();
    }

    private RealmResults<MemberModel> getContactFromRealm(){

        Realm realm = Realm.getDefaultInstance();
        RealmResults<MemberModel> infos = realm.where(MemberModel.class).equalTo(
                "parentId",parentId).findAll();

        if ( infos.size() <= 0 ){

            MembersActivity.txtNoMember.setVisibility(View.VISIBLE);
            MembersActivity.recyclerMembers.setVisibility(View.INVISIBLE);
        }

        return infos;

    }
}
