package pkgTab;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import group2.schoolproject.a02soccer.ExceptionNotification;
import group2.schoolproject.a02soccer.R;
import pkgData.PlayerPosition;

/**
 * Created by Raphael on 01.05.2017.
 * The best Productowner
 */

public class TeamDivisionTab1 extends Fragment implements View.OnClickListener{
    private TableLayout tableAllPlayers = null;
    private Spinner sItems = null;
    private View view = null;
    private  ArrayAdapter<String> spinnerAdapter = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.tab_team1,container,false);

        List<String> spinnerArray = new ArrayList<>();
        for(PlayerPosition p : PlayerPosition.values()){
            spinnerArray.add(p.toString());
        }

        spinnerAdapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_dropdown_item,spinnerArray);

        getAllViews();
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //sItems.setAdapter(spinnerAdapter);
        return view;
    }

    private void setSpinnerOptions(Spinner s){
        s.setAdapter(spinnerAdapter);
    }


    private void getAllViews() {
        //sItems = (Spinner) view.findViewById(R.id.Team1Spinner);
        tableAllPlayers = (TableLayout) view.findViewById(R.id.tableAllPlayers1);
    }

    public void onClick(View arg0) {
        try {
            System.out.println("dddddddddddddddddd");
        } catch (Exception e) {
        }
    }

    public void addPlayer() {
        TableRow row = new TableRow(this.getContext());
        /*CheckBox chBox = new CheckBox(this.getContext());
        chBox.setLayoutParams(new TableRow.LayoutParams(10, ViewGroup.LayoutParams.MATCH_PARENT,0.2f));
        chBox.setOnClickListener(this);
        row.addView(chBox);*/
        // create a new TextView for showing xml data
        TextView txtName = new TextView(this.getContext());
        txtName.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,1));
        txtName.setGravity(Gravity.CENTER_VERTICAL);
        txtName.setTextSize(16);
        txtName.setTextColor(Color.BLACK);
        // set the text to "text xx"
        txtName.setText("Marco Wilscher");
        // add the TextView  to the new TableRow
        row.addView(txtName);
        Spinner spinnerPosition = new Spinner(this.getContext());
        spinnerPosition.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,1));
        setSpinnerOptions(spinnerPosition);
        row.addView(spinnerPosition);
        tableAllPlayers.addView(row);
    }

    public void addPlayerToTeam1() {
        tableAllPlayers = (TableLayout) view.findViewById(R.id.tableTeam1);
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
        tableAllPlayers.addView(row);
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
