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

import pkgData.Game;
import pkgData.Participation;
import pkgData.Player;
import pkgData.PlayerPosition;
import pkgData.Team;
import pkgListeners.OnTeamChangedListener;
import pkgAdapter.SectionsPageAdapter;
import pkgTab.TeamDivisionTab;

public class TeamDivisionActivity extends BaseActivity implements OnTeamChangedListener, View.OnClickListener {

    private SectionsPageAdapter mSectionsPageAdapter;
    private Game game = null;
    private ArrayList<Participation> participations = null;
    private TreeMap<Integer, Player> allPlayers = null;
    private OnTeamChangedListener[] listener = new OnTeamChangedListener[2];


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
                //showMessage("folgt");
                shuffle();
            }
        } catch (Exception e) {
            showMessage(e.getMessage());
        }
    }


    private boolean shuffleGoalie() {
        boolean retVal = true;
        Random rand = new Random();
        TeamDivisionTab team1 = ((TeamDivisionTab) mSectionsPageAdapter.getItem(0));
        TeamDivisionTab team2 = ((TeamDivisionTab) mSectionsPageAdapter.getItem(1));
        ArrayList<Integer> freeOnlyGoalies = team1.getGoalieOnlyId();
        if (freeOnlyGoalies.size() > 2) {
            retVal = false;
            showMessage("mehr als 2 Spieler die nur Tormann spielen können");
        } else {
            try {
                int diff = team1.Teammembercount() - team2.Teammembercount(); // wenn negativ müssen leute in team1, sonst team2
                if (freeOnlyGoalies.size() != 0) {
                    if (diff < 0) {
                        for (int i = 0; i > diff && !team1.hasOnlyOneGoalkeeper();) {
                            team1.movePlayerRow(freeOnlyGoalies.remove(rand.nextInt(freeOnlyGoalies.size())));
                            diff++;
                        }
                    } else if (diff > 0) {
                        for (int i = 0; i < diff && !team2.hasOnlyOneGoalkeeper();) {
                            team2.movePlayerRow(freeOnlyGoalies.remove(rand.nextInt(freeOnlyGoalies.size())));
                            diff--;
                        }
                    }
                    if (diff == 0 && freeOnlyGoalies.size() != 0) {
                        for (int i = 0; i < freeOnlyGoalies.size(); i++) {
                            if (!team1.hasOnlyOneGoalkeeper()) {
                                team1.movePlayerRow(freeOnlyGoalies.remove(rand.nextInt(freeOnlyGoalies.size())));
                                diff++;
                            } else if (diff != 0 &&!team2.hasOnlyOneGoalkeeper()) {
                                team2.movePlayerRow(freeOnlyGoalies.remove(rand.nextInt(freeOnlyGoalies.size())));
                                diff--;
                            } else {
                                retVal = false;
                                showMessage("Tormann kann nicht zugewiesen werden");
                            }
                        }
                    }
                }
            } catch (Exception e) {
                showMessage(e.getMessage());
                retVal = false;
            }
        }
        return retVal;
    }

    private void shuffle() {
        if (shuffleGoalie()) {
            Random rand = new Random();
            TeamDivisionTab team1 = ((TeamDivisionTab) mSectionsPageAdapter.getItem(0));
            TeamDivisionTab team2 = ((TeamDivisionTab) mSectionsPageAdapter.getItem(1));
            ArrayList<Integer> freePlayers = ((TeamDivisionTab) mSectionsPageAdapter.getItem(0)).getFreePlayerids();
            int diff = team1.Teammembercount() - team2.Teammembercount(); // wenn negativ müssen leute in team1, sonst team2
            if (freePlayers.size() != 0) {
                if (diff < 0) {
                    for (int i = 0; i > diff; diff++) {
                        team1.movePlayerRow(freePlayers.remove(rand.nextInt(freePlayers.size())));
                    }
                } else if (diff > 0) {
                    for (int i = 0; i < diff; diff--) {
                        team2.movePlayerRow(freePlayers.remove(rand.nextInt(freePlayers.size())));
                    }
                }
                if (diff == 0 && freePlayers.size() != 0) {
                    for (int i = 0; i < freePlayers.size(); ) {
                        if (diff == 0) {
                            team1.movePlayerRow(freePlayers.remove(rand.nextInt(freePlayers.size())));
                            diff++;
                        } else if (diff != 0) {
                            team2.movePlayerRow(freePlayers.remove(rand.nextInt(freePlayers.size())));
                            diff--;
                        }
                    }
                }
            } else {
                showMessage("Keine Spieler zum aufteilen");
            }
        }
    }
}

