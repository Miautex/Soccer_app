package pkgTab;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import group2.schoolproject.a02soccer.R;

/**
 * Created by Raphael
 */

public class Tab2Fragment extends Fragment {

    private View view = null;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tab_team2,container,false);

        return view;
    }


}
