package dk.group22.smap.shinemyroom.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.todddavies.components.progressbar.ProgressWheel;

import dk.group2.smap.shinemyroom.R;
//https://github.com/Todd-Davies/ProgressWheel
public class LoadingFragment extends Fragment {
    ProgressWheel pw;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.authentication_progress_bar, container, false);
        pw = (ProgressWheel) view.findViewById(R.id.pw_spinner);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        pw.startSpinning();
    }

    @Override
    public void onStop() {
        super.onStop();
        pw.stopSpinning();
    }
}
