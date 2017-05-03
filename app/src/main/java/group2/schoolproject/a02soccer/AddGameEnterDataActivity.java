package group2.schoolproject.a02soccer;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.TreeSet;

import pkgComparator.ParticipationComparatorName;
import pkgData.Database;
import pkgData.Game;
import pkgData.Participation;
import pkgData.Team;
import pkgListeners.OnScoreChangedListener;
import pkgTab.SectionsPageAdapter;
import pkgTab.TabAddGameEnterData;

public class AddGameEnterDataActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener,
        OnScoreChangedListener, View.OnClickListener {

    private SectionsPageAdapter adapter = null;
    private ViewPager mViewPager = null;
    private TabLayout tablayout = null;

    private TextView txtScore = null;
    private EditText edtRemark = null;
    private Button btnBack = null,
                   btnSave = null;

    private Game tmpGame = null;
    private Database db = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_game_enter_data);

        try {
            getAllViews();
            registrateEventHandlers();

            db = Database.getInstance();
            tmpGame = (Game) this.getIntent().getSerializableExtra("game");

            if (tmpGame == null) {
                throw new Exception("Please call activity with intent-extra 'game'");
            }

            adapter = new SectionsPageAdapter(getSupportFragmentManager());
            setupViewPager(mViewPager);
            tablayout.setupWithViewPager(mViewPager);
            tablayout.setOnTabSelectedListener(this);

            //Handler to display first tab after 0.1sec
            new Handler().postDelayed(
                new Runnable(){
                    @Override
                    public void run() {
                        tablayout.getTabAt(0).select();
                    }
                }, 100);
        }
        catch (Exception ex) {
            ExceptionNotification.notify(this, ex);
            ex.printStackTrace();
        }
    }

    private void getAllViews() {
        mViewPager = (ViewPager) findViewById(R.id.pager);
        tablayout = (TabLayout) findViewById(R.id.tabs);
        txtScore = (TextView) findViewById(R.id.txtScore);
        edtRemark = (EditText) findViewById(R.id.edtRemark);
        btnBack = (Button) findViewById(R.id.btnBack);
        btnSave = (Button) findViewById(R.id.btnSave);
    }

    private void registrateEventHandlers() {
        btnBack.setOnClickListener(this);
        btnSave.setOnClickListener(this);
    }

    private void setupViewPager(ViewPager viewPager){
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new TabAddGameEnterData(),"TEAM1");
        adapter.addFragment(new TabAddGameEnterData(),"TEAM2");
        viewPager.setAdapter(adapter);
        this.adapter = adapter;
    }

    private ArrayList<Participation> getParticipationsOfTeam(Team team) {
        ArrayList<Participation> participationsOfTeam = new ArrayList<>();

        for (Participation p: tmpGame.getParticipations()) {
            if (p.getTeam().equals(team)) {
                participationsOfTeam.add(p);
            }
        }

        return participationsOfTeam;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        TabAddGameEnterData fragment = (TabAddGameEnterData) adapter.getItem(tab.getPosition());

        /*if (tab.getPosition() == 0) {
            fragment.setParticipations(getParticipationsOfTeam(Team.TEAM1));
        }
        else if (tab.getPosition() == 1) {
            fragment.setParticipations(getParticipationsOfTeam(Team.TEAM2));
        }*/

        TreeSet<Participation> tsp = new TreeSet<>(new ParticipationComparatorName());
        tsp.addAll(tmpGame.getParticipations());
        fragment.setParticipations(tsp);
        fragment.setOnScoreChangedListener(this);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        TabAddGameEnterData fragment = (TabAddGameEnterData) adapter.getItem(tab.getPosition());
        ArrayList<Participation> participationsFromTable = fragment.getParticipationsFromTable();

        for (Participation p: participationsFromTable) {
            tmpGame.removeParticipation(p);
            tmpGame.addParticipation(p);
        }
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        onTabSelected(tab);
    }

    @Override
    public void onScoreUpdated(int sumGoalsShot) {
        if (tablayout.getTabAt(0).isSelected()) {
            tmpGame.setScoreTeamA(sumGoalsShot);
        }
        else {
            tmpGame.setScoreTeamB(sumGoalsShot);
        }
        updateScoreDisplay(tmpGame.getScoreTeamA(), tmpGame.getScoreTeamB());
    }

    private void updateScoreDisplay(int scoreTeamA, int scoreTeamB) {
        txtScore.setText("A:B - " + scoreTeamA + ":" + scoreTeamB);
    }

    private void onBtnSaveClick() {
        try {
            tmpGame.setRemark(edtRemark.getText().toString());

            db.insert(tmpGame);

            for (Participation p: tmpGame.getParticipations()) {
                db.insert(p);
            }
        }
        catch (Exception ex) {
            ExceptionNotification.notify(this, ex);
        }
    }

    @Override
    public void onClick(View v) {
        try {
            if (v.getId() == R.id.btnSave) {
                onBtnSaveClick();
            }
            else if (v.getId() == R.id.btnBack) {
                this.finish();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
