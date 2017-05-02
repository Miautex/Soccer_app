package pkgTab;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import group2.schoolproject.a02soccer.R;
import pkgData.PlayerPosition;

/**
 * Created by Raphael on 01.05.2017.
 */

public class Tab1Fragment extends Fragment {
    private static final String TAG = "Tab1Fragment";
    private View mview = null;
    private TableLayout table;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tab_team1,container,false);
        mview = view;

        List<String> spinnerArray =  new ArrayList<String>();
        for(PlayerPosition p : PlayerPosition.values()){
            spinnerArray.add(p.toString());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_dropdown_item,spinnerArray);


        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner sItems = (Spinner) view.findViewById(R.id.Team1Spinner);
        sItems.setAdapter(adapter);
        table = (TableLayout)mview.findViewById(R.id.tableTeam1);
        return view;
    }

    public void addPlayer(TableRow row){
        System.out.println("+++++++++++++++++++++++++++");
        System.out.println(table);
            // get a reference for the TableLayout
            table.addView(row, new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }



}
