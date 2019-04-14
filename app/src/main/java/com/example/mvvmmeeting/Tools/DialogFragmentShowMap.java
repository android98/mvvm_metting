package com.example.mvvmmeeting.Tools;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mvvmmeeting.Activities.AddMeetingActivity;
import com.example.mvvmmeeting.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;

public class DialogFragmentShowMap extends DialogFragment
        implements OnMapReadyCallback {
    private GoogleMap mMap;
    private static View view;
    CardView btnSet;

    public DialogFragmentShowMap() {
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (AddMeetingActivity.userLocation) {
            LatLng lng = new LatLng(AddMeetingActivity.userLat, AddMeetingActivity.userLng);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(lng));
            CameraUpdate update = CameraUpdateFactory.zoomTo(16);
            mMap.animateCamera(update);
        } else if (AddMeetingActivity.userLat != 0.0) {
            LatLng latLng = new LatLng(AddMeetingActivity.userLat, AddMeetingActivity.userLng);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            CameraUpdate cameraUpdate = CameraUpdateFactory.zoomTo(16);
            mMap.animateCamera(cameraUpdate);
        } else if (Utilities.checkLocationPermission(getActivity())) {
            SmartLocation.with(getActivity()).location()
                    .start(new OnLocationUpdatedListener() {
                        @Override
                        public void onLocationUpdated(Location location) {
                            LatLng latLng = new LatLng(location.getLatitude(),
                                    location.getLongitude());
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                            CameraUpdate zoom = CameraUpdateFactory.zoomTo(16);
                            mMap.animateCamera(zoom);
                            Log.i("test123", String.valueOf(latLng));

                        }
                    });
        }

        btnSet = view.findViewById(R.id.btnSet);
        btnSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double mylat = mMap.getCameraPosition().target.latitude;
                double mylng = mMap.getCameraPosition().target.longitude;
                Log.i("test123", String.valueOf(mylat));
                Log.i("test123", String.valueOf(mylng));
                AddMeetingActivity.meetingLat = mylat;
                AddMeetingActivity.meetingLng = mylng;
                AddMeetingActivity.userLocation = true;

                dismiss();

            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        if (view != null) {
            ViewGroup group = (ViewGroup) view.getParent();
            if (group != null) {
                group.removeView(view);
            }
        }

        try {
            view = inflater.inflate(R.layout.custom_dialog_show_map, container, false);
        } catch (InflateException e) {
            e.getMessage().toString();
        }


        SupportMapFragment fragment = (SupportMapFragment) getFragmentManager().findFragmentById(R.id.map);
        fragment.getMapAsync(this);
        btnSet = view.findViewById(R.id.btnSet);
        return view;
    }
}
