package com.example.mvvmmeeting.Activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mvvmmeeting.Adapters.Recycler_Adapter_Show_Actions;
import com.example.mvvmmeeting.Models.ActionModel;
import com.example.mvvmmeeting.R;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;
import ir.hamsaa.persiandatepicker.Listener;
import ir.hamsaa.persiandatepicker.PersianDatePickerDialog;
import ir.hamsaa.persiandatepicker.util.PersianCalendar;

public class ActionsActivity extends AppCompatActivity {


    private int parentId = -1;
    private static final int CONTACT_PICKER_RESULT = 1001;
    private static final int CONTACT_PICKER_RESULT_UPDATE = 1002;
    ImageView btnMenu;
    TextView txtShowContact;
    TextView txtShowContactUpdate;
    TextView txtNoAction;
    RecyclerView recyclerActions;
    LinearLayoutManager managerActions;
    Recycler_Adapter_Show_Actions adapterActions;
    RealmResults<ActionModel> actions = null;
    String title = "";
    String performerName = "";
    String performerNumber = "";
    Date actionDate = null;
    Date updateResultDate = null;
    String updatePerformerName = "";
    String updatePerformerNumber = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actions);


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        parentId = bundle.getInt("parentId");

        getActionsFromRealm();
        injectTopToolbar();
    }


    private void injectRecyclerView() {

        recyclerActions = findViewById(R.id.recyclerActions);
        managerActions = new LinearLayoutManager(ActionsActivity.this, LinearLayoutManager.VERTICAL, false);
        adapterActions = new Recycler_Adapter_Show_Actions(this, actions);

        recyclerActions.setHasFixedSize(true);
        recyclerActions.setLayoutManager(managerActions);
        recyclerActions.setAdapter(adapterActions);

    }

    private void injectTopToolbar() {

        // popup menu
        btnMenu = findViewById(R.id.btnMenu);
        final Context myContext = new ContextThemeWrapper(ActionsActivity.this, R.style.menuStyle);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PopupMenu popup = new PopupMenu(myContext, btnMenu);

                //Inflating the Popup using xml file
                popup.getMenuInflater()
                        .inflate(R.menu.actions_menu, popup.getMenu());
                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {
                            // add action
                            case R.id.item_add_action:
                                final Dialog dialogAction = new Dialog(ActionsActivity.this);
                                dialogAction.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialogAction.setContentView(R.layout.custom_dialog_add_action);

                                ImageView btnSaveAction = dialogAction.findViewById(R.id.btnSaveAction);
                                LinearLayout btnGetDate = dialogAction.findViewById(R.id.btnGetDate);
                                LinearLayout btnGetContact = dialogAction.findViewById(R.id.btnGetContact);
                                final TextView txtShowDate = dialogAction.findViewById(R.id.txtShowDate);
                                txtShowContact = dialogAction.findViewById(R.id.txtShowContact);
                                final EditText txtTitle = dialogAction.findViewById(R.id.txtTitle);


                                // btn getDate
                                btnGetDate.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        getDateDialog(txtShowDate);
                                    }
                                });

                                // btn get contact
                                btnGetContact.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        final Dialog addMemberDialog = new Dialog(ActionsActivity.this);
                                        addMemberDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                        addMemberDialog.setContentView(R.layout.custom_dialog_add_member);

                                        TextView btnManually = addMemberDialog.findViewById(R.id.btnManually);
                                        TextView btnFromContact = addMemberDialog.findViewById(R.id.btnFromContact);

                                        // select contact
                                        btnFromContact.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                addMemberDialog.dismiss();
                                                Intent pickerIntent = new Intent(Intent.ACTION_PICK,
                                                        ContactsContract.Contacts.CONTENT_URI);
                                                startActivityForResult(pickerIntent, CONTACT_PICKER_RESULT);
                                            }
                                        });

                                        // add contact
                                        btnManually.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {

                                                getContactInfoAndSaveToContacts();
                                                addMemberDialog.dismiss();
                                            }
                                        });

                                        addMemberDialog.show();


                                    }
                                });


                                // btn save
                                btnSaveAction.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        title = txtTitle.getText().toString();
                                        if (title.matches("")) {
                                            Toast.makeText(ActionsActivity.this, "عنوان باید وارد شود.", Toast.LENGTH_SHORT).show();
                                        } else if (performerName.matches("")) {
                                            Toast.makeText(ActionsActivity.this, "انجام دهنده را انتخاب کنید.", Toast.LENGTH_SHORT).show();
                                        } else if (actionDate == null) {
                                            Toast.makeText(ActionsActivity.this, "تاریخ انجام را مشخص کنید.", Toast.LENGTH_SHORT).show();
                                        } else {

                                            Realm realm = Realm.getDefaultInstance();
                                            final int actionId = getNextKey(realm);

                                            realm.executeTransactionAsync(new Realm.Transaction() {
                                                @Override
                                                public void execute(Realm realm) {

                                                    ActionModel model = realm.createObject(ActionModel.class, actionId);
                                                    model.setState(0);
                                                    model.setParentId(parentId);
                                                    model.setActionTitle(title);
                                                    model.setActionPerformerName(performerName);
                                                    model.setActionPerformerNumber(performerNumber);
                                                    model.setActionDate(actionDate);
                                                }
                                            }, new Realm.Transaction.OnSuccess() {
                                                @Override
                                                public void onSuccess() {
                                                    Log.i("test123", "action is added to the realm db");
                                                    getActionsFromRealm();
                                                }
                                            }, new Realm.Transaction.OnError() {
                                                @Override
                                                public void onError(Throwable error) {
                                                    Log.i("test123", "cant add description in the realm db");
                                                }
                                            });

                                            dialogAction.dismiss();
                                        }


                                    }
                                });


                                Window windowAction = dialogAction.getWindow();
                                windowAction.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                dialogAction.show();
                                return true;

                            // add description
                            case R.id.item_add_description:
                                final Dialog dialog = new Dialog(ActionsActivity.this);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setContentView(R.layout.custom_dialog_get_description);

                                final EditText edtDescription = dialog.findViewById(R.id.edtDescription);
                                ImageView btnSave = dialog.findViewById(R.id.btnSave);

                                //btn save
                                btnSave.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        Realm realm = Realm.getDefaultInstance();
                                        final int actionId = getNextKey(realm);
                                        final String description = edtDescription.getText().toString();

                                        realm.executeTransactionAsync(new Realm.Transaction() {
                                            @Override
                                            public void execute(Realm realm) {

                                                ActionModel model = realm.createObject(ActionModel.class, actionId);
                                                model.setState(1);
                                                model.setParentId(parentId);
                                                model.setActionDescription(description);
                                            }
                                        }, new Realm.Transaction.OnSuccess() {
                                            @Override
                                            public void onSuccess() {
                                                Log.i("test123", "description is added to the realm db");
                                            }
                                        }, new Realm.Transaction.OnError() {
                                            @Override
                                            public void onError(Throwable error) {
                                                Log.i("test123", "cant add description in the realm db");
                                            }
                                        });

                                        dialog.dismiss();
                                    }
                                });

                                Window window = dialog.getWindow();
                                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                dialog.show();
                                return true;

                            default:
                                return true;
                        }
                    }
                });
                popup.show();
            }
        });

    }

    private void getDateDialog(final TextView textView) {


        PersianDatePickerDialog perDialog = new PersianDatePickerDialog(ActionsActivity.this)
                .setPositiveButtonString("باشه")
                .setNegativeButton("بیخیال")
                .setTodayButton("امروز")
                .setTodayButtonVisible(true)
                .setActionTextColor(Color.GRAY)
                .setListener(new Listener() {
                    @Override
                    public void onDateSelected(PersianCalendar persianCalendar) {

                        actionDate = persianCalendar.getTime();
                        textView.setText(persianCalendar.getPersianLongDate().toString());
                    }

                    @Override
                    public void onDismissed() {

                    }
                });

        perDialog.show();

    }

    private void getContactInfo(String id, int from) {
        Cursor cursor = null;
        String result = "";
        String number = "";
        String mobileNumber = "";


        try {
            cursor = getContentResolver().query(ContactsContract
                            .Data.CONTENT_URI,
                    null,
                    ContactsContract.Data.CONTACT_ID + "=?",
                    new String[]{id},
                    null);

            while (cursor.moveToNext()) {

                result = cursor.getString(cursor.getColumnIndex
                        (ContactsContract.Data.DISPLAY_NAME));

                number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                int type = cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
                switch (type) {
                    case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
                        // do something with the Home number here...
                        Log.i("test123", "number home:" + number);
                        break;
                    case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
                        // do something with the Mobile number here...
                        Log.i("test123", "number mobile:" + number);
                        mobileNumber = number;
                        break;
                    case ContactsContract.CommonDataKinds.Phone.TYPE_WORK:
                        // do something with the Work number here...
                        Log.i("test123", "number work:" + number);
                        break;
                }
            }

        } catch (Exception e) {

            Log.i("test12", "Error reading contact", e);

        }

        //mContactName = result;
        Log.i("test123", "result:" + result.toString());
        Log.i("test123", "number:" + number);
        Log.i("test123", "number:" + mobileNumber);


        if (from == 1) {
            txtShowContact.setText(result);
            performerName = result;
            performerNumber = number;

        } else {
            updatePerformerName = result;
            updatePerformerNumber = number;
        }


    }

    public int getNextKey(Realm realm) {
        try {
            Number number = realm.where(ActionModel.class).max("actionId");
            if (number != null) {
                return number.intValue() + 1;
            } else {
                return 0;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            return 0;
        }
    }

    private void getContactInfoAndSaveToContacts() {
        final Dialog dialog = new Dialog(ActionsActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_get_members_info);

        CardView btnAdd = dialog.findViewById(R.id.btnAdd);
        final EditText edtName = dialog.findViewById(R.id.edtName);
        final EditText edtNumber = dialog.findViewById(R.id.edtNumber);


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = edtName.getText().toString();
                String number = edtNumber.getText().toString();

                if (name == "") {
                    Toast.makeText(ActionsActivity.this, "", Toast.LENGTH_SHORT).show();
                } else {

                    Intent addContactIntent = new Intent(Intent.ACTION_INSERT);
                    addContactIntent.setType(ContactsContract.Contacts.CONTENT_TYPE);
                    addContactIntent.putExtra(ContactsContract.Intents.Insert.NAME, name);
                    addContactIntent.putExtra(ContactsContract.Intents.Insert.PHONE, number);
                    startActivity(addContactIntent);
                    dialog.dismiss();

                }
            }
        });

        dialog.show();
    }


    private void getActionsFromRealm() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<ActionModel> results = realm.where(ActionModel.class).equalTo("parentId", parentId).findAll();

        if (results.size() > 0) {
            showRecycler();
            actions = results;
            injectRecyclerView();
        } else {
            showNullText();
        }
    }

    private void showRecycler() {
        txtNoAction = findViewById(R.id.txtNoAction);
        recyclerActions = findViewById(R.id.recyclerActions);

        txtNoAction.setVisibility(View.GONE);
        recyclerActions.setVisibility(View.VISIBLE);
    }

    private void showNullText() {
        txtNoAction = findViewById(R.id.txtNoAction);
        recyclerActions = findViewById(R.id.recyclerActions);

        txtNoAction.setVisibility(View.VISIBLE);
        recyclerActions.setVisibility(View.GONE);
    }


    public void updateActionItems(final Activity mActivity, final ActionModel info) {

        final Dialog dialog = new Dialog(mActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_add_action);

        ImageView btnSaveAction = dialog.findViewById(R.id.btnSaveAction);
        LinearLayout btnGetDate = dialog.findViewById(R.id.btnGetDate);
        LinearLayout btnGetContact = dialog.findViewById(R.id.btnGetContact);
        final TextView txtShowDate = dialog.findViewById(R.id.txtShowDate);
        txtShowContactUpdate = dialog.findViewById(R.id.txtShowContact);
        final EditText txtTitle = dialog.findViewById(R.id.txtTitle);

        PersianCalendar calendar = new PersianCalendar();
        calendar.setTime(info.getActionDate());
        txtTitle.setText(info.getActionTitle());
        txtShowDate.setText(calendar.getPersianLongDate());
        txtShowContactUpdate.setText(info.getActionPerformerName());

        // get date
        btnGetDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PersianDatePickerDialog perDialog = new PersianDatePickerDialog(mActivity)
                        .setPositiveButtonString("باشه")
                        .setNegativeButton("بیخیال")
                        .setTodayButton("امروز")
                        .setTodayButtonVisible(true)
                        .setActionTextColor(Color.GRAY)
                        .setListener(new Listener() {
                            @Override
                            public void onDateSelected(PersianCalendar persianCalendar) {

                                updateResultDate = persianCalendar.getTime();
                                txtShowDate.setText(persianCalendar.getPersianLongDate().toString());
                                Log.i("test123", "updateResultDate:" + updateResultDate.toString());
                            }

                            @Override
                            public void onDismissed() {

                            }
                        });
                perDialog.show();
            }
        });


        btnSaveAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ActionsActivity.updateItems(info.getActionId()
                        , info.getActionTitle()
                        , info.getActionPerformerName()
                        , info.getActionPerformerNumber()
                        , info.getActionDescription());

                dialog.dismiss();

            }
        });

        Window windowAction = dialog.getWindow();
        windowAction.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();


    }

    public static void updateItems(final int actionId
            , final String actionTitile
            , final String actionPerformName
            , final String actionPerformNumber
            , final String actionDescription) {

        Realm realm = Realm.getDefaultInstance();

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                ActionModel model = realm.where(ActionModel.class).equalTo("actionId", actionId).findFirst();
                if (model != null) {
                    model.setActionTitle(actionTitile);
                    model.setActionPerformerName(actionPerformName);
                    model.setActionPerformerNumber(actionPerformNumber);
                    model.setActionDescription(actionDescription);
                }

            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Log.i("test123", "description is updated to the realm db");
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Log.i("test123", "cant update description in the realm db" + error.toString());
            }
        });

    }


    public static void removeItemFromRealm(final int actionId) {

        Realm realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                ActionModel model = realm.where(ActionModel.class).equalTo("actionId", actionId).findFirst();
                model.deleteFromRealm();
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Log.i("test123", "item removed from realm");
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Log.i("test123", "can not remove item from realm" + error.toString());
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (resultCode == RESULT_OK) {

            if (requestCode == CONTACT_PICKER_RESULT) {

                Uri result = data.getData();
                String id = result.getLastPathSegment();
                Log.i("test123", "result:" + result.toString());
                getContactInfo(id, 1);
            }

            if (requestCode == CONTACT_PICKER_RESULT_UPDATE) {
                Uri result = data.getData();
                String id = result.getLastPathSegment();
                Log.i("test123", "result:" + result.toString());
                getContactInfo(id, 2);
            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }


}
