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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;

import pkgData.Database;
import pkgData.Game;
import pkgData.Participation;
import pkgData.Player;
import pkgMenu.DynamicMenuActivity;

public class AddGameSelectPlayersActivity extends DynamicMenuActivity implements View.OnClickListener {
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

        try {
            db = Database.getInstance();
            hmPlayers = new HashMap<>();

            for (Player p: db.getAllPlayers()) {
                hmPlayers.put(p.getId(), p);
            }

            getAllViews();
            registrateEventHandlers();
            displayPlayersInTable(hmPlayers.values());

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
            txvName.setText(p.getName());
            txvId.setText(Integer.toString(p.getId()));
            txvId.setVisibility(View.GONE);

            //Layout doesn't work...
            TableRow.LayoutParams params = new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
            cb.setLayoutParams(params);
            txvId.setLayoutParams(params);

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
        TableRow row = null;
        CheckBox checkBox = null;
        TextView textViewId = null;

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
        Game result = getGameWithParticipations(getSelectedPlayersFromTable());

        //Intent myIntent = new Intent(this, TeamManagmentActivity.class);
        Intent myIntent = new Intent(this, AddGameEnterDataActivity.class);
        myIntent.putExtra("game", result);

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
            ExceptionNotification.notify(getApplicationContext(), e);
            e.printStackTrace();
        }
    }
}
/*
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import java.util.Calendar;
import java.util.HashMap;

import pkgData.Database;
import pkgData.Game;
import pkgData.Participation;
import pkgData.Player;
import pkgListener.AddGame_FragmentEventListener;

public class AddGameSelectPlayersActivity extends DynamicMenuActivity implements AddGame_FragmentEventListener {
    private Database db = null;
    private HashMap<Integer, Player> hmPlayers = null;
    private Game tmpGame = null;

    private PagerAdapter adapter = null;
    private ViewPager viewPager = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addgame);

        try {
            db = Database.getInstance();
            hmPlayers = new HashMap<>();
            tmpGame = new Game(Calendar.getInstance().getTime(), 0, 0);

            for (Player p : db.getAllPlayers()) {
                Participation part = new Participation();
                part.setPlayer(p);
                tmpGame.addParticipation(part);
            }

            initTabView();
        }
        catch (Exception ex) {
            ExceptionNotification.notify(this, ex);
            ex.printStackTrace();
        }
    }

    private void initTabView() {
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Tab 1"));
        tabLayout.addTab(tabLayout.newTab().setText("Tab 2"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = (ViewPager) findViewById(R.id.pager);
        adapter = new PagerAdapter (getSupportFragmentManager(), tabLayout.getTabCount());

        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    adapter.getItem(0);
                    //TODO:
                    //Interface for saving method in all three Tabs
                    //Once tab is unselected, saving occours and AddGameSelectPlayersActivity (this)
                    //has updated data of game+participations
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onBtnContinueClicked(Fragment sender) {
    }

    @Override
    public void onBtnCancelClicked(Fragment sender) {
        if (sender.getClass().equals(TabFragment_AddGame1.class)) {
            this.finish();
        }
    }

    @Override
    public void onBtnBackClicked(Fragment sender) {

    }

    @Override
    public void onBtnSaveClicked(Fragment sender) {

    }
}
*/