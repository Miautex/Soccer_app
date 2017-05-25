package group2.schoolproject.a02soccer;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import pkgAdapter.SectionsPageAdapter;
import pkgData.Game;
import pkgData.Participation;
import pkgData.Team;
import pkgDatabase.Database;
import pkgDatabase.LoadParticipationsHandler;
import pkgDatabase.UpdateGameHandler;
import pkgDatabase.pkgListener.OnGameUpdatedListener;
import pkgDatabase.pkgListener.OnLoadParticipationsListener;
import pkgListeners.OnScoreChangedListener;
import pkgMisc.NamePWValidator;
import pkgTab.TabAddGameEnterData;

public class EditGameActivity extends BaseActivity implements OnScoreChangedListener, View.OnClickListener,
        OnLoadParticipationsListener, OnGameUpdatedListener {

    private SectionsPageAdapter adapter = null;
    private ViewPager mViewPager = null;
    private TabLayout tablayout = null;

    private TextView txtScore = null;
    private EditText edtRemark = null;
    private Button btnBack = null,
            btnSave = null;
    private ProgressBar pb = null;

    private Game gameToUpdate = null;
    private Database db = null;

    private TabAddGameEnterData[] tabs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_game_enter_data);
        setTitle(R.string.title_activity_edit_game);

        try {
            getAllViews();
            registrateEventHandlers();

            db = Database.getInstance();
            gameToUpdate = (Game) this.getIntent().getSerializableExtra("game");

            if (gameToUpdate == null) {
                throw new Exception("Please call activity with intent-extra 'game'");
            }

            //Exit activity when user is not an admin (shouldn't happen)
            if (!db.getCurrentlyLoggedInPlayer().isAdmin()) {
                this.finish();
            }

            adapter = new SectionsPageAdapter(getSupportFragmentManager());
            setupViewPager(mViewPager);
            tablayout.setupWithViewPager(mViewPager);

            //Load participations
            db.getParticipationsOfGame(gameToUpdate, this);
            toggleProgressBar(true);

            //Initially display score
            updateScoreDisplay(gameToUpdate.getScoreTeamA(), gameToUpdate.getScoreTeamB());
            initRemark();
        }
        catch (Exception ex) {
            showMessage(getString(R.string.Error) + ": " + ex.getMessage());
        }
    }

    private void getAllViews() {
        mViewPager = (ViewPager) findViewById(R.id.pager);
        tablayout = (TabLayout) findViewById(R.id.tabs);
        txtScore = (TextView) findViewById(R.id.txtScore);
        edtRemark = (EditText) findViewById(R.id.edtRemark);
        btnBack = (Button) findViewById(R.id.btnBack);
        btnSave = (Button) findViewById(R.id.btnSave);
        pb = (ProgressBar) findViewById(R.id.progressBar);
    }

    private void registrateEventHandlers() {
        btnBack.setOnClickListener(this);
        btnSave.setOnClickListener(this);
    }

    private void toggleProgressBar(boolean isEnabled) {
        if (isEnabled) {
            pb.setVisibility(View.VISIBLE);
        }
        else {
            pb.setVisibility(View.GONE);
        }
    }

    private void initRemark() {
        edtRemark.setText(gameToUpdate.getRemark());
    }

    private void setupViewPager(ViewPager viewPager){
        tabs = new TabAddGameEnterData[2];

        for (int i=0; i<tabs.length; i++) {
            tabs[i] = new TabAddGameEnterData();
            tabs[i].setOnScoreChangedListener(this);
        }

        Bundle bundle = new Bundle();
        bundle.putSerializable("participations", getParticipationsOfTeam(Team.TEAM1));
        tabs[0].setArguments(bundle);

        bundle = new Bundle();
        bundle.putSerializable("participations", getParticipationsOfTeam(Team.TEAM2));
        tabs[1].setArguments(bundle);

        adapter.addFragment(tabs[0],"TEAM1");
        adapter.addFragment(tabs[1],"TEAM2");
        viewPager.setAdapter(adapter);
    }

    private ArrayList<Participation> getParticipationsOfTeam(Team team) {
        ArrayList<Participation> participationsOfTeam = new ArrayList<>();

        for (Participation p: gameToUpdate.getParticipations()) {
            if (p.getTeam().equals(team)) {
                participationsOfTeam.add(p);
            }
        }

        return participationsOfTeam;
    }

    @Override
    public void onScoreUpdated(int sumGoalsShot, Fragment fragment) {
        if (tabs[0].equals(fragment)) {
            gameToUpdate.setScoreTeamA(sumGoalsShot);
        }
        else {
            gameToUpdate.setScoreTeamB(sumGoalsShot);
        }
        updateScoreDisplay(gameToUpdate.getScoreTeamA(), gameToUpdate.getScoreTeamB());
    }

    private void updateScoreDisplay(int scoreTeamA, int scoreTeamB) {
        txtScore.setText(scoreTeamA + ":" + scoreTeamB);
    }

    private void onBtnSaveClick() throws Exception {
        int[][] goalData = new int[2][2];

        for (int i = 0; i < tabs.length; i++) {
            for (Participation p : tabs[i].getParticipationsFromTable()) {
                tabs[i].forceScoreRecalculation();
                gameToUpdate.removeParticipation(p);
                gameToUpdate.addParticipation(p);

                goalData[i][0] += p.getNumGoalsShotDefault()+p.getNumGoalsShotHead()+
                        p.getNumGoalsShotHeadSnow()+p.getNumGoalsShotPenalty();
                goalData[i][1] += p.getNumGoalsGot();
            }
        }

        //If goalsShot of TeamA != goalsGot of TeamB and vice versa
        if (!(goalData[0][0]==goalData[1][1] && goalData[0][1]==goalData[1][0])) {
            throw new Exception(getString(R.string.msg_InconsistentGoalData));
        }
        else {
            if (!NamePWValidator.validate(edtRemark.getText().toString())) {
                throw new Exception(getString(R.string.msg_IllegalRemark));
            }

            gameToUpdate.setRemark(edtRemark.getText().toString());

            toggleProgressBar(true);
            db.update(gameToUpdate, this);
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
            showMessage(getString(R.string.Error) + ": " + e.getMessage());
        }
    }

    @Override
    public void updateGameFinished(UpdateGameHandler handler) {
        toggleProgressBar(false);
        if (handler.getException() == null) {
            showMessage(getString(R.string.msg_SavedGame));

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        else {
            toggleProgressBar(false);
            showMessage(getString(R.string.Error) + ": " + getString(R.string.msg_CouldNotUpdateGame));
        }
    }

    @Override
    public void loadParticipationsFinished(LoadParticipationsHandler handler) {
        if (handler.getException() == null) {
            gameToUpdate.removeAllParticipations();
            for (Participation part: handler.getPatrticipations()) {
                gameToUpdate.addParticipation(part);
            }

            tabs[0].setParticipations(getParticipationsOfTeam(Team.TEAM1));
            tabs[1].setParticipations(getParticipationsOfTeam(Team.TEAM2));

            toggleProgressBar(false);
        }
        else {
            showMessage(getString(R.string.Error) + ": " + getString(R.string.msg_CannotConnectToWebservice));
            toggleProgressBar(false);
        }
    }
}
