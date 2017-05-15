package group2.schoolproject.a02soccer;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.TreeMap;

import pkgData.Game;
import pkgData.Participation;
import pkgData.Player;
import pkgData.Team;
import pkgListeners.OnTeamChangedListener;
import pkgTab.SectionsPageAdapter;
import pkgTab.TeamDivisionTab;

public class TeamDivisionActivity extends AppCompatActivity implements OnTeamChangedListener, View.OnClickListener {

    private SectionsPageAdapter mSectionsPageAdapter;
    private Game game = null;
    private ArrayList<Participation> participations = null;
    private TreeMap<Integer, Player> allPlayers = null;
    private OnTeamChangedListener[] listener = new OnTeamChangedListener[2];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewPager viewPager;
        game = (Game) this.getIntent().getSerializableExtra("game");
        allPlayers = new TreeMap<>();
        ArrayList<Player> temp = (ArrayList<Player>) getIntent().getSerializableExtra("players");
        for (Player p : temp) {
            allPlayers.put(p.getId(), p);
        }
        setContentView(R.layout.activity_team_division);
        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(viewPager);
        setOnClickListener();
        TabLayout tablayout = (TabLayout) findViewById(R.id.tabs);
        tablayout.setupWithViewPager(viewPager);
    }

    private void setOnClickListener() {
        this.findViewById(R.id.btnContinue).setOnClickListener(this);
        this.findViewById(R.id.btnCancel).setOnClickListener(this);
        this.findViewById(R.id.btnShuffle).setOnClickListener(this);
    }

    private void setupViewPager(ViewPager viewPager) {
        SectionsPageAdapter adapter = mSectionsPageAdapter;
        Bundle bundle1 = new Bundle();
        bundle1.putSerializable("Players", allPlayers);
        bundle1.putSerializable("Team", Team.TEAM1);
        Fragment team1 = new TeamDivisionTab();
        listener[0] = (TeamDivisionTab) team1;
        ((TeamDivisionTab) team1).setOnTeamChangedListener(this);
        team1.setArguments(bundle1);
        adapter.addFragment(team1, "TEAM1");
        Bundle bundle2 = new Bundle();
        bundle2.putSerializable("Players", allPlayers);
        bundle2.putSerializable("Team", Team.TEAM2);
        Fragment team2 = new TeamDivisionTab();
        listener[1] = (TeamDivisionTab) team2;
        team2.setArguments(bundle2);
        ((TeamDivisionTab) team2).setOnTeamChangedListener(this);
        adapter.addFragment(team2, "TEAM2");
        viewPager.setAdapter(adapter);
    }

    public void createParticipations() throws Exception {
        ArrayList<Participation> list1 = ((TeamDivisionTab) mSectionsPageAdapter.getItem(0)).getPlayersInTeam();
        ArrayList<Participation> list2 = ((TeamDivisionTab) mSectionsPageAdapter.getItem(1)).getPlayersInTeam();
        if (list1.size() + list2.size() != allPlayers.size()) {
            throw new Exception(getString(R.string.msg_PlayerhasNoTeam));
        }
        else if(Math.abs((list1.size()-list2.size())) > 1){
            throw new Exception(getString(R.string.msg_UnbalancedTeams));
        }
        list1.addAll(list2);
        participations = list1;
    }

    @Override
    public void onTeamUpdated(Player p, Team t, boolean remove) {
        try {
            int id = p.getId();
            allPlayers.put(id, p);
            if (remove) {
                if (t == Team.TEAM1) {
                    listener[1].onTeamUpdated(p, t, true);
                } else if (t == Team.TEAM2) {
                    listener[0].onTeamUpdated(p, t, true);
                }
            } else {
                if (t == Team.TEAM1) {
                    listener[1].onTeamUpdated(p, t, false);
                } else if (t == Team.TEAM2) {
                    listener[0].onTeamUpdated(p, t, false);
                }
            }
        } catch (Exception e) {
            showToast(e.getMessage());
        }
    }

    @Override
    public void onClick(View button) {
        if (button.getId() == R.id.btnContinue) {
            try {
                createParticipations();
                game.removeAllParticipations();
                for (Participation p : participations) {
                    game.addParticipation(p);
                }
                Intent myIntent = new Intent(this, AddGameEnterDataActivity.class);
                myIntent.putExtra("game", game);
                this.startActivity(myIntent);
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else if (button.getId() == R.id.btnCancel) {
            this.finish();
        } else if (button.getId() == R.id.btnShuffle) {
            Toast.makeText(this, "folgt", Toast.LENGTH_SHORT).show();
        }
    }

    private void showToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}

