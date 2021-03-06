package group2.schoolproject.a02soccer;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;
import java.util.TreeMap;

import pkgAdapter.SectionsPageAdapter;
import pkgData.Game;
import pkgData.Participation;
import pkgData.Player;
import pkgData.Team;
import pkgListeners.OnOkDialogButtonPressedListener;
import pkgListeners.OnTeamChangedListener;
import pkgTab.TeamDivisionTab;

public class TeamDivisionActivity extends BaseActivity implements OnTeamChangedListener, View.OnClickListener, OnOkDialogButtonPressedListener {

    private SectionsPageAdapter mSectionsPageAdapter;
    private Game game = null;
    private ArrayList<Participation> participations = null;
    private TreeMap<Integer, Player> allPlayers = null;
    private OnTeamChangedListener[] listener = new OnTeamChangedListener[2];
    private boolean isWarned;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.title_activity_team_management);
        ViewPager viewPager;
        game = (Game) this.getIntent().getSerializableExtra("game");
        allPlayers = new TreeMap<>();
        ArrayList<Player> temp = (ArrayList<Player>) getIntent().getSerializableExtra("players");
        for (Player p : temp) {
            allPlayers.put(p.getId(), p);
        }
        isWarned = false;
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
        } else if (Math.abs((list1.size() - list2.size())) > 1) {
            throw new Exception(getString(R.string.msg_UnbalancedTeams));
        }
        list1.addAll(list2);
        participations = list1;
    }


    private void createWarning(String s){
            OkDialog dia = new OkDialog(this, s);
            dia.show();
            isWarned = true;
    }

    @Override
    public void okDialogButtonPressed() {
        System.out.println("des mocht anfoch nichts");
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
            showMessage(
                    e.getMessage());
        }
    }

    @Override
    public void onClick(View button) {
        try {
            if (button.getId() == R.id.btnContinue) {
                createParticipations();
                game.removeAllParticipations();
                for (Participation p : participations) {
                    game.addParticipation(p);
                }
                Intent myIntent = new Intent(this, AddGameEnterDataActivity.class);
                myIntent.putExtra("game", game);
                this.startActivity(myIntent);
            } else if (button.getId() == R.id.btnCancel) {
                this.finish();
            } else if (button.getId() == R.id.btnShuffle) {
                shuffle();
                if(!isWarned) {
                    createWarning(getString(R.string.msg_ShuffleWarning));
                }
            }
        } catch (Exception e) {
            showMessage(e.getMessage());
        }
    }


    private void shuffle() throws Exception{
        Random rand = new Random();
        TeamDivisionTab team1 = ((TeamDivisionTab) mSectionsPageAdapter.getItem(0));
        TeamDivisionTab team2 = ((TeamDivisionTab) mSectionsPageAdapter.getItem(1));
        ArrayList<Integer> freePlayers = ((TeamDivisionTab) mSectionsPageAdapter.getItem(0)).getFreePlayerids();

        int diff;
        if (freePlayers.size() != 0) {
            while (freePlayers.size() != 0) {
                diff = team1.Teammembercount() - team2.Teammembercount();

                if (diff < 0) {
                    team1.movePlayerRow(freePlayers.remove(rand.nextInt(freePlayers.size())));
                } else if (diff > 0){
                    team2.movePlayerRow(freePlayers.remove(rand.nextInt(freePlayers.size())));
                }
                else {
                    if (rand.nextFloat() > 0.5f)
                        team1.movePlayerRow(freePlayers.remove(rand.nextInt(freePlayers.size())));
                    else
                        team2.movePlayerRow(freePlayers.remove(rand.nextInt(freePlayers.size())));
                }
            }
        } else {
            showMessage(getString(R.string.msg_NoPlayersToAssign));
        }
    }
}


