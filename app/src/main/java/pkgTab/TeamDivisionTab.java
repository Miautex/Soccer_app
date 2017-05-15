package pkgTab;

import android.graphics.Color;
import android.graphics.Typeface;
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
import android.widget.SpinnerAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

import group2.schoolproject.a02soccer.R;
import pkgData.Participation;
import pkgData.Player;
import pkgData.PlayerPosition;
import pkgData.Team;
import pkgListeners.OnTeamChangedListener;

/**
 * Created by Raphael on 01.05.2017.
 * The best Product owner
 */

public class TeamDivisionTab extends Fragment implements View.OnClickListener, OnTeamChangedListener {
    private TableLayout tableAllPlayers = null;
    private TableLayout tableTeam1 = null;
    private View container = null;
    private TreeMap<Integer, Player> players = null;
    private TableRow.LayoutParams layoutSpinner = null;
    private TableRow.LayoutParams layoutTextView = null;
    private TableRow.LayoutParams layoutButton = null;
    private TableRow.LayoutParams layoutNotVisible = null;
    private OnTeamChangedListener listener;
    private Team team;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        this.container = inflater.inflate(R.layout.tab_team, container, false);
        players = (TreeMap<Integer, Player>) this.getArguments().getSerializable("Players");
        team = (Team) this.getArguments().getSerializable("Team");
        getAllViews();
        return this.container;
    }

    private void getAllViews() {
        tableAllPlayers = (TableLayout) container.findViewById(R.id.tableAllPlayers1);
        tableTeam1 = (TableLayout) container.findViewById(R.id.tableTeam1);
    }

    public void onClick(View bttn) {
        movePlayerRow(bttn);
    }

    private void movePlayerRow(View bttn) {
        TableRow row = (TableRow) bttn.getParent();
        if (row.getParent() == tableAllPlayers) {
            informOnTeamChangedListener(Integer.parseInt(((TextView) row.getChildAt(0)).getText().toString()), true);
            tableAllPlayers.removeView(row);
            ((Button) row.getChildAt(3)).setText("-");
            ((Button) row.getChildAt(3)).setTextColor(Color.RED);
            tableTeam1.addView(row);
        } else {
            informOnTeamChangedListener(Integer.parseInt(((TextView) row.getChildAt(0)).getText().toString()), false);
            tableTeam1.removeView(row);
            ((Button) row.getChildAt(3)).setText("+");
            ((Button) row.getChildAt(3)).setTextColor(Color.parseColor("#007c2b"));
            tableAllPlayers.addView(row);
        }
    }

    public void playerToRow(Player player) {
        TableRow row = new TableRow(this.getContext());
        row.addView(createTextViewId(String.valueOf(player.getId())));
        row.addView(createTextViewName(player.toString()));
        row.addView(createSpinner(player.getPositions()));
        row.addView(createButton());
        tableAllPlayers.addView(row);
    }


    public void setOnTeamChangedListener(OnTeamChangedListener listener) {
        this.listener = listener;
    }

    private void informOnTeamChangedListener(int id, boolean remove) {
        if (listener != null) {
            listener.onTeamUpdated(players.get(id), team, remove);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            setLayouts();
            for (Player p : players.values()) {
                playerToRow(p);
            }
        } catch (Exception e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onTeamUpdated(Player p, Team t, boolean remove) {
        if (remove) {
            players.get(p.getId());
            removeRow(p.getId());
        } else {
            players.put(p.getId(), p);
            playerToRow(p);
        }
    }

    public void removeRow(int id) {
        for (int i = 0; i < tableAllPlayers.getChildCount(); i++) {
            TableRow row = (TableRow) tableAllPlayers.getChildAt(i);
            if (Integer.parseInt(((TextView) row.getChildAt(0)).getText().toString()) == id) {
                tableAllPlayers.removeView(row);
            }
        }
    }

    public ArrayList<Participation> getPlayersInTeam() throws Exception {
        ArrayList<Participation> list = new ArrayList<>();
        if(!hasGoalkeeper()){
            throw new Exception(getString(R.string.msg_TeamHasNoGolaie) + team);
        }
        for (int i = 0; i < tableTeam1.getChildCount(); i++) {
            TableRow row = (TableRow) tableTeam1.getChildAt(i);
            TextView textv = (TextView) row.getChildAt(0);
            Spinner sp = (Spinner) row.getChildAt(2);
            int playerId = Integer.parseInt(textv.getText().toString());
            list.add(new Participation(players.get(playerId), team, StringToEnum((sp.getSelectedItem().toString()))));
        }
        return list;
    }

    private boolean hasGoalkeeper(){
        boolean hasGolaie = false;
        for (int i = 0; i < tableTeam1.getChildCount() && !hasGolaie; i++) {
            TableRow row = (TableRow) tableTeam1.getChildAt(i);
            Spinner sp = (Spinner) row.getChildAt(2);
            if (getString(R.string.PosGoal).equals(sp.getSelectedItem().toString())){
                hasGolaie = true;
            }
        }
        return hasGolaie;
    }

    private void setLayouts() {
        layoutTextView = new TableRow.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1.2f);
        layoutSpinner = new TableRow.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);
        layoutButton = new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.4f);
        layoutNotVisible = new TableRow.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 0);
    }

    private TextView createTextViewId(String id) {
        TextView txtId = new TextView(this.getContext());
        txtId.setText(id);
        txtId.setLayoutParams(layoutNotVisible);
        return txtId;
    }

    private TextView createTextViewName(String name) {
        TextView txtName = new TextView(this.getContext());
        txtName.setLayoutParams(layoutTextView);
        txtName.setGravity(Gravity.CENTER_VERTICAL);
        txtName.setTextSize(16);
        txtName.setTextColor(Color.BLACK);
        txtName.setText(name);
        return txtName;
    }

    private Spinner createSpinner(TreeSet<PlayerPosition> positions) {
        List<String> spinnerArray = new ArrayList<>();
        for (PlayerPosition pos : positions) {
            spinnerArray.add(EnumToString(pos));
        }
        SpinnerAdapter spinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, spinnerArray);
        Spinner spinnerPosition = new Spinner(this.getContext());
        spinnerPosition.setLayoutParams(layoutSpinner);
        spinnerPosition.setAdapter(spinnerAdapter);
        return spinnerPosition;
    }

    private Button createButton() {
        Button button = new Button(this.getContext());
        button.setLayoutParams(layoutButton);
        button.setText("+");
        button.setTextSize(20);
        button.setTextColor(Color.parseColor("#007c2b"));
        button.setTypeface(null, Typeface.BOLD);
        button.setOnClickListener(this);
        return button;
    }

    private String EnumToString(PlayerPosition position){
        String retVal = "";
        if(position == PlayerPosition.ATTACK){
            retVal = getString(R.string.PosAtk);
        }
        else if (position == PlayerPosition.DEFENSE){
            retVal = getString(R.string.PosDef);
        }
        else if (position == PlayerPosition.GOAL){
            retVal = getString(R.string.PosGoal);
        }
        else if (position == PlayerPosition.MIDFIELD){
            retVal = getString(R.string.PosMid);
        }
        return retVal;
    }

    private PlayerPosition StringToEnum (String position){
        PlayerPosition retVal = null;
        if (position.equals(getString(R.string.PosAtk))){
            retVal = PlayerPosition.ATTACK;
        }
        else if (position.equals(getString(R.string.PosDef))){
            retVal = PlayerPosition.DEFENSE;
        }
        else if (position.equals(getString(R.string.PosMid))){
            retVal = PlayerPosition.MIDFIELD;
        }
        else if (position.equals(getString(R.string.PosGoal))){
            retVal = PlayerPosition.GOAL;
        }
        return retVal;
    }
}

