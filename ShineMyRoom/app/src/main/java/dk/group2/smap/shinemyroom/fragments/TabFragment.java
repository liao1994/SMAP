package dk.group2.smap.shinemyroom.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.TabItem;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import dk.group2.smap.shinemyroom.R;

public class TabFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate;
        inflate = inflater.inflate(R.layout.tabmenu_layout, container, false);
        return inflate;

    }
}
