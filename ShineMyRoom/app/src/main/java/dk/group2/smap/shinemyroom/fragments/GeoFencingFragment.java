package dk.group2.smap.shinemyroom.fragments;

import android.Manifest;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.GoogleApiAvailability;

import dk.group2.smap.shinemyroom.Global;
import dk.group2.smap.shinemyroom.R;



public class GeoFencingFragment extends Fragment {

    public GeoFencingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_geo_fencing, container, false);
//        GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getActivity().getApplicationContext());
//        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, Global.LOCATION_REQUEST);
        return view;
    }
}
