package group2.schoolproject.a02soccer;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import pkgAdapter.SectionsPageAdapter;
import pkgData.Game;
import pkgData.Participation;
import pkgData.Team;
import pkgDatabase.Database;
import pkgDatabase.LoadParticipationsHandler;
import pkgDatabase.pkgListener.OnLoadParticipationsListener;
import pkgListeners.OnScoreChangedListener;
import pkgMisc.LocalizedDateFormatter;
import pkgTab.TabAddGameEnterData;

/**
 * @author Elias Santner
 */

public class ShowGameActivity extends BaseActivity implements OnLoadParticipationsListener, OnScoreChangedListener {

    private SectionsPageAdapter adapter = null;
    private ViewPager mViewPager = null;
    private TabLayout tablayout = null;
    private ProgressBar pb = null;
    private TextView txtScore = null,
                     txvRemark = null,
                     txvDate = null;

    private Game tmpGame = null;
    private Database db = null;

    private TabAddGameEnterData[] tabs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_game);
        setTitle(R.string.title_activity_display_game);
        try {
            getAllViews();

            db = Database.getInstance();
            tmpGame = (Game) this.getIntent().getSerializableExtra("game");

            if (tmpGame == null) {
                throw new Exception("Please call activity with intent-extra 'game'");
            }

            adapter = new SectionsPageAdapter(getSupportFragmentManager());
            setupViewPager(mViewPager);
            tablayout.setupWithViewPager(mViewPager);

            db.getParticipationsOfGame(tmpGame, this);
            toggleProgressBar(true);
            init();
        }
        catch (Exception ex) {
            ex.printStackTrace();
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

    private void init() {
        updateScoreDisplay(tmpGame.getScoreTeamA(), tmpGame.getScoreTeamB());
        displayRemark(tmpGame.getRemark());
        displayDate(tmpGame.getDate());
    }

    private void displayRemark(String remark) {
        if (remark.trim().isEmpty()) {
            remark = getString(R.string.NoRemark);
        }
        txvRemark.setText(remark);
    }

    private void displayDate(Date date) {
        txvDate.setText(LocalizedDateFormatter.format(date, Locale.getDefault()));
    }

    private void getAllViews() {
        mViewPager = (ViewPager) findViewById(R.id.pager);
        tablayout = (TabLayout) findViewById(R.id.tabs);
        txtScore = (TextView) findViewById(R.id.txtScore);
        txvRemark = (TextView) findViewById(R.id.txvRemark);
        txvDate = (TextView) findViewById(R.id.txvDate);
        pb = (ProgressBar) findViewById(R.id.progressBar);
    }

    private void setupViewPager(ViewPager viewPager){
        tabs = new TabAddGameEnterData[2];

        for (int i=0; i<tabs.length; i++) {
            tabs[i] = new TabAddGameEnterData();
        }

        Bundle bundle = new Bundle();
        bundle.putSerializable("participations", getParticipationsOfTeam(Team.TEAM1));
        bundle.putSerializable("isEditable", false);
        tabs[0].setArguments(bundle);
        tabs[0].setOnScoreChangedListener(this);

        bundle = new Bundle();
        bundle.putSerializable("participations", getParticipationsOfTeam(Team.TEAM2));
        bundle.putSerializable("isEditable", false);
        tabs[1].setArguments(bundle);
        tabs[1].setOnScoreChangedListener(this);

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

    private void updateScoreDisplay(int scoreTeamA, int scoreTeamB) {
        txtScore.setText(scoreTeamA + ":" + scoreTeamB);
    }

    @Override
    public void onScoreUpdated(int sumGoalsShot, Fragment fragment) {
        //Only to display message from Tabs
    }

    @Override
    public void loadParticipationsFinished(LoadParticipationsHandler handler) {
        toggleProgressBar(false);
        if (handler.getException() == null) {
            tmpGame.removeAllParticipations();
            for (Participation part: handler.getPatrticipations()) {
                tmpGame.addParticipation(part);
            }

            tabs[0].setParticipations(getParticipationsOfTeam(Team.TEAM1));
            tabs[1].setParticipations(getParticipationsOfTeam(Team.TEAM2));
        }
        else {
            showMessage(getString(R.string.Error) + ": " + getString(R.string.msg_CannotConnectToWebservice));
        }
    }
}
