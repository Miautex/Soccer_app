package pkgTab;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import group2.schoolproject.a02soccer.R;
import pkgData.Player;
import pkgData.PlayerPosition;
import pkgData.Team;
import pkgListeners.OnScoreChangedListener;
import pkgListeners.OnTeamChangedListener;
import pkgResult.PositionResult;

/**
 * Created by Raphael on 01.05.2017.
 * The best Product owner
 */

public class TeamDivisionTab1 extends Fragment implements View.OnClickListener, OnTeamChangedListener{
    private TableLayout tableAllPlayers = null;
    private TableLayout tableTeam1 = null;
    private Spinner sItems = null;
    private View view = null;
    private  ArrayAdapter<String> spinnerAdapter = null;
    private ArrayList<Player> players = null;
    private int[] ids;
    private int cursorIds = 0;
    private TableRow.LayoutParams rowLayout = null;
    private TableRow.LayoutParams rowLayoutNotVisible = null;
    private OnTeamChangedListener listener;
    private Team team;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.tab_team1,container,false);
        //Toast.makeText(view.getContext(),getArguments().getString("message"), Toast.LENGTH_SHORT).show();
        //Game g =(Game) this.getArguments().getSerializable("test");
        //player= this.getArguments ...
        team = (Team) this.getArguments().getSerializable("Team");
        getAllViews();
        //sItems.setAdapter(spinnerAdapter);
        return view;
    }

    private void setSpinnerOptions(Spinner s){
        s.setAdapter(spinnerAdapter);
    }


    private void getAllViews() {
        //sItems = (Spinner) view.findViewById(R.id.Team1Spinner);
        tableAllPlayers = (TableLayout) view.findViewById(R.id.tableAllPlayers1);
        tableTeam1 = (TableLayout) view.findViewById(R.id.tableTeam1);
    }

    public void onClick(View arg0) {
        if(arg0.getId() == R.id.Player1){
            TableRow row = (TableRow) view.findViewById(R.id.Player1).getParent();
            informOnTeamChangedListener(Integer.parseInt(((TextView)row.getChildAt(0)).getText().toString()));
            //Toast.makeText(view.getContext(),((TextView)row.getChildAt(0)).getText(), Toast.LENGTH_SHORT).show();
            tableAllPlayers.removeView(row);
            tableTeam1.addView(row);

        }
    }

    public void loadIds(){
        ids = new int[22];
        ids[0] = R.id.Player1;
        ids[1] = R.id.Player2;
        ids[2] = R.id.Player3;
        ids[3] = R.id.Player4;
        ids[4] = R.id.Player5;
        ids[5] = R.id.Player6;
        ids[6] = R.id.Player7;
        ids[7] = R.id.Player8;
        ids[8] = R.id.Player9;
        ids[9] = R.id.Player10;
        ids[10] = R.id.Player11;
        ids[11] = R.id.Player12;
        ids[12] = R.id.Player13;
        ids[13] = R.id.Player14;
        ids[14] = R.id.Player15;
        ids[15] = R.id.Player16;
        ids[16] = R.id.Player17;
        ids[17] = R.id.Player18;
        ids[18] = R.id.Player19;
        ids[19] = R.id.Player20;
        ids[20] = R.id.Player21;
        ids[21] = R.id.Player22;
    }

    public void addPlayer(Player p) {
        List<String> spinnerArray = new ArrayList<>();
        for(PlayerPosition pos : p.getPositions()){
            spinnerArray.add(pos.toString());
        }
        spinnerAdapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_dropdown_item,spinnerArray);

        TableRow row = new TableRow(this.getContext());
        TextView txtId = new TextView(this.getContext());
        txtId.setText("" +p.getId());
        txtId.setLayoutParams(rowLayoutNotVisible);
        row.addView(txtId);
        TextView txtName = new TextView(this.getContext());
        txtName.setLayoutParams(rowLayout);
        txtName.setGravity(Gravity.CENTER_VERTICAL);
        txtName.setTextSize(16);
        txtName.setTextColor(Color.BLACK);
        txtName.setText(p.getName());
        row.addView(txtName);
        Spinner spinnerPosition = new Spinner(this.getContext());
        spinnerPosition.setLayoutParams(rowLayout);
        setSpinnerOptions(spinnerPosition);
        row.addView(spinnerPosition);
        Button button = new Button(this.getContext());
        button.setText("ADD");
        button.setId(ids[cursorIds]);
        button.setOnClickListener(this);
        row.addView(button);
        tableAllPlayers.addView(row);
    }

    public void setOnTeamChangedListener(OnTeamChangedListener listener) {
        this.listener = listener;
    }

    private void informOnTeamChangedListener(int id) {
        if (listener != null) {
            listener.onTeamUpdated(new Player(id),team);
        }
    }

    private void setLayouts(){
        rowLayout = new TableRow.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,1);
        rowLayoutNotVisible = new TableRow.LayoutParams(0,ViewGroup.LayoutParams.MATCH_PARENT,0);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            setLayouts();
            loadIds();
            Player a = new Player(1,"a","q",true);
            a.addPosition(PlayerPosition.ATTACK);
            a.addPosition(PlayerPosition.DEFENSE);
            addPlayer(a);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTeamUpdated(Player p, Team t) {
        Toast.makeText(view.getContext(),"des ballert", Toast.LENGTH_SHORT).show();
    }
}
