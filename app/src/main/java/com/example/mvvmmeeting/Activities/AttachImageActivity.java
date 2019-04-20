package com.example.mvvmmeeting.Activities;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mvvmmeeting.Adapters.Recycler_Adapter_Attach_Image;
import com.example.mvvmmeeting.Models.ImageModel;
import com.example.mvvmmeeting.R;

import java.io.File;

import io.realm.Realm;
import io.realm.RealmResults;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

public class AttachImageActivity extends AppCompatActivity {


    int PICK_IMAGE_CAMERA = 178;
    int PICK_IMAGE_GALLERY = 179;
    ImageView btnBack;
    ImageView btnAdd;
    public static RecyclerView recyclerImages;
    Recycler_Adapter_Attach_Image adapterImage;
    GridLayoutManager managerImage;
    int parentId = -1;
    RealmResults<ImageModel> images;
    public static TextView txtNoImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attach_image);


        Intent intent = getIntent();
        parentId = intent.getIntExtra("parentId", -1);

        injectToptoolbar();
        getImagesFromRealm();
    }

    private void getImagesFromRealm() {


    }

    private void injectToptoolbar() {

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        // btn add image
        btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addImage();
            }
        });
    }

    private void addImage() {
        final BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(R.layout.custom_dialog_select_image);

        LinearLayout linearCamera = dialog.findViewById(R.id.linearCamera);
        LinearLayout linearGallery = dialog.findViewById(R.id.linearGallery);

        linearCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EasyImage.openCamera(AttachImageActivity.this, PICK_IMAGE_CAMERA);
                dialog.dismiss();
            }
        });


        linearGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EasyImage.openGallery(AttachImageActivity.this, PICK_IMAGE_GALLERY);
                dialog.dismiss();
            }
        });

        dialog.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                super.onImagePickerError(e, source, type);
                Log.i("test123", "in error image picker");
            }

            @Override
            public void onImagePicked(File imageFile, EasyImage.ImageSource source, int type) {
                Log.i("test123", "file:" + imageFile.toString());
                Log.i("test123", "source:" + source.toString());

                String path = imageFile.getAbsolutePath();
                saveImageInRealm(path);


            }

            @Override
            public void onCanceled(EasyImage.ImageSource source, int type) {
                super.onCanceled(source, type);

                if (source == EasyImage.ImageSource.CAMERA) {
                    Log.i("test123", "in image picker canceled");
                    File photoFile = EasyImage.lastlyTakenButCanceledPhoto(AttachImageActivity.this);
                    if (photoFile != null)
                        photoFile.delete();
                }

            }
        });

    }


    private void saveImageInRealm(final String path) {
        Realm realm = Realm.getDefaultInstance();
        final int imageId = getNextKey(realm);

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                ImageModel model = realm.createObject(ImageModel.class, imageId);
                model.setParenId(parentId);
                model.setImagePath(path);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Log.i("test123", "in success: image add to DB");
                getImagesFromRealm();

            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Log.i("test123", "in error:" + error.toString());
            }
        });

        realm.close();
    }

    public int getNextKey(Realm realm) {
        try {
            Number number = realm.where(ImageModel.class).max("imageId");
            if (number != null) {
                return number.intValue() + 1;
            } else {
                return 0;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            return 0;
        }
    }


    private void injectRecyclerView() {

        recyclerImages = findViewById(R.id.recyclerImages);
        managerImage = new GridLayoutManager(this, 2);
        adapterImage = new Recycler_Adapter_Attach_Image(images, this, parentId);

        recyclerImages.setHasFixedSize(true);
        recyclerImages.setLayoutManager(managerImage);
        recyclerImages.setAdapter(adapterImage);

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(AttachImageActivity.this, MeetingDetailsActivity.class);
        intent.putExtra("orderid", parentId);
        startActivity(intent);

    }

    public static void removeImageFroimRealm(final int imageId, final File file, final int parentId) {

        final Realm realm = Realm.getDefaultInstance();

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                ImageModel image = realm.where(ImageModel.class).equalTo("imageId", imageId).findFirst();
                image.deleteFromRealm();
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                boolean deleted = file.delete();

                if (deleted) {
                    Log.i("test123", "file deleted from storage");
                } else {
                    Log.i("test123", "cant delete file");
                }

                RealmResults<ImageModel> results = realm.where(ImageModel.class)
                        .equalTo("parentId", parentId)
                        .findAll();
                if (results != null) {

                    if (results.size() >= 1) {
                        Log.i("test123", "recycler is visible and size is:" + String.valueOf(results.size()));
                        txtNoImage.setVisibility(View.INVISIBLE);
                        recyclerImages.setVisibility(View.VISIBLE);

                    } else {
                        recyclerImages.setVisibility(View.INVISIBLE);
                        txtNoImage.setVisibility(View.VISIBLE);
                        Log.i("test123", "txtNoImage is visible");
                    }
                } else {
                    Log.i("test123", "the result for image from realm is null");
                }

            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Log.i("test123", "cant delete image from realm:" + error.toString());
            }
        });


        realm.close();


    }


}
