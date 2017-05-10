package group2.schoolproject.a02soccer;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.TreeSet;

import pkgComparator.PlayerComparatorName;
import pkgData.Database;
import pkgData.Game;
import pkgData.Participation;
import pkgData.Player;
import pkgMenu.DynamicMenuActivity;

public class AddGameSelectPlayersActivity extends DynamicMenuActivity implements View.OnClickListener {
    private static final int MIN_PLAYERS_REQUIRED = 4;

    private DatePicker datePicker = null;
    private TableLayout tablePlayers = null;
    private TableLayout tablePlayersHeader = null;
    private Button btnContinue = null,
            btnCancel = null;

    private Database db = null;
    private HashMap<Integer, Player> hmPlayers = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addgame);

        getAllViews();
        registrateEventHandlers();

        try {
            db = Database.getInstance();
            hmPlayers = new HashMap<>();

            for (Player p: db.getAllPlayers()) {
                hmPlayers.put(p.getId(), p);
            }

            TreeSet<Player> tsPlayers = new TreeSet<>(new PlayerComparatorName());
            tsPlayers.addAll(hmPlayers.values());
            displayPlayersInTable(tsPlayers);

        } catch (Exception ex) {
            ExceptionNotification.notify(this, ex);
            ex.printStackTrace();
        }
    }

    private void getAllViews() {
        datePicker = (DatePicker) findViewById(R.id.datePicker);
        tablePlayers = (TableLayout) findViewById(R.id.table_Players);
        tablePlayersHeader = (TableLayout) findViewById(R.id.table_PlayersHeader);
        btnContinue = (Button) findViewById(R.id.btnContinue);
        btnCancel = (Button) findViewById(R.id.btnCancel);
    }

    private void registrateEventHandlers() {
        btnContinue.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    private void displayPlayersInTable(Collection<Player> players) throws Exception {
        for (Player p : players) {
            TableRow row = new TableRow(this);
            CheckBox cb = new CheckBox(this);
            TextView txvName = new TextView(this);
            TextView txvId = new TextView(this);

            cb.setChecked(true);
            txvName.setText(p.toString());
            txvId.setText(Integer.toString(p.getId()));
            txvId.setVisibility(View.GONE);

            //Set layout like header
            cb.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
            txvName.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 2.5f));

            row.addView(cb);
            row.addView(txvName);
            row.addView(txvId);

            tablePlayers.addView(row, new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));

        }
    }
    private Game getGameWithParticipations(Collection<Player> players) {
        Game tmpGame = new Game(getDateFromDatePicker(datePicker), 0, 0);

        for (Player p : players) {
            Participation part = new Participation();
            part.setPlayer(p);
            tmpGame.addParticipation(part);
        }

        return tmpGame;
    }

    /**
     *
     * @param datePicker
     * @return a java.util.Date
     */
    public static java.util.Date getDateFromDatePicker(DatePicker datePicker){
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year =  datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        return calendar.getTime();
    }

    private ArrayList<Player> getSelectedPlayersFromTable() {
        ArrayList<Player> selectedPlayers = new ArrayList<Player>();
        TableRow row;
        CheckBox checkBox;
        TextView textViewId;

        for (int i = 0; i < tablePlayers.getChildCount(); i++) {
            row = (TableRow) tablePlayers.getChildAt(i);

            checkBox = (CheckBox) row.getChildAt(0);
            textViewId = (TextView) row.getChildAt(2);

            if (checkBox.isChecked()) {
                selectedPlayers.add(hmPlayers.get(Integer.parseInt(textViewId.getText().toString())));
            }
        }

        return selectedPlayers;
    }

    private void onBtnContinue() throws Exception {
        ArrayList<Player> selectedPlayers = getSelectedPlayersFromTable();

        if (selectedPlayers.size() < MIN_PLAYERS_REQUIRED) {
            throw new Exception(String.format(getString(R.string.msg_SelectMinNumOfPlayers), MIN_PLAYERS_REQUIRED));
        }

        //Game result = getGameWithParticipations(selectedPlayers);
        Game result = new Game(getDateFromDatePicker(datePicker), 0, 0);

        //Intent myIntent = new Intent(this, TeamManagmentActivity.class);
        //Intent myIntent = new Intent(this, AddGameEnterDataActivity.class);
        Intent myIntent = new Intent(this, TeamManagmentActivity.class);
        myIntent.putExtra("game", result);
        myIntent.putExtra("players",selectedPlayers);

        this.startActivity(myIntent);
    }

    @Override
    public void onClick(View v) {
        try {
            if (v.getId() == R.id.btnContinue) {
                onBtnContinue();
            } else if (v.getId() == R.id.btnCancel) {
                this.finish();
            }
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}