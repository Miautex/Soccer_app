package group2.schoolproject.a02soccer;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.TreeSet;

import pkgData.Game;
import pkgData.Participation;
import pkgData.Player;
import pkgData.Team;
import pkgListeners.OnTeamChangedListener;
import pkgTab.SectionsPageAdapter;
import pkgTab.TeamDivisionTab1;
import pkgTab.TeamDivisionTab2;

public class TeamManagmentActivity extends AppCompatActivity implements OnTeamChangedListener {

    private SectionsPageAdapter mSectionsPageAdapter;
    private ViewPager mViewPager;
    private Game game = null;
    private ArrayList<Participation> temp = null;
    private TreeSet<Player> allPlayers = null;
    private TreeSet<Player> team1 = null;
    private TreeSet<Player> team2 = null;
    private OnTeamChangedListener[] listener = new OnTeamChangedListener[2];



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_managment);
        // get Game
        //get Participations in List
        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);

        TabLayout tablayout = (TabLayout) findViewById(R.id.tabs);
        tablayout.setupWithViewPager(mViewPager);



    }

    private void getPlayersFromParticipations(){
        for (Participation p : temp){
            allPlayers.add(p.getPlayer());
        }
    }

    private void setupViewPager(ViewPager viewPager){
        SectionsPageAdapter adapter = mSectionsPageAdapter;
        Bundle bundle1 = new Bundle();
        bundle1.putSerializable("Players",allPlayers);
        bundle1.putSerializable("Team", Team.TEAM1);
        Fragment team1 = new TeamDivisionTab1();
        listener[0] = (TeamDivisionTab1) team1;
        ((TeamDivisionTab1)team1).setOnTeamChangedListener(this);
        team1.setArguments(bundle1);
        adapter.addFragment(team1,"TEAM1");
        Bundle bundle2 = new Bundle();
        bundle2.putSerializable("Players",allPlayers);
        bundle2.putSerializable("Team", Team.TEAM2);
        Fragment team2  = new TeamDivisionTab1();
        listener[1] = (TeamDivisionTab1) team2;
        team2.setArguments(bundle2);
        ((TeamDivisionTab1)team2).setOnTeamChangedListener(this);
        adapter.addFragment(team2,"TEAM2");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onTeamUpdated(Player p, Team t) {
        listener[0].onTeamUpdated(p,t);
    }
}

