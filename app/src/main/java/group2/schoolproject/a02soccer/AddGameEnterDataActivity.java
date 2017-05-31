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

    private Game tmpGame = null;
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
            tmpGame = (Game) this.getIntent().getSerializableExtra("game");

            if (tmpGame == null) {
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
            updateScoreDisplay(tmpGame.getScoreTeamA(), tmpGame.getScoreTeamB());
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
        txtScore.setText(scoreTeamA + ":" + scoreTeamB);
    }

    private void onBtnSaveClick() throws Exception {
        int[][] goalData = new int[2][2];

        for (int i = 0; i < tabs.length; i++) {
            for (Participation p : tabs[i].getParticipationsFromTable()) {
                tabs[i].forceScoreRecalculation();
                tmpGame.removeParticipation(p);
                tmpGame.addParticipation(p);

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

            tmpGame.setRemark(edtRemark.getText().toString());

            db.insert(tmpGame, this);

            toggleProgressBar(true);
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

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        else {
            showMessage(getString(R.string.Error) + ": " + getString(R.string.msg_CouldNotInsertGame));
        }
    }

    private void closeKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
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
