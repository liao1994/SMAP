package dk.group2.smap.shinemyroom.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.TabItem;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import dk.group2.smap.shinemyroom.R;

public class TabFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
                Toast.makeText(getActivity().getApplicationContext(),"EditBtn clicked",Toast.LENGTH_SHORT).show();
            }
        });

        return inflate;

    }
}
