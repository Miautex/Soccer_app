package group2.schoolproject.a02soccer;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.zxing.Result;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.TreeSet;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import pkgComparator.PlayerComparatorName;
import pkgData.Game;
import pkgData.Player;
import pkgDatabase.Database;
import pkgMisc.PxDpConverter;

/**
 * @author Elias Santner
 */

public class AddGameSelectPlayersActivity extends BaseActivity implements View.OnClickListener, ZXingScannerView.ResultHandler {
    private static final int MIN_PLAYERS_REQUIRED = 4;

    private DatePicker datePicker = null;
    private TableLayout tablePlayers = null;
    private TableLayout tablePlayersHeader = null;
    private Button btnContinue = null,
            btnCancel = null,
            btnQRScan = null;
    private CheckBox ckbParticipationHeader = null;
    private ZXingScannerView scanner;

    private Database db = null;
    private HashMap<Integer, Player> hmPlayers = null;
    private int numSelectedCheckboxes = 0;
    private static final int RC_BARCODE_CAPTURE = 9001;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_game_select_players);
        setTitle(R.string.title_activity_add_game_select_players );
        getAllViews();
        registrateEventHandlers();

        try {
            db = Database.getInstance();
            hmPlayers = new HashMap<>();        //for easier access to players by id

            //Exit activity when user is not an admin (shouldn't happen)
            if (!db.getCurrentlyLoggedInPlayer().isAdmin()) {
                this.finish();
            }

            for (Player p: db.getCachedPlayers()) {
                hmPlayers.put(p.getId(), p);
            }

            TreeSet<Player> tsPlayers = new TreeSet<>(new PlayerComparatorName());      //for sorting
            tsPlayers.addAll(db.getCachedPlayers());
            displayPlayersInTable(tsPlayers);

        } catch (Exception ex) {
            showMessage(getString(R.string.Error) + ": " + ex.getMessage());
        }
    }

    private void getAllViews() {
        datePicker = (DatePicker) findViewById(R.id.datePicker);
        tablePlayers = (TableLayout) findViewById(R.id.table_Players);
        tablePlayersHeader = (TableLayout) findViewById(R.id.table_PlayersHeader);
        btnContinue = (Button) findViewById(R.id.btnSave);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnQRScan = (Button) findViewById(R.id.btnQRScan);
        ckbParticipationHeader = (CheckBox) findViewById(R.id.ckbParticipationHeader);
    }

    private void registrateEventHandlers() {
        btnContinue.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnQRScan.setOnClickListener(this);
        ckbParticipationHeader.setOnClickListener(this);
    }

    private void displayPlayersInTable(Collection<Player> players) throws Exception {
        for (Player p : players) {
            TableRow row = new TableRow(this);
            CheckBox cb = new CheckBox(this);
            TextView txvName = new TextView(this);
            TextView txvId = new TextView(this);
            LinearLayout llo = new LinearLayout(this);


            cb.setChecked(true);
            cb.setOnClickListener(this);
            txvName.setText(p.toString());
            txvName.setGravity(Gravity.TOP);
            txvId.setText(Integer.toString(p.getId()));
            txvId.setVisibility(View.GONE);


            numSelectedCheckboxes++;

            //Set layout like header
            cb.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
            txvName.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            llo.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 5));

            //set paddingBottom for row
            row.setPadding(0, PxDpConverter.toDp(5, this), 0, 0);
            row.setBackgroundResource(R.drawable.tablelayout3);
            llo.addView(txvName);       //linear layout as wrapper for txvName to fix text being cut off
            row.addView(cb);
            row.addView(llo);
            row.addView(txvId);

            tablePlayers.addView(row, new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));

        }
    }

    private void setCheckPlayers(ArrayList<Player> players) {
        TableRow row;
        CheckBox checkBox;
        TextView textViewId;
        int id, numAllPlayers;
        int count = 0;
        numAllPlayers = tablePlayers.getChildCount();
        ckbParticipationHeader.setChecked(false);

        for (int i = 0; i < numAllPlayers; i++) {
            row = (TableRow) tablePlayers.getChildAt(i);
            checkBox = (CheckBox) row.getChildAt(0);
            checkBox.setChecked(false);
            textViewId = (TextView) row.getChildAt(2);
            id = Integer.parseInt(textViewId.getText().toString());
            for (Player p : players) {
                if (id == p.getId()) {
                    count++;
                    checkBox.setChecked(true);
                    break;
                }
            }
        }
        if(count == numAllPlayers){
            ckbParticipationHeader.setChecked(true);
        }
    }

    /**
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
        ArrayList<Player> selectedPlayers = new ArrayList<>();
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

        //Intent myIntent = new Intent(this, TeamDivisionActivity.class);
        //Intent myIntent = new Intent(this, AddGameEnterDataActivity.class);
        Intent myIntent;
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String layout = sp.getString("preference_assignment_layout","Wilscher suckt");
        if(layout == "TAB_LAYOUT") {
            myIntent = new Intent(this, TeamDivisionActivity.class);
        }
        else{
            myIntent = new Intent(this, TeamDivision2.class);
        }
        myIntent.putExtra("game", result);
        myIntent.putExtra("players",selectedPlayers);
        this.startActivity(myIntent);
    }

    private void openQRScanner() {
        //scanner = new ZXingScannerView(this);
        //setContentView(scanner);
        //scanner.setResultHandler(this);
        //scanner.startCamera();
        // launch barcode activity.
            Intent intent = new Intent(this, QRScannerActivity.class);
            intent.putExtra(QRScannerActivity.AutoFocus, true);
            intent.putExtra(QRScannerActivity.UseFlash, false);
            startActivityForResult(intent, RC_BARCODE_CAPTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_BARCODE_CAPTURE) {
            if(resultCode == Activity.RESULT_OK){
                ArrayList<Player> result = (ArrayList<Player>) data.getSerializableExtra("Result");
                setCheckPlayers(result);

            }
            else if (resultCode != Activity.RESULT_CANCELED) {
                showMessage(getString(R.string.qr_failed));
            }
        }

    }

    @Override
    public void handleResult(Result rawResult){
       //Log.i("SCANN RESULT", rawResult.getText());
       // scanner.resumeCameraPreview(this);
    }

    @Override
    public void onClick(View v) {
        try {
            if (v.getId() == R.id.btnSave) {
                onBtnContinue();
            }
            else if (v.getId() == R.id.btnCancel) {
                this.finish();
            }
            else if(v.getId() == R.id.btnQRScan){
                openQRScanner();
            }
            else if (v.getId() == R.id.ckbParticipationHeader) {
                TableRow row;
                CheckBox checkBox;

                for (int i = 0; i < tablePlayers.getChildCount(); i++) {
                    row = (TableRow) tablePlayers.getChildAt(i);

                    checkBox = (CheckBox) row.getChildAt(0);
                    checkBox.setChecked(ckbParticipationHeader.isChecked());
                }

                if (ckbParticipationHeader.isChecked()) {
                    numSelectedCheckboxes = tablePlayers.getChildCount();
                }
                else  {
                    numSelectedCheckboxes = 0;
                }
            }
            else if (v.getClass().equals(CheckBox.class)) {
                CheckBox ckb = (CheckBox) v;
                if (ckb.isChecked()) {
                    numSelectedCheckboxes++;
                }
                else {
                    numSelectedCheckboxes--;
                }

                //if all ckbs are selected select top one
                if (numSelectedCheckboxes == tablePlayers.getChildCount()) {
                    ckbParticipationHeader.setChecked(true);
                }
                else {
                    ckbParticipationHeader.setChecked(false);
                }
            }
        } catch (Exception e) {
            showMessage(e.getMessage());
            e.printStackTrace();
        }
    }
}