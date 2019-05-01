package com.example.mvvmmeeting.Activities;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mvvmmeeting.Recycler_Adapter_Members;
import com.example.mvvmmeeting.MemberModel;
import com.example.mvvmmeeting.R;

import io.realm.Realm;
import io.realm.RealmResults;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

public class MembersActivity extends AppCompatActivity {

    private static final int CONTACT_PICKER_DOIALOG = 1001;
    private static final int ACCESS_CONTACT_REQUEST = 101;
    public static RecyclerView recyclerMembers;
    LinearLayoutManager managerMembers;
    ImageView btnAdd;
    public int parentId = -1;
    RealmResults<MemberModel>members;
    public static TextView txtNoMember;
    ImageView btnBack;
    Recycler_Adapter_Members recycler_adapter_members;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_members);


        Intent intent = getIntent();
        parentId = intent.getIntExtra("parentId", -1);

        getContactFromRealm();
        AddingMember();


      /*  recyclerMembers = findViewById(R.id.recyclerMembers);
        managerMembers = new LinearLayoutManager(
                MembersActivity.this, LinearLayout.VERTICAL, false
        );
        recycler_adapter_members = new Recycler_Adapter_Members(
                MembersActivity.this
                , members, members.get(0).getParentId());
        recyclerMembers.setHasFixedSize(true);
        recyclerMembers.setLayoutManager(managerMembers);
        recyclerMembers.setAdapter(recycler_adapter_members);*/
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    private void AddingMember() {
        btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(MembersActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.custom_dialog_add_member);


                TextView btnFromContact = dialog.findViewById(R.id.btnFromContact);
                TextView btnManually= dialog.findViewById(R.id.btnManually);


                btnFromContact.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (ContextCompat.checkSelfPermission(MembersActivity.this
                        , Manifest.permission.READ_CONTACTS)== PackageManager.PERMISSION_GRANTED
                            && ContextCompat.checkSelfPermission(MembersActivity.this,
                                Manifest.permission.WRITE_CONTACTS)
                                == PackageManager.PERMISSION_GRANTED){
                            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                            startActivityForResult(intent,CONTACT_PICKER_DOIALOG);
                        }else {
                            getPermissionToAccessContact();
                        }
                        dialog.dismiss();
                    }
                });

                btnManually.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getMemberInfo();
                        dialog.dismiss();
                    }
                });

                dialog.show();

            }
        });
    }


    public void getMemberInfo(){
        final Dialog dialog = new Dialog(MembersActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_getmember);
        CardView btnAdd = dialog.findViewById(R.id.btnAdd);
        final EditText edtName = dialog.findViewById(R.id.edtName);
        final EditText edtNumber = dialog.findViewById(R.id.edtNumber);


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtName.getText().toString();
                String number = edtNumber.getText().toString();

                if (name == "") {

                }else {
                    Intent intent = new Intent(Intent.ACTION_INSERT);
                    intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
                    intent.putExtra(ContactsContract.Intents.Insert.NAME, name);
                    intent.putExtra(ContactsContract.Intents.Insert.PHONE, number);
                    startActivity(intent);
                    dialog.dismiss();
                }
            }
        });
        dialog.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if ( resultCode == RESULT_OK ){

            if ( requestCode == CONTACT_PICKER_DOIALOG ){

                Uri result = data.getData();
                String id = result.getLastPathSegment();
                Log.i("test123","result:"+result.toString());
                getContactInfo(id);

            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void getContactInfo(String id){
        Cursor cursor = null;
        String result = "";
        String number ="";
        String mobileNumber = "";


        try {
            cursor = getContentResolver().query(ContactsContract
                            .Data.CONTENT_URI,
                    null,
                    ContactsContract.Data.CONTACT_ID + "=?",
                    new String[] { id },
                    null);

            while (cursor.moveToNext()) {

                result = cursor.getString(cursor.getColumnIndex
                        (ContactsContract.Data.DISPLAY_NAME));

                number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                int type = cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
                switch (type) {
                    case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
                        // do something with the Home number here...
                        Log.i("test123","number home:" +number);
                        break;
                    case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
                        // do something with the Mobile number here...
                        Log.i("test123","number mobile:" +number);
                        mobileNumber = number;
                        break;
                    case ContactsContract.CommonDataKinds.Phone.TYPE_WORK:
                        // do something with the Work number here...
                        Log.i("test123","number work:" +number);
                        break;
                }
            }

        } catch (Exception e) {

            Log.i("test12","Error reading contact",e);

        }

        //mContactName = result;
        Log.i("test123","result:"+result.toString() );
        Log.i("test123","number:"+number );
        Log.i("test123","number:"+mobileNumber );


        boolean isExist = false;
        for ( int i=0 ; i<members.size() ; i++ ){
            if ( mobileNumber.equals(members.get(i).getMemberNumber())){
                Log.i("test123","isExist change to true");
                isExist = true;
            }

        }

        if ( !isExist ){
            addContactToRealm(result,mobileNumber);
        }else {
            Toast.makeText(MembersActivity.this,"این عضو قبلا اضافه شده است.",Toast.LENGTH_SHORT).show();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void getPermissionToAccessContact(){

        if (ContextCompat.checkSelfPermission(MembersActivity.this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(MembersActivity.this, Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED ){

            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS},
                    ACCESS_CONTACT_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if ( requestCode == ACCESS_CONTACT_REQUEST){

            if ( grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED ){

                Intent pickerIntent = new Intent(Intent.ACTION_PICK,
                        ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(pickerIntent,CONTACT_PICKER_DOIALOG);

            }else{
                Toast.makeText(this, "برای اضافه کردن به این دسترسی نیاز است.", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void addContactToRealm(final String name, final String number){

        Realm realm =Realm.getDefaultInstance();
        final int memberId = getNextKey(realm);

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                MemberModel model = realm.createObject(MemberModel.class,memberId);
                model.setParentId(parentId);
                model.setMemberName(name);
                model.setMemberNumber(number);
                model.setMemberPeresent(false);

            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Log.i("test123","in success: contact add to member");
                getContactFromRealm();
                managerMembers = new LinearLayoutManager(
                        MembersActivity.this, LinearLayout.VERTICAL, false
                );
                recycler_adapter_members = new Recycler_Adapter_Members(
                        MembersActivity.this
                        , members, members.get(0).getParentId()
                );
                recyclerMembers.setHasFixedSize(true);
                recyclerMembers.setLayoutManager(managerMembers);
                recyclerMembers.setAdapter(recycler_adapter_members);
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Log.i("test123","in error:"+error.toString());
            }
        });

    }

    private void getContactFromRealm(){

        recyclerMembers = findViewById(R.id.recyclerMembers);
        txtNoMember = findViewById(R.id.txtNoMember);

        Realm realm = Realm.getDefaultInstance();
        members = realm.where(MemberModel.class).equalTo("parentId",parentId).findAll();

        for( int i=0 ; i<members.size() ; i++ ){
            Log.i("test123","parentId:"+members.get(i).getParentId() + " name:"+members.get(i).getMemberName() + " number:"+members.get(i).getMemberNumber());
        }

        if ( members.size() > 0 ){
            txtNoMember.setVisibility(View.INVISIBLE);
            recyclerMembers.setVisibility(View.VISIBLE);
            managerMembers = new LinearLayoutManager(
                    MembersActivity.this, LinearLayout.VERTICAL, false);
            recycler_adapter_members = new Recycler_Adapter_Members(
                    MembersActivity.this
                    , members, members.get(0).getParentId());
            recyclerMembers.setHasFixedSize(true);
            recyclerMembers.setLayoutManager(managerMembers);
            recyclerMembers.setAdapter(recycler_adapter_members);
        }else {
            txtNoMember.setVisibility(View.VISIBLE);
            recyclerMembers.setVisibility(View.INVISIBLE);
        }

    }

    public int getNextKey(Realm realm) {
        try {
            Number number = realm.where(MemberModel.class).max("memberId");
            if (number != null) {
                return number.intValue() + 1;
            } else {
                return 0;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            return 0;
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(MembersActivity.this,MainActivity.class);
        intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(intent);
    }

}
