package group2.schoolproject.a02soccer;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

import pkgData.Game;
import pkgData.Participation;
import pkgData.Team;
import pkgTab.SectionsPageAdapter;
import pkgTab.TabAddGameEnterData;

public class AddGameEnterDataActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {

    private static final String TAG = "EnterData";

    private SectionsPageAdapter adapter = null;
    private ViewPager mViewPager = null;
    private TabLayout tablayout = null;
    private Game tmpGame = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_game_enter_data);

        try {
            getAllViews();

            tmpGame = (Game) this.getIntent().getSerializableExtra("game");

            if (tmpGame == null) {
                throw new Exception("Please call activity with intent-extra 'game'");
            }

            SectionsPageAdapter mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());
            setupViewPager(mViewPager);
            tablayout.setupWithViewPager(mViewPager);
            tablayout.setOnTabSelectedListener(this);
        }
        catch (Exception ex) {
            ExceptionNotification.notify(this, ex);
            ex.printStackTrace();
        }
    }

    private void getAllViews() {
        mViewPager = (ViewPager) findViewById(R.id.pager);
        tablayout = (TabLayout) findViewById(R.id.tabs);
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
        fragment.setParticipations(tmpGame.getParticipations());
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

    }
}
