package dk.group2.smap.shinemyroom;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.todddavies.components.progressbar.ProgressWheel;

/**
 * Created by liao on 26-05-2017.
 */

public class LoadingFragment extends Fragment {
    ProgressWheel pw;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.authentication_progress_bar, container, false);
        pw = (ProgressWheel) view.findViewById(R.id.pw_spinner);
        pw.startSpinning();
        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        pw.stopSpinning();
    }
}