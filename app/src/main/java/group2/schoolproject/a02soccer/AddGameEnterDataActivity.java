package group2.schoolproject.a02soccer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
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
import pkgDatabase.InsertGameHandler;
import pkgDatabase.pkgListener.OnGameInsertedListener;
import pkgListeners.OnScoreChangedListener;
import pkgMisc.NamePWValidator;
import pkgTab.TabAddGameEnterData;

/**
 * @author Elias Santner
 */

public class AddGameEnterDataActivity extends BaseActivity implements OnScoreChangedListener, View.OnClickListener,
        OnGameInsertedListener, TabLayout.OnTabSelectedListener, TextView.OnEditorActionListener {

    private SectionsPageAdapter adapter = null;
    private ViewPager mViewPager = null;
    private TabLayout tablayout = null;

    private TextView txtScore = null;
    private EditText edtRemark = null;
    private Button btnBack = null,
                   btnSave = null;
    private ProgressBar pb = null;

    private Game game = null;
    private Database db = null;

    private TabAddGameEnterData[] tabs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_game_enter_data);
        setTitle(R.string.title_activity_add_game_enter_data);
        try {
            getAllViews();
            registrateEventHandlers();

            db = Database.getInstance();
            game = (Game) this.getIntent().getSerializableExtra("game");

            if (game == null) {
                throw new Exception("Please call activity with intent-extra 'game'");
            }

            //Exit activity when user is not an admin (shouldn't happen)
            if (!db.getCurrentlyLoggedInPlayer().isAdmin()) {
                this.finish();
            }

            adapter = new SectionsPageAdapter(getSupportFragmentManager());
            setupViewPager(mViewPager);
            tablayout.setupWithViewPager(mViewPager);
            tablayout.setOnTabSelectedListener(this);

            //Initially display score
            updateScoreDisplay(game.getScoreTeamA(), game.getScoreTeamB());
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
        edtRemark.setOnEditorActionListener(this);
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

        for (Participation p: game.getParticipations()) {
            if (p.getTeam().equals(team)) {
                participationsOfTeam.add(p);
            }
        }

        return participationsOfTeam;
    }

    @Override
    public void onScoreUpdated(int sumGoalsShot, Fragment fragment) {
        if (tabs[0].equals(fragment)) {
            game.setScoreTeamA(sumGoalsShot);
        }
        else {
            game.setScoreTeamB(sumGoalsShot);
        }
        updateScoreDisplay(game.getScoreTeamA(), game.getScoreTeamB());
    }

    private void updateScoreDisplay(int scoreTeamA, int scoreTeamB) {
        txtScore.setText(scoreTeamA + ":" + scoreTeamB);
    }

    private void onBtnSaveClick() throws Exception {
        int[][] goalData = new int[2][2];

        for (int i = 0; i < tabs.length; i++) {
            for (Participation p : tabs[i].getParticipationsFromTable()) {
                tabs[i].forceScoreRecalculation();
                game.removeParticipation(p);
                game.addParticipation(p);

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

            game.setRemark(edtRemark.getText().toString());

            toggleProgressBar(true);

            if (db.isOnline()) {
                db.insert(game, this);
            }
            else {
                db.insertGameLocally(game);
                showMessage(getString(R.string.msg_DataSavedLocally));
                toggleProgressBar(false);
                openMainActivity();
            }
        }
    }

    private void toggleProgressBar(boolean isEnabled) {
        if (isEnabled) {
            pb.setVisibility(View.VISIBLE);
        }
        else {
            pb.setVisibility(View.GONE);
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
            e.printStackTrace();
        }
    }

    @Override
    public void insertGameFinished(InsertGameHandler handler) {
        toggleProgressBar(false);
        if (handler.getException() == null) {
            showMessage(getString(R.string.msg_SavedGame));
            openMainActivity();
        }
        else {
            showMessage(getString(R.string.Error) + ": " + getString(R.string.msg_CouldNotInsertGame));
        }
    }

    private void closeKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
    }

    private void openMainActivity() {
        finish();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("showPlayers", false);
        startActivity(intent);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        closeKeyboard();
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        closeKeyboard();
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        closeKeyboard();
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            closeKeyboard();
        }
        return false;
    }
}
