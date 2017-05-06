package group2.schoolproject.a02soccer;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
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

public class AddGameEnterDataActivity extends AppCompatActivity implements OnScoreChangedListener, View.OnClickListener {

    private SectionsPageAdapter adapter = null;
    private ViewPager mViewPager = null;
    private TabLayout tablayout = null;

    private TextView txtScore = null;
    private EditText edtRemark = null;
    private Button btnBack = null,
                   btnSave = null;

    private Game tmpGame = null;
    private Database db = null;

    TabAddGameEnterData[] tabs = null;

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

            tabs = new TabAddGameEnterData[2];

            tabs[0] = (TabAddGameEnterData) adapter.getItem(0);
            tabs[1] = (TabAddGameEnterData) adapter.getItem(1);
            tabs[0].setOnScoreChangedListener(this);
            tabs[1].setOnScoreChangedListener(this);

            //Initially display score
            updateScoreDisplay(tmpGame.getScoreTeamA(), tmpGame.getScoreTeamB());

            //Handler to display first tab after 0.1sec
            new Handler().postDelayed(
                new Runnable(){
                    @Override
                    public void run() {
                        //Very temporary, until Team selection GUI is done
                        TreeSet<Participation> tsp1 = new TreeSet<>(new ParticipationComparatorName());
                        TreeSet<Participation> tsp2 = new TreeSet<>(new ParticipationComparatorName());
                        ArrayList<Participation> participations = tmpGame.getParticipations();
                        for (int i=0; i<participations.size(); i++) {
                            if (i<(participations.size()/2)) {
                                Participation p = participations.get(i);
                                p.setTeam(Team.TEAM1);
                                tsp1.add(p);
                            }
                            else {
                                Participation p = participations.get(i);
                                p.setTeam(Team.TEAM2);
                                tsp2.add(p);
                            }
                        }
                        tabs[0].setParticipations(tsp1);
                        tabs[1].setParticipations(tsp2);
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
    public void onScoreUpdated(int sumGoalsShot, Fragment fragment) {
        if (tabs[0].equals(fragment)) {
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

            for (int i = 0; i < tabs.length; i++) {
                for (Participation p : tabs[i].getParticipationsFromTable()) {
                    tabs[i].forceScoreRecalculation();
                    tmpGame.removeParticipation(p);
                    tmpGame.addParticipation(p);
                }
            }

            tmpGame.setRemark(edtRemark.getText().toString());

            Game remoteGame = db.insert(tmpGame);
            tmpGame.setId(remoteGame.getId());      //set webservice-generated id for game

            for (Participation p: tmpGame.getParticipations()) {
                db.insert(p);
            }

            Toast.makeText(this, "Saved Game", Toast.LENGTH_SHORT).show();
        }
        catch (Exception ex) {
            ExceptionNotification.notify(this, ex);
            ex.printStackTrace();
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
