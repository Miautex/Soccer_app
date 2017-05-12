package group2.schoolproject.a02soccer;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
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
    private ViewPager mViewPager;
    private Game game = null;
    private ArrayList<Participation> participations = null;
    private TreeMap<Integer,Player> allPlayers = null;
    private OnTeamChangedListener[] listener = new OnTeamChangedListener[2];



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        game = (Game)this.getIntent().getSerializableExtra("game");
        allPlayers = new TreeMap<>();
        ArrayList<Player> temp = (ArrayList<Player>) getIntent().getSerializableExtra("players");
        for(Player p : temp){
            allPlayers.put(p.getId(),p);
        }
        setContentView(R.layout.activity_team_division);
        // get Game
        //get Participations in List
        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);
        ((Button)this.findViewById(R.id.btnSave)).setOnClickListener(this);

        TabLayout tablayout = (TabLayout) findViewById(R.id.tabs);
        tablayout.setupWithViewPager(mViewPager);



    }

    private void getPlayersFromParticipations(){
        /*for (Participation p : temp){
            Player player = p.getPlayer();
            allPlayers.put(player.getId(),player);
        }*/
        try {

            allPlayers.put(1, new Player(1, "a", "andererPlayer", true));
            allPlayers.put(2, new Player(2, "a", "q", true));
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setupViewPager(ViewPager viewPager){
        SectionsPageAdapter adapter = mSectionsPageAdapter;
        Bundle bundle1 = new Bundle();
        bundle1.putSerializable("Players",allPlayers);
        bundle1.putSerializable("Team", Team.TEAM1);
        Fragment team1 = new TeamDivisionTab();
        listener[0] = (TeamDivisionTab) team1;
        ((TeamDivisionTab)team1).setOnTeamChangedListener(this);
        team1.setArguments(bundle1);
        adapter.addFragment(team1,"TEAM1");
        Bundle bundle2 = new Bundle();
        bundle2.putSerializable("Players",allPlayers);
        bundle2.putSerializable("Team", Team.TEAM2);
        Fragment team2  = new TeamDivisionTab();
        listener[1] = (TeamDivisionTab) team2;
        team2.setArguments(bundle2);
        ((TeamDivisionTab)team2).setOnTeamChangedListener(this);
        adapter.addFragment(team2,"TEAM2");
        viewPager.setAdapter(adapter);
    }

    public ArrayList<Participation> createParticipations() throws Exception {
        //getPlayersFromParticipations();
        ArrayList<Participation> list1 = ((TeamDivisionTab) mSectionsPageAdapter.getItem(0)).getPlayersInTeam();
        ArrayList<Participation> list2 = ((TeamDivisionTab) mSectionsPageAdapter.getItem(1)).getPlayersInTeam();
        if (list1.size() + list2.size() != allPlayers.size()) {
            //try {
                throw new Exception("there are players without team");
            //} catch (Exception e) {
              //  Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT);
            //}
        }
        list1.addAll(list2);
        participations = list1;
        return participations;
    }


    @Override
    public void onTeamUpdated(Player p, Team t, boolean remove) {
        try {
            int id = p.getId();
            allPlayers.put(id,p);
            if(remove == true) {
                if (t == Team.TEAM1) {
                    listener[1].onTeamUpdated(p, t, remove);
                } else if (t == Team.TEAM2) {
                    listener[0].onTeamUpdated(p, t, remove);
                }
            }
            else{
                if (t == Team.TEAM1) {
                    listener[1].onTeamUpdated(p, t, remove);
                } else if (t == Team.TEAM2) {
                    listener[0].onTeamUpdated(p, t, remove);
                }
            }
        }
        catch (Exception e){
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT);
        }


    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnSave){
            try {
                createParticipations();

                Game newGame = new Game(game.getDate(), game.getScoreTeamA(), game.getScoreTeamB());

                for(Participation p : participations){
                    newGame.addParticipation(p);
                }
                Intent myIntent = new Intent(this, AddGameEnterDataActivity.class);
                myIntent.putExtra("game", newGame);

                this.startActivity(myIntent);
            }
            catch (Exception e){
                Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }

        }
    }
}

