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
 * The best Productowner
 */

public class TeamDivisionTab1 extends Fragment {
    private TableLayout table_PlayersData = null;
    private Spinner sItems = null;
    private View view = null;
    private  ArrayAdapter<String> adapter= null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.tab_team1,container,false);

        List<String> spinnerArray = new ArrayList<>();
        for(PlayerPosition p : PlayerPosition.values()){
            spinnerArray.add(p.toString());
        }

        adapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_dropdown_item,spinnerArray);

        getAllViews();
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //sItems.setAdapter(adapter);
        return view;
    }

    private void setSpinnerOptions(Spinner s){
        s.setAdapter(adapter);
    }


    private void getAllViews() {
        //sItems = (Spinner) view.findViewById(R.id.Team1Spinner);
        table_PlayersData = (TableLayout) view.findViewById(R.id.tableTeam1);
    }

    public void addPlayer() {
        TableRow row = new TableRow(this.getContext());
        // create a new TextView for showing xml data
        TextView t = new TextView(this.getContext());
        t.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,1));
        // set the text to "text xx"
        t.setText("Santner");
        // add the TextView  to the new TableRow
        row.addView(t);
        Spinner s = new Spinner(this.getContext());
        s.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,1));
        setSpinnerOptions(s);
        row.addView(s);
        table_PlayersData.addView(row);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            addPlayer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
