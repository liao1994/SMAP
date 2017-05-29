package dk.group2.smap.shinemyroom.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabItem;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import dk.group2.smap.shinemyroom.R;
import dk.group2.smap.shinemyroom.activities.LampActivity;
import dk.group2.smap.shinemyroom.activities.MainActivity;

public class TabFragment extends Fragment {
    private static final int HOMETAB_REQUEST_CODE = 1;


    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate;

        inflate = inflater.inflate(R.layout.tabmenu, container, false);

        ImageView homeBtn = (ImageView) inflate.findViewById(R.id.homeView);
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity().getApplicationContext(),"HomeBtn clicked",Toast.LENGTH_SHORT).show();
            }


        });

        ImageView editBtn = (ImageView) inflate.findViewById(R.id.editView);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return inflate;

    }
}
