package dk.group22.smap.shinemyroom.fragments;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.common.GoogleApiAvailability;

import dk.group22.smap.shinemyroom.Global;
import dk.group22.smap.shinemyroom.R;

//https://developer.android.com/training/permissions/requesting.html
public class GeoFencingFragment extends Fragment {

    public GeoFencingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view;
        if (ActivityCompat.checkSelfPermission(getContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(),
                        android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            view = inflater.inflate(R.layout.fragment_need_permission, container, false);
            Button btn = (Button) view.findViewById(R.id.permission_btn);
            btn.setOnClickListener(new View.OnClickListener(){
               @Override
               public void onClick(View v) {
                   Log.d("LOG", "PERMISSION CLICK");
                   ActivityCompat.requestPermissions(getActivity(),
                           new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,
                                   android.Manifest.permission.ACCESS_FINE_LOCATION},
                           Global.LOCATION_REQUEST);
               }
           });

        } else {
            view = inflater.inflate(R.layout.fragment_geo_fencing, container, false);
            Log.d("LOG", "PERMISSION GRANTED");
        }
        GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getActivity().getApplicationContext());

        // Here, thisActivity is the current activity

        return view;
    }
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case Global.LOCATION_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
