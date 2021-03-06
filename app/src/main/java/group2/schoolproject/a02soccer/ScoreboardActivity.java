package group2.schoolproject.a02soccer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.TreeSet;

import pkgAdapter.CustomSpinnerAdapter;
import pkgAdapter.ScoreboardListAdapter;
import pkgComparator.PlayerComparatorDefeats;
import pkgComparator.PlayerComparatorGoalsGot;
import pkgComparator.PlayerComparatorGoalsShot;
import pkgComparator.PlayerComparatorWins;
import pkgData.Player;
import pkgDatabase.Database;
import pkgMisc.PlayerWithScore;

/**
 * @author Raphael Moser
 */
public class ScoreboardActivity extends BaseActivity implements AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {
    private ListView lsv = null;
    private Spinner spSorting = null;
    private Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.title_activity_score_board);
        setContentView(R.layout.activity_scoreboard);
        db = Database.getInstance();
        getViews();
        setListeners();
        fillSpinner();
    }

    private void getViews(){
        lsv = (ListView) findViewById(R.id.lsv_Score);
        spSorting = (Spinner) findViewById(R.id.spSorting);
    }

    private void setListeners(){
        spSorting.setOnItemSelectedListener(this);
        lsv.setOnItemClickListener(this);
    }

    private void fillSpinner() {
        ArrayList<String> values = new ArrayList<>();
        values.add(getString(R.string.wins));
        values.add(getString(R.string.GoalsShotAll));
        values.add(getString(R.string.GoalsGot));
        values.add(getString(R.string.defeats));

        CustomSpinnerAdapter<String> spAdapter = new CustomSpinnerAdapter<>(this, values);
        spSorting.setAdapter(spAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        TreeSet<Player> ts = new TreeSet<>();
        try {
            ts.addAll(Database.getInstance().getCachedPlayers());
            switch (position) {
                case 0:
                    lsv.setAdapter(createAdapter(ts, getString(R.string.Scoreboard_titles_wins), new PlayerComparatorWins()));
                    break;
                case 1:
                    lsv.setAdapter(createAdapter(ts, getString(R.string.Scoreboard_titles_goalsshot), new PlayerComparatorGoalsShot()));
                    break;
                case 2:
                    lsv.setAdapter(createAdapter(ts, getString(R.string.Scoreboard_titles_goalsgot), new PlayerComparatorGoalsGot()));
                    break;
                case 3:
                    lsv.setAdapter(createAdapter(ts, getString(R.string.Scoreboard_titles_defeats), new PlayerComparatorDefeats()));
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ScoreboardListAdapter createAdapter(TreeSet<Player> ts, String title, Comparator<Player> comparator){
        setTitle(title);
        TreeSet<Player> ts2 = new TreeSet<>(comparator);
        ts2.addAll(ts);
        ArrayList<PlayerWithScore> list = new ArrayList<>();
        for (Player p : ts2) {
            if (comparator.getClass() == PlayerComparatorWins.class) {
                list.add(new PlayerWithScore(p.toString(), p.getStatistics().getNumWins(), p.getUsername()));
            } else if (comparator.getClass() == PlayerComparatorDefeats.class) {
                list.add(new PlayerWithScore(p.toString(), p.getStatistics().getNumDefeats(), p.getUsername()));
            } else if (comparator.getClass() == PlayerComparatorGoalsShot.class) {
                list.add(new PlayerWithScore(p.toString(), p.getStatistics().getNumGoalsShotAll(), p.getUsername()));
            } else if (comparator.getClass() == PlayerComparatorGoalsGot.class) {
                list.add(new PlayerWithScore(p.toString(), p.getStatistics().getNumGoalsGot(), p.getUsername()));
            }
        }
        return (new ScoreboardListAdapter(this, list));
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Player selectedPlayer = db.getPlayerByUsername(((PlayerWithScore) lsv.getItemAtPosition(position)).getUserame());
        Intent myIntent = new Intent(this, ShowPlayerStatsActivity.class);
        myIntent.putExtra("player", selectedPlayer);
        this.startActivity(myIntent);
    }
}

